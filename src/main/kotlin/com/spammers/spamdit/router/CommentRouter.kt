package com.spammers.spamdit.router

import com.spammers.spamdit.handler.CommentHandler
import kotlinx.coroutines.FlowPreview
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class CommentRouter {

    @FlowPreview
    @Bean
    fun commentRoutes(commentHandler: CommentHandler) = coRouter {
        GET("/comment", commentHandler::getComments)
        GET("/comment/parent/{parentId}", commentHandler::getCommentsForParent)
        POST("/comment", commentHandler::saveComment)
        PUT("/comment/{id}", commentHandler::updateComment)
    }
}