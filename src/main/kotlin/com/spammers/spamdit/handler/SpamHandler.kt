package com.spammers.spamdit.handler

import com.spammers.spamdit.model.Spam
import com.spammers.spamdit.model.Topic
import com.spammers.spamdit.repository.SpamRepository
import com.spammers.spamdit.repository.TopicRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class SpamHandler (@Autowired var spamRepository: SpamRepository,
                   @Autowired var topicRepository: TopicRepository){

    @FlowPreview
    suspend fun getSpam(request: ServerRequest): ServerResponse =
        ServerResponse.ok().json().bodyAndAwait(spamRepository.findAll().asFlow())

    suspend fun getSpamById(request: ServerRequest): ServerResponse {
        val spam: Deferred<Spam?> = GlobalScope.async {
            spamRepository.findById(request.pathVariable("id")).awaitFirstOrNull()
        }

        return spam.await()?.let { ServerResponse.ok().bodyValueAndAwait(it) } ?:
            ServerResponse.notFound().buildAndAwait()
    }

    suspend fun saveSpam(request: ServerRequest): ServerResponse {
        System.out.println(request)
        val spam: Deferred<Spam?> = GlobalScope.async {
            spamRepository.save(request.awaitBody<Spam>()).awaitFirstOrNull()
        }

        return spam.await()?.let { ServerResponse.ok().bodyValueAndAwait(it) } ?:
            ServerResponse.notFound().buildAndAwait()
    }

    suspend fun updateSpam(request: ServerRequest): ServerResponse {
        System.out.println(request)

        val spam: Deferred<Spam?> = GlobalScope.async {
            spamRepository.findById(request.pathVariable("id")).awaitFirstOrNull()
        }

        return spam.await()?.let {
            val savedSpam: Deferred<Spam?> = GlobalScope.async {
                spamRepository.save(request.awaitBody<Spam>()).awaitFirstOrNull()
            }
            savedSpam.await()?.let { ServerResponse.ok().bodyValueAndAwait(it) }
        } ?: ServerResponse.notFound().buildAndAwait()
    }

    suspend fun deleteSpam(request: ServerRequest): ServerResponse {

        val spam: Deferred<Spam?> = GlobalScope.async {
            spamRepository.findById(request.pathVariable("id")).awaitFirstOrNull()
        }
        return spam.await()?.let {
            val deletedSpam: Deferred<Void?> = GlobalScope.async {
                spamRepository.delete(it).awaitFirstOrNull()
            }
            deletedSpam.await().let { ServerResponse.ok().buildAndAwait() }
        } ?: ServerResponse.notFound().buildAndAwait()

    }

}