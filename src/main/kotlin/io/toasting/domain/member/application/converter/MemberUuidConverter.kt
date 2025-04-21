package io.toasting.domain.member.application.converter

import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.exception.MemberExceptionHandler
import io.toasting.domain.member.repository.MemberRepository
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class MemberUuidConverter(
    private val memberRepository: MemberRepository
) {
    fun toMemberId(uuid: String): Long = memberRepository.findByUuid(UUID.fromString(uuid))?.id
        ?: throw MemberExceptionHandler.MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND)
}