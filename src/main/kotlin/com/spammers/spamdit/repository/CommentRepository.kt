package com.spammers.spamdit.repository

import com.spammers.spamdit.model.Comment
import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface CommentRepository: ReactiveMongoRepository<Comment, String>