package com.spammers.spamdit.router

import com.spammers.spamdit.handler.VoteHandler
import kotlinx.coroutines.FlowPreview
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class VoteRouter {
    @FlowPreview
    @Bean
    fun voteRoutes(voteHandler: VoteHandler) = coRouter {
        GET("/vote", voteHandler::getAllVotes)
        GET("/vote/{id}", voteHandler::getVoteById)
        GET("/vote/spam/{spamId}/user/{userId}", voteHandler::getVoteForSpamByUser)
        GET("/vote/spam/{spamId}", voteHandler::getAllVotesForSpam)
        GET("/vote/comment/{commentId}/user/{userId}", voteHandler::getVoteForCommentByUser)
        GET("/vote/comment/{commentId}", voteHandler::getAllVotesForComment)
        POST("/vote", voteHandler::saveVote)
        PUT("/vote/{id}", voteHandler::saveVoteByUser)
        DELETE("/vote/{id}", voteHandler::deleteVote)
    }

}