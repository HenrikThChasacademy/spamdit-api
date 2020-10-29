package com.spammers.spamdit.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Spam(
        @Id val id: String? = null,
        val userId: String,
        var topicId: String,
        val text: String,
        val commentIds: List<String> = listOf(),
        val dateCreated: LocalDateTime,
        val dateEdited: LocalDateTime? = null)