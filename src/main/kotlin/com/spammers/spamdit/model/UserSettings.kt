package com.spammers.spamdit.model

import org.springframework.data.annotation.Id

data class UserSettings(@Id val id: String? = null, val textColor: String, val backgroundColor: String, val avatarPath: String)