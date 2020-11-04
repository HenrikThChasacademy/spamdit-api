package com.spammers.spamdit.router

import com.spammers.spamdit.handler.SpamHandler
import kotlinx.coroutines.FlowPreview
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class SpamRouters {

    @FlowPreview
    @Bean
    fun spamRoutes(spamHandler: SpamHandler) = coRouter {
        GET("/spam", spamHandler::getSpam)
        GET("/spam/{id}", spamHandler::getSpamById)
        GET("/spam/topic/{topicId}", spamHandler::getSpamByTopic)
        GET("/spam/user/{userId}", spamHandler::getSpamByUser)
        POST("/spam", spamHandler::saveSpam)
        PUT("/spam/{id}", spamHandler::updateSpam)
        DELETE("/car/{id}", spamHandler::deleteSpam)
    }

}