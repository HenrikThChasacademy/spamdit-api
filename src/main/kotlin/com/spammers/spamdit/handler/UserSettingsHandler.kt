package com.spammers.spamdit.handler

import com.spammers.spamdit.model.UserSettings
import com.spammers.spamdit.repository.UserSettingsRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class UserSettingsHandler (@Autowired var userSettingsRepository: UserSettingsRepository){

    suspend fun getUserSettingsById(request: ServerRequest): ServerResponse {
        val user: Deferred<UserSettings?> = GlobalScope.async {
            userSettingsRepository.findById(request.pathVariable("id")).awaitFirstOrNull()
        }

        return user.await()?.let { ServerResponse.ok().bodyValueAndAwait(it) } ?: ServerResponse.notFound().buildAndAwait()
    }

    suspend fun saveUserSettings(request: ServerRequest): ServerResponse {
        val savedUserSettings: Deferred<UserSettings?> = GlobalScope.async {
            userSettingsRepository.save(request.awaitBody<UserSettings>()).awaitFirstOrNull()
        }

        return savedUserSettings.await()?.let { ServerResponse.ok().bodyValueAndAwait(it) }
         ?: ServerResponse.notFound().buildAndAwait()
    }

}