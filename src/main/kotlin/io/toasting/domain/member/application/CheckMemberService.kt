package io.toasting.domain.member.application

import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.exception.MemberExceptionHandler.MemberNameDuplicationException
import io.toasting.domain.member.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class CheckMemberService(
    private val memberRepository: MemberRepository,
) {
    fun checkMemberNicknameIsDuplicated(nickname: String) {
        if (memberRepository.existsByNickname(nickname)) {
            throw MemberNameDuplicationException(ErrorStatus.MEMBER_NAME_DUPLICATION)
        }
    }
}
