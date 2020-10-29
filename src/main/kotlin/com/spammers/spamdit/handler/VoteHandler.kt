package com.spammers.spamdit.handler

import com.spammers.spamdit.model.Comment
import com.spammers.spamdit.model.Spam
import com.spammers.spamdit.model.User
import com.spammers.spamdit.model.Vote
import com.spammers.spamdit.repository.CommentRepository
import com.spammers.spamdit.repository.SpamRepository
import com.spammers.spamdit.repository.UserRepository
import com.spammers.spamdit.repository.VoteRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import java.net.URI

@Component
class VoteHandler(@Autowired var voteRepository: VoteRepository,
                  @Autowired var userRepository: UserRepository,
                  @Autowired var commentRepository: CommentRepository,
                  @Autowired var spamRepository: SpamRepository) {

    @FlowPreview
    suspend fun getAllVotes(request: ServerRequest): ServerResponse =
            ServerResponse.ok().json().bodyAndAwait(voteRepository.findAll().asFlow())

    suspend fun getVoteById(request: ServerRequest): ServerResponse {
        val vote: Deferred<Vote?> = GlobalScope.async {
            voteRepository.findById(request.pathVariable("id")).awaitFirstOrNull()
        }

        return vote.await()?.let { ServerResponse.ok().bodyValueAndAwait(vote) } ?:
            ServerResponse.notFound().buildAndAwait()

    }
    suspend fun getVoteForSpamByUser(request: ServerRequest): ServerResponse {
        val vote: Deferred<Vote?> = GlobalScope.async {
            voteRepository.findBySpamIdAndUserId(request.pathVariable("spamId"),
                    request.pathVariable("userId")).awaitFirstOrNull()
        }

        return vote.await()?.let { ServerResponse.ok().bodyValueAndAwait(it) } ?:
            ServerResponse.notFound().buildAndAwait()
    }

    suspend fun getAllVotesForComment(request: ServerRequest): ServerResponse =
            ServerResponse.ok().json()
                    .bodyAndAwait(
                            voteRepository.findAllByCommentId(
                                    request.pathVariable("commentId")).asFlow())

    suspend fun getAllVotesForSpam(request: ServerRequest): ServerResponse =
            ServerResponse.ok().json()
                    .bodyAndAwait(
                            voteRepository.findAllBySpamId(
                                    request.pathVariable("spamId")).asFlow())

    suspend fun saveVote(request: ServerRequest): ServerResponse {
        println(request)

        val voteRequest: Vote = request.awaitBody()

        return voteRequest.spamId?.let { id ->
                val voteExistsForUser: Deferred<Vote?> = GlobalScope.async {
                    voteRepository.findBySpamIdAndUserId(id, voteRequest.userId).awaitFirstOrNull()
                }
                voteExistsForUser.await()?.let {
                    ServerResponse.seeOther(URI.create("/vote/${it.id}")).buildAndAwait()
                } ?: updateVoteForSpam(voteRequest, id)
            } ?: voteRequest.commentId?.let { id ->
                val voteExistsForUser: Deferred<Vote?> = GlobalScope.async {
                    voteRepository.findByCommentIdAndUserId(id, voteRequest.userId).awaitFirstOrNull()
                }
                voteExistsForUser.await()?.let {
                    ServerResponse.seeOther(URI.create("/vote/${it.id}")).buildAndAwait()
                } ?: updateVoteForComment(voteRequest, id)
            } ?: ServerResponse.notFound().buildAndAwait()
    }

    suspend fun deleteVote(request: ServerRequest): ServerResponse {
        println(request)
        val voteToDelete: Deferred<Vote?> = GlobalScope.async {
            voteRepository.findById(request.pathVariable("id")).awaitFirstOrNull()
        }
        return voteToDelete.await()?.let {
            val deletedVote: Deferred<Void?> = GlobalScope.async {
                voteRepository.delete(it).awaitFirstOrNull()
            }
            deletedVote.await().let { ServerResponse.ok().buildAndAwait() }
        } ?: ServerResponse.notFound().buildAndAwait()
    }

    suspend fun saveVoteByUser(request: ServerRequest): ServerResponse {
        println(request)
        val voteRequest: Vote = request.awaitBody()

        val vote: Deferred<Vote?> = GlobalScope.async {
            voteRepository.findById(request.pathVariable("id")).awaitFirstOrNull()
        }
        return saveVoteIfUserExistsAndCommentOrSpamIdSet(vote, voteRequest)
    }

    private suspend fun saveVoteIfUserExistsAndCommentOrSpamIdSet(vote: Deferred<Vote?>, voteRequest: Vote): ServerResponse {
        return vote.await()?.let {
                val user: Deferred<User?> = GlobalScope.async {
                    userRepository.findById(voteRequest.userId).awaitFirstOrNull()
                }
                user.await()?.let {
                    voteRequest.spamId?.let { id ->
                        updateVoteForSpam(voteRequest, id)
                    } ?: voteRequest.commentId?.let { id ->
                        updateVoteForComment(voteRequest, id)
                    }
                }
            } ?: ServerResponse.notFound().buildAndAwait()
    }

    private suspend fun updateVoteForSpam(vote: Vote, id: String): ServerResponse {
        val spam: Deferred<Spam?> = GlobalScope.async {
            spamRepository.findById(id).awaitFirstOrNull()
        }
        return spam.await()?.let {
            updateVote(vote) } ?:
            ServerResponse.notFound().buildAndAwait()
    }

    private suspend fun updateVoteForComment(vote: Vote, id: String): ServerResponse {
        val comment: Deferred<Comment?> = GlobalScope.async {
            commentRepository.findById(id).awaitFirstOrNull()
        }
        return comment.await()?.let {
            updateVote(vote) } ?:
            ServerResponse.notFound().buildAndAwait()
    }

    private suspend fun updateVote(vote: Vote): ServerResponse {
        val voteToSave: Deferred<Vote?> = GlobalScope.async {
            voteRepository.save(vote).awaitFirstOrNull()
        }
        return voteToSave.await()?.let { ServerResponse.ok().bodyValueAndAwait(it) } ?:
                ServerResponse.notFound().buildAndAwait()
    }
}