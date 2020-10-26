package com.spammers.spamdit.handler

import com.spammers.spamdit.model.Topic
import com.spammers.spamdit.repository.TopicRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class TopicHandler (@Autowired var topicRepository: TopicRepository) {

    suspend fun saveTopic(request: ServerRequest): ServerResponse {
        System.out.println(request)
        val topic: Deferred<Topic?> = GlobalScope.async {
            topicRepository.save(request.awaitBody<Topic>()).awaitFirstOrNull()
        }

        return topic.await()?.let { ServerResponse.ok().bodyValueAndAwait(it) } ?:
            ServerResponse.notFound().buildAndAwait()
    }

    suspend fun getTopics(request: ServerRequest): ServerResponse =
            ServerResponse.ok().json().bodyAndAwait(topicRepository.findAll().asFlow())

    suspend fun getTopicById(request: ServerRequest): ServerResponse {
        val topic: Deferred<Topic?> = GlobalScope.async {
            topicRepository.findById(request.pathVariable("id")).awaitFirstOrNull()
        }

        return topic.await()?.let { ServerResponse.ok().bodyValueAndAwait(it) } ?:
        ServerResponse.notFound().buildAndAwait()
    }

    suspend fun getTopicByText(request: ServerRequest): ServerResponse {
        val topic: Deferred<Topic?> = GlobalScope.async {
            topicRepository.findFirstByText(request.pathVariable("name")).awaitFirstOrNull()
        }

        return topic.await()?.let { ServerResponse.ok().bodyValueAndAwait(it) } ?:
        ServerResponse.notFound().buildAndAwait()
    }
}