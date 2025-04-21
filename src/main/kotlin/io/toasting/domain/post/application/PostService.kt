package io.toasting.domain.post.application

import io.toasting.api.PageResponse
import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.member.exception.MemberExceptionHandler
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.post.application.out.GetPostDetailOutput
import io.toasting.domain.post.application.out.SearchPostsOutput
import io.toasting.domain.post.entity.Post
import io.toasting.domain.post.exception.PostExceptionHandler
import io.toasting.domain.post.repository.BookmarkRepository
import io.toasting.domain.post.repository.PostRepository
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
        memberDetails: MemberDetails,
        keyword: String?,
        pageable: Pageable
    ): PageResponse<SearchPostsOutput> {
        val postPage = postRepository.searchByKeyword(keyword, pageable)
        val postList = postPage.content

        val memberIdList = postList.map { it.memberId }
        val memberList = memberRepository.findAllById(memberIdList)
        val memberMapById = memberList.associateBy { it.id!! }

        val outputList = mutableListOf<SearchPostsOutput>()
        val bookmarkList = bookmarkRepository.findByPostInAndMemberId(postPage.content, memberDetails.username.toLong())
        val postIdSetByBookmarkSet = bookmarkList.map { it.post.id!! }.toSet()
        for (post in postList) {
            val memberId = post.memberId
            val member = memberMapById[memberId]
            if (member == null) {
                throw MemberExceptionHandler.MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND)
            }
            val isBookmarked = postIdSetByBookmarkSet.contains(post.id)
            val output = SearchPostsOutput.of(post, member, isBookmarked)
            outputList.add(output)
        }

        return PageResponse.of(
            outputList,
            postPage.totalElements,
            postPage.totalPages
        )
    }

    @Transactional(readOnly = false)
    fun linkBlog(memberDetails: MemberDetails, id: String, sourceType: String) {
        val memberId = memberDetails.username.toLong()
        var member = memberRepository.findById(memberId)
            .orElseThrow { MemberExceptionHandler.MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND) }
        validateAlreadyLinkedBlog(member, sourceType)
        member.registerBlog(sourceType, id)
        memberRepository.save(member)

        val crawledPostList = postCrawler.crawlPost(id, sourceType)

        val postList = mutableListOf<Post>()
        for (crawledPost in crawledPostList) {
            val html = crawledPost.content
            val text = Jsoup.parse(html).text()
            val shortContent = text.take(100)
            val postedAt = parseDateToLocalDateTime(crawledPost.posted_at)

            val post = Post(
                sourceType = sourceType,
                url = crawledPost.link,
                postedAt = postedAt,
                shortContent = shortContent,
                content = crawledPost.content,
                title = crawledPost.title,
                memberId = memberId
            )
            postList.add(post)
        }
        postRepository.saveAll(postList)
    }

    fun validateAlreadyLinkedBlog(member: Member, sourceType: String) {
        if ((sourceType == "tistory" && !member.tistoryId.isNullOrBlank()) ||
            (sourceType == "velog" && !member.velogId.isNullOrBlank())
        ) {
            throw PostExceptionHandler.AlreadyLinkedBlog(ErrorStatus.ALREADY_LINKED_BLOG)
        }
    }

    fun getPostDetail(postId: Long): GetPostDetailOutput {
        val post = postRepository.findById(postId)
            .orElseThrow { PostExceptionHandler.PostNotFoundException(ErrorStatus.POST_NOT_FOUND) }
        val member = memberRepository.findById(post.memberId)
            .orElseThrow { MemberExceptionHandler.MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND) }

        return GetPostDetailOutput.of(post, member)
    }

    fun parseDateToLocalDateTime(dateStr: String): LocalDateTime {
        val format = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
        val date = format.parse(dateStr)

        return date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }
}