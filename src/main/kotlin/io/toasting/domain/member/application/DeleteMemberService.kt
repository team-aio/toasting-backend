package io.toasting.domain.member.application

import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.exception.MemberExceptionHandler.MemberNotFoundException
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.member.repository.SocialLoginRepository
import io.toasting.domain.post.repository.BookmarkRepository
import io.toasting.domain.post.repository.LikeRepository
import io.toasting.domain.post.repository.PostRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class DeleteMemberService(
    private val memberRepository: MemberRepository,
    private val socialLoginRepository: SocialLoginRepository,
    private val postRepository: PostRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val likeRepository: LikeRepository
) {
    @Transactional
    fun deleteMember(memberId: Long) {
        val member = findMember(memberId)
        delete(member)
    }

    private fun findMember(memberId: Long): Member = (memberRepository.findById(memberId).getOrNull()
        ?: throw MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND))

    private fun delete(member: Member) {
        val memberId = member.id ?: throw MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND)
        
        socialLoginRepository.deleteByMember(member)
        postRepository.deleteAllByMemberId(memberId)
        bookmarkRepository.deleteAllByMemberId(memberId)
        likeRepository.deleteAllByMemberId(memberId)
    }
}