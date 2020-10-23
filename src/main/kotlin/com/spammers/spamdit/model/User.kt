package com.spammers.spamdit.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(@Id val id: String? = null,
                @Indexed val name: String,
                @DBRef(lazy = true) val spams: List<Spam>? = null,
                @DBRef(lazy = true) val comments: List<Comment>? = null) {
}