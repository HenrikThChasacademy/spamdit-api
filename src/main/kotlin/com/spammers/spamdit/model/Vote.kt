package com.spammers.spamdit.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Vote(@Id val id: String? = null,
                val upVote: Boolean,
                val userId: String,
                @Indexed val spamId: String? = null,
                @Indexed val commentId: String? = null,
                val votedDate: LocalDateTime)