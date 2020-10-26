package com.spammers.spamdit.repository

import com.spammers.spamdit.model.Comment
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface CommentRepository: ReactiveMongoRepository<Comment, String> {
    fun findAllByParentId(parentId: String): Flux<Comment>
}