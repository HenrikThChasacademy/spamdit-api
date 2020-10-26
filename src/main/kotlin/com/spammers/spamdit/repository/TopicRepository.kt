package com.spammers.spamdit.repository

import com.spammers.spamdit.model.Topic
import com.spammers.spamdit.model.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface TopicRepository: ReactiveMongoRepository<Topic, String> {

    fun findFirstByText(text: String): Mono<Topic>
}