package io.toasting.domain.member.application

import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.application.input.SignUpSocialLoginInput
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.entity.SocialLogin
import io.toasting.domain.member.exception.MemberExceptionHandler
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.member.repository.SocialLoginRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SignUpMemberService(
    private val socialLoginRepository: SocialLoginRepository,
    private val memberRepository: MemberRepository,
) {
    /*
     * 소셜 로그인으로 회원가입할 때 (추후 확장성을 고려했습니다.)
     * 구글 소셜 회원 가입일
     */
    @Transactional
    fun signUpBySocialLogin(signUpSocialLoginInput: SignUpSocialLoginInput): Long {
        validate(signUpSocialLoginInput)
        return save(signUpSocialLoginInput)
    }

    private fun validate(signUpSocialLoginInput: SignUpSocialLoginInput) {
        if (socialLoginRepository.existsBySocialTypeAndExternalId(
                signUpSocialLoginInput.socialType,
                signUpSocialLoginInput.externalId,
            )
        ) {
            throw MemberExceptionHandler.SocialMemberAlreadySignUpException(ErrorStatus.ALERADY_SIGN_UP_MEMBER)
        }
    }

    private fun save(signUpSocialLoginInput: SignUpSocialLoginInput): Long {
        val uuid = generateUUID()
        val savedMember = Member.defaultMember(
            nickname = signUpSocialLoginInput.nickname,
            email = signUpSocialLoginInput.email,
            uuid = uuid,
        )
        socialLoginRepository.save(
            SocialLogin(
                socialType = signUpSocialLoginInput.socialType,
                externalId = signUpSocialLoginInput.externalId,
                member = savedMember,
            ),
        )
        return savedMember.id ?: throw IllegalStateException("회원 가입에 실패했습니다.")
    }

    private fun generateUUID(): UUID {
        var uuid: UUID
        do {
            uuid = UUID.randomUUID()
        } while (memberRepository.existsByUuid(uuid))
        return uuid
    }
}
