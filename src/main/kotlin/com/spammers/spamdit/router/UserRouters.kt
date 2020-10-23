package com.spammers.spamdit.router

import com.spammers.spamdit.handler.UserHandler
import kotlinx.coroutines.FlowPreview
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class UserRouters {
    @FlowPreview
    @Bean
    fun userRoutes(userHandler: UserHandler) = coRouter {
        GET("/user", userHandler::getAllUsers)
        GET("/user/{id}", userHandler::getUserById)
        GET("/user/name/{name}", userHandler::getUserByName)
        POST("/user", userHandler::createUniqueUser)
        PUT("/user/{id}", userHandler::saveUser)
    }
    
}