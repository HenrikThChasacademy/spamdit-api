package com.spammers.spamdit.handler

import com.spammers.spamdit.model.Comment
import com.spammers.spamdit.model.Spam
import com.spammers.spamdit.repository.CommentRepository
import com.spammers.spamdit.repository.SpamRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class CommentHandler (@Autowired val commentRepository: CommentRepository,
                      @Autowired val spamRepository: SpamRepository) {

    @FlowPreview
    suspend fun getComments(request: ServerRequest): ServerResponse =
            ServerResponse.ok().json().bodyAndAwait(commentRepository.findAll().asFlow())

    suspend fun getCommentsForParent(request: ServerRequest): ServerResponse =
            ServerResponse.ok().json().bodyAndAwait(
                    commentRepository.findAllByParentId(request.pathVariable("parentId")).asFlow())

    suspend fun saveComment(request: ServerRequest): ServerResponse {
        val comment: Comment = request.awaitBody()
        val savedComment: Deferred<Comment?> = GlobalScope.async {
            commentRepository.save(comment).awaitFirstOrNull()
        }

        val parentSpam: Deferred<Spam?> = GlobalScope.async {
            spamRepository.findById(comment.parentId).awaitFirstOrNull()
        }

        val parentComment: Deferred<Comment?> = GlobalScope.async {
            commentRepository.findById(comment.parentId).awaitFirstOrNull()
        }

        return savedComment.await()?.let {commentWithId ->
            parentSpam.await()?.let {
                val spamToSave = createUpdatedSpam(it, commentWithId.id)
                println("Adding id to parent spam $it with id ${commentWithId.id}")
                spamRepository.save(spamToSave).awaitFirstOrNull()
                ServerResponse.ok().bodyValueAndAwait(commentWithId)
            } ?: updateParentCommentWithCommentId(commentWithId, parentComment)
        } ?: ServerResponse.notFound().buildAndAwait()
    }

    fun createUpdatedSpam(spam: Spam, id: String?): Spam {
        val commentIdList = spam.commentIds.toMutableList()
        commentIdList.add(id!!)
        return Spam(spam.id, spam.userId, spam.topicId, spam.text, commentIdList, spam.dateCreated, spam.dateEdited)
    }

    suspend fun updateParentCommentWithCommentId(comment: Comment, parentComment: Deferred<Comment?>): ServerResponse {
        return parentComment.await()?.let {
            val commentToSave = createUpdatedComment(it, comment.id)
            println("Adding id to parent comment $it with id ${comment.id}")
            commentRepository.save(commentToSave).awaitFirstOrNull()
            ServerResponse.ok().bodyValueAndAwait(comment)
        } ?: ServerResponse.notFound().buildAndAwait()
    }

    fun createUpdatedComment(comment: Comment, id: String?): Comment {
        val commentIdList = comment.commentIds.toMutableList()
        commentIdList.add(id!!)
        return Comment(comment.id, comment.parentId, comment.text, comment.userId, commentIdList, comment.dateCreated, comment.dateEdited)
    }

    suspend fun updateComment(request: ServerRequest): ServerResponse {
        val comment: Deferred<Comment?> = GlobalScope.async {
            commentRepository.findById(request.pathVariable("id")).awaitFirstOrNull()
        }

        return comment.await()?.let {
            val savedComment: Deferred<Comment?> = GlobalScope.async {
                commentRepository.save(request.awaitBody<Comment>()).awaitFirstOrNull()
            }
            savedComment.await()?.let { ServerResponse.ok().bodyValueAndAwait(it) }
        } ?: ServerResponse.notFound().buildAndAwait()
    }

}