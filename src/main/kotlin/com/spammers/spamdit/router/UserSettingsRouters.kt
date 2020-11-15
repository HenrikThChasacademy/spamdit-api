package com.spammers.spamdit.router

import com.spammers.spamdit.handler.UserHandler
import com.spammers.spamdit.handler.UserSettingsHandler
import kotlinx.coroutines.FlowPreview
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class UserSettingsRouters {
    @FlowPreview
    @Bean
    fun userSettingsRoutes(userSettingsHandler: UserSettingsHandler) = coRouter {
        GET("/userSettings/{id}", userSettingsHandler::getUserSettingsById)
        POST("/userSettings", userSettingsHandler::saveUserSettings)
    }
    
}