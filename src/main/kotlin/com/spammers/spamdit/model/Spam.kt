package com.spammers.spamdit.model

import java.time.LocalDateTime

data class Spam(
        val id: String,
        val user: String,
        val topic: String,
        val text: String,
        val date: LocalDateTime
)