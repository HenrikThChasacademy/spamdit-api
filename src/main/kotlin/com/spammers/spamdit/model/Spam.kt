package com.spammers.spamdit.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Spam(
        @Id val id: String? = null,
        val user: String,
        val topic: String,
        val text: String,
        val date: LocalDateTime)