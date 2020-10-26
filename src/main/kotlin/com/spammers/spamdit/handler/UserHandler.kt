package com.spammers.spamdit.handler

import com.spammers.spamdit.model.User
import com.spammers.spamdit.repository.TopicRepository
import com.spammers.spamdit.repository.UserRepository
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
class UserHandler (@Autowired var userRepository: UserRepository){


    @FlowPreview
    suspend fun getAllUsers(request: ServerRequest): ServerResponse =
        ServerResponse.ok().json().bodyAndAwait(userRepository.findAll().asFlow())

    suspend fun getUserById(request: ServerRequest): ServerResponse {
        val user: Deferred<User?> = GlobalScope.async {
            userRepository.findById(request.pathVariable("id")).awaitFirstOrNull()
        }

        return user.await()?.let { ServerResponse.ok().bodyValueAndAwait(it) } ?: ServerResponse.notFound().buildAndAwait()
    }

    suspend fun getUserByName(request: ServerRequest): ServerResponse {
        val user: Deferred<User?> = GlobalScope.async {
            userRepository.findFirstByName(request.pathVariable("name")).awaitFirstOrNull()
        }

        return user.await()?.let { ServerResponse.ok().bodyValueAndAwait(it) } ?: ServerResponse.notFound().buildAndAwait()
    }

    suspend fun saveUser(request: ServerRequest): ServerResponse {
        val user: Deferred<User?> = GlobalScope.async {
            userRepository.findById(request.pathVariable("id")).awaitFirstOrNull()
        }

        return user.await()?.let {
            val savedUser: Deferred<User?> = GlobalScope.async {
                userRepository.save(request.awaitBody<User>()).awaitFirstOrNull()
            }
            savedUser.await()?.let { ServerResponse.ok().bodyValueAndAwait(it) }
        } ?: ServerResponse.notFound().buildAndAwait()
    }

    suspend fun createUniqueUser(request: ServerRequest): ServerResponse {
        lateinit var userInRequest: User
        val user: Deferred<User?> = GlobalScope.async {
            userInRequest = request.awaitBody();
            userRepository.findFirstByName(userInRequest.name).awaitFirstOrNull()
        }

        return user.await()?.let { ServerResponse.ok().bodyValueAndAwait(it) } ?: createUser(userInRequest)
    }

    private suspend fun createUser(user: User): ServerResponse {
        val newUser: Deferred<User?> = GlobalScope.async {
            userRepository.save(user).awaitFirstOrNull()
        }
        return newUser.await()?.let { ServerResponse.ok().bodyValueAndAwait(it) }
                ?: ServerResponse.badRequest().buildAndAwait()
    }
}