package io.toasting.domain.post.application

import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.post.entity.PinnedPost
import io.toasting.domain.post.exception.PostExceptionHandler
import io.toasting.domain.post.repository.PinnedPostRepository
import io.toasting.domain.post.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PinPostService(
    private val postRepository: PostRepository,
    private val pinnedPostRepository: PinnedPostRepository,
) {

    @Transactional
    fun pinPost(memberId: Long, postId: Long) {
        val post = postRepository.findById(postId)
            .orElseThrow { PostExceptionHandler.PostNotFoundException(ErrorStatus.POST_NOT_FOUND) }

        if (post.memberId != memberId) {
            throw PostExceptionHandler.NotWriterByPinException(ErrorStatus.NOT_WRITER_BY_PIN)
        }

        val existingPinnedPost = pinnedPostRepository.findByPostIdAndMemberId(postId, memberId)
            .orElse(null)
        if (existingPinnedPost != null) {
            throw PostExceptionHandler.AlreadyPinnedPostException(ErrorStatus.ALREADY_PINNED_POST)
        }

        val pinnedPost = PinnedPost(
            post = post,
            memberId = memberId
        )
        pinnedPostRepository.save(pinnedPost)
    }

    @Transactional
    fun unpinPost(memberId: Long, postId: Long) {
        val pinnedPost = pinnedPostRepository.findByPostIdAndMemberId(postId, memberId)
            .orElseThrow { PostExceptionHandler.NotPinnedPostException(ErrorStatus.NOT_PINNED_POST) }
        
        pinnedPostRepository.delete(pinnedPost)
    }
}