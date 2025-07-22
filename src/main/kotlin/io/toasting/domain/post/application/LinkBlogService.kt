package io.toasting.domain.post.application

import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.exception.MemberExceptionHandler
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.post.entity.Post
import io.toasting.domain.post.exception.PostExceptionHandler
import io.toasting.domain.post.repository.PostRepository
import io.toasting.domain.post.vo.SourceType
import io.toasting.global.external.crawler.PostCrawler
import org.jsoup.Jsoup
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Locale

@Service
class LinkBlogService(
    private val postCrawler: PostCrawler,
    private val memberRepository: MemberRepository,
    private val postRepository: PostRepository,
) {
    @Transactional(readOnly = false)
    fun linkBlog(memberId: Long, id: String, sourceType: SourceType) {
        val member = memberRepository.findById(memberId)
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

    @Transactional(readOnly = false)
    fun unlinkBlog(memberId: Long, sourceType: SourceType) {
        val member = memberRepository.findById(memberId)
            .orElseThrow { MemberExceptionHandler.MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND) }
        validateLinkedBlog(member, sourceType)
        member.unlinkBlog(sourceType)
        memberRepository.save(member)

        postRepository.deleteAllByMemberIdAndSourceType(memberId, sourceType)
    }

    private fun parseDateToLocalDateTime(dateStr: String): LocalDateTime {
        val format = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
        val date = format.parse(dateStr)

        return date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    private fun validateAlreadyLinkedBlog(member: Member, sourceType: SourceType) {
        if ((sourceType == SourceType.TISTORY && !member.tistoryId.isNullOrBlank()) ||
            (sourceType == SourceType.VELOG && !member.velogId.isNullOrBlank())) {
            throw PostExceptionHandler.AlreadyLinkedBlog(ErrorStatus.ALREADY_LINKED_BLOG)
        }
    }

    private fun validateLinkedBlog(member: Member, sourceType: SourceType) {
        if ((sourceType == SourceType.TISTORY && member.tistoryId.isNullOrBlank()) ||
            (sourceType == SourceType.VELOG && member.velogId.isNullOrBlank())) {
            throw PostExceptionHandler.NotLinkedBlog(ErrorStatus.NOT_LINKED_BLOG)
        }
    }
}