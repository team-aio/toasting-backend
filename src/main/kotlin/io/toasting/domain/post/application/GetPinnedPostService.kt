package io.toasting.domain.post.application

import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.exception.MemberExceptionHandler
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.post.application.out.GetPinnedPostsOutput
import io.toasting.domain.post.repository.BookmarkRepository
import io.toasting.domain.post.repository.PinnedPostRepository
import io.toasting.domain.post.repository.PostRepository
import org.springframework.stereotype.Service

@Service
class GetPinnedPostService(
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository,
    private val pinnedPostRepository: PinnedPostRepository,
    private val bookmarkRepository: BookmarkRepository,
    ) {

    fun getPinnedPostListWithLogin(memberId: Long, writerId: Long): List<GetPinnedPostsOutput> {
        val pinnedPostList = pinnedPostRepository.findByMemberId(writerId)
        val postIdList = pinnedPostList.mapNotNull { it.post.id }
        val postList = postRepository.findAllById(postIdList)

        val bookmarkedPostList = bookmarkRepository.findByPostInAndMemberId(postList, memberId)
        val bookmarkedPostIdSet = bookmarkedPostList
            .mapNotNull { it.post.id }
            .toSet()

        val writer = memberRepository.findById(writerId)
            .orElseThrow { MemberExceptionHandler.MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND) }
        return postList.map { post ->
            val isBookmarked = bookmarkedPostIdSet.contains(post.id)
            GetPinnedPostsOutput.of(post, writer, isBookmarked)
        }
    }

    fun getPinnedPostListWithoutLogin(writerId: Long): List<GetPinnedPostsOutput> {
        val pinnedPostList = pinnedPostRepository.findByMemberId(writerId)
        val postIdList = pinnedPostList.mapNotNull { it.post.id }
        val postList = postRepository.findAllById(postIdList)

        val writer = memberRepository.findById(writerId)
            .orElseThrow { MemberExceptionHandler.MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND) }
        return postList.map { post ->
            GetPinnedPostsOutput.of(post, writer, false)
        }
    }
}