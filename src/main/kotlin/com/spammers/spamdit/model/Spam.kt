package com.spammers.spamdit.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Spam(
        @Id val id: String? = null,
        val user: User,
        @DBRef(lazy = true) val topic: Topic,
        val text: String,
        @DBRef(lazy = true) val comments: List<Comment>,
        val date: LocalDateTime)