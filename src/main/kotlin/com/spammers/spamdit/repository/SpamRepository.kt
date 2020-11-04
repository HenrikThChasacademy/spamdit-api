package com.spammers.spamdit.repository;

import com.spammers.spamdit.model.Spam
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface SpamRepository : ReactiveMongoRepository<Spam, String> {
    fun findAllByTopicId(topicId: String): Flux<Spam>
    fun findAllByUserId(userId: String): Flux<Spam>
}