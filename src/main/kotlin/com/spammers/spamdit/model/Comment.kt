package com.spammers.spamdit.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Comment(@Id val id: String,
                val text: String,
                @DBRef(lazy = true) val comments: List<Comment>) {
}