package io.toasting.domain.member.application

import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.application.output.GetLinkedBlogStatusOutput
import io.toasting.domain.member.exception.MemberExceptionHandler
import io.toasting.domain.member.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberBlogService(
    private val memberRepository: MemberRepository,
) {
    fun getLinkedBlogStatus(memberId: Long): GetLinkedBlogStatusOutput {
        val member = memberRepository.findById(memberId)
            .orElseThrow({ MemberExceptionHandler.MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND) })
        return GetLinkedBlogStatusOutput.from(member)
    }
}