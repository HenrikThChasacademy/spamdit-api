package com.spammers.spamdit.repository

import com.spammers.spamdit.model.Vote
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface VoteRepository: ReactiveMongoRepository<Vote, String>{
    fun findBySpamIdAndUserId(spamId: String, userId: String): Mono<Vote>
    fun findByCommentIdAndUserId(commentId: String, userId: String): Mono<Vote>
    fun findAllByCommentId(commentId: String): Flux<Vote>
    fun findAllBySpamId(spamId: String): Flux<Vote>
}