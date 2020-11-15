package com.spammers.spamdit.repository

import com.spammers.spamdit.model.User
import com.spammers.spamdit.model.UserSettings
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface UserSettingsRepository: ReactiveMongoRepository<UserSettings, String>{

}