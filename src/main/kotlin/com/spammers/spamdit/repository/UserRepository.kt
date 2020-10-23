package com.spammers.spamdit.repository

import com.spammers.spamdit.model.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface UserRepository: ReactiveMongoRepository<User, String>{

    fun findFirstByName(name: String): Mono<User>
}