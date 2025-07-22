package io.toasting.domain.post.application

import io.toasting.api.PageResponse
import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.exception.MemberExceptionHandler
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.post.application.out.GetPostDetailOutput
import io.toasting.domain.post.application.out.SearchPostsOutput
import io.toasting.domain.post.entity.Post
import io.toasting.domain.post.exception.PostExceptionHandler
import io.toasting.domain.post.repository.BookmarkRepository
import io.toasting.domain.post.repository.PostRepository
import io.toasting.domain.post.vo.SourceType
import io.toasting.global.external.crawler.PostCrawler
import org.jsoup.Jsoup
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale

@Service
class PostService(
    private val postRepository: PostRepository,
    private val memberRepository: MemberRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val postCrawler: PostCrawler,
) {
    fun searchPost(
        memberId: Long?,
        keyword: String?,
        pageable: Pageable
    ): PageResponse<SearchPostsOutput> {
        val postPage = postRepository.searchByKeyword(keyword, pageable)
        val postList = postPage.content

        val memberMapById = getMemberMapById(postList)

        val bookmarkedPostIdSet = getBookmarkedPostIdSet(memberId, postList)

        val outputList = postList.map { post ->
            val member = memberMapById[post.memberId]
                ?: throw MemberExceptionHandler.MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND)
            val isBookmarked = bookmarkedPostIdSet.contains(post.id)
            SearchPostsOutput.of(post, member, isBookmarked)
        }

        return PageResponse.of(
            outputList,
            postPage.totalElements,
            postPage.totalPages
        )
    }

    private fun getMemberMapById(postList: List<Post>): Map<Long, Member> {
        val memberIdList = postList.map { it.memberId }
        val memberList = memberRepository.findAllById(memberIdList)
        return memberList.associateBy { it.id!! }
    }

    private fun getBookmarkedPostIdSet(memberId: Long?, postList: List<Post>): Set<Long> {
        if (memberId == null) return emptySet()
        return bookmarkRepository.findByPostInAndMemberId(postList, memberId)
            .mapNotNull { it.post.id }
            .toSet()
    }

    fun getPostDetail(postId: Long): GetPostDetailOutput {
        val post = postRepository.findById(postId)
            .orElseThrow { PostExceptionHandler.PostNotFoundException(ErrorStatus.POST_NOT_FOUND) }
        val member = memberRepository.findById(post.memberId)
            .orElseThrow { MemberExceptionHandler.MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND) }

        return GetPostDetailOutput.of(post, member)
    }


}