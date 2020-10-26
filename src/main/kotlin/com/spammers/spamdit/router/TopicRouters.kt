package com.spammers.spamdit.router

import com.spammers.spamdit.handler.TopicHandler
import kotlinx.coroutines.FlowPreview
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class TopicRouters {

    @FlowPreview
    @Bean
    fun topicRoutes(topicHandler: TopicHandler) = coRouter {
        GET("/topic", topicHandler::getTopics)
        GET("/topic/{id}", topicHandler::getTopicById)
        GET("/topic/name/{name}", topicHandler::getTopicByText)
        POST("/topic", topicHandler::saveTopic)
    }
}