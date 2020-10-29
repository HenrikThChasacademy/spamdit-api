package com.spammers.spamdit.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Comment(@Id val id: String? = null,
                   @Indexed val parentId: String,
                   val text: String,
                   val userId: String,
                   val commentIds: List<String> = listOf(),
                   val dateCreated: LocalDateTime,
                   val dateEdited: LocalDateTime? = null)