package io.toasting.global.codec

import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.exception.MemberExceptionHandler.MemberNotFoundException
import io.toasting.domain.member.repository.MemberRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class MemberIdCodec(
    private val passwordEncoder: BCryptPasswordEncoder,
    private val memberRepository: MemberRepository
) {
    fun encode(memberId: Long): String = passwordEncoder.encode(memberId.toString())
    fun decode(memberIdHash: String): Long =
        memberRepository.findByMemberIdHash(memberIdHash)?.id
            ?: throw MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND)
}