import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberSocialLoginService(
    private val memberRepository: MemberRepository,
    private val socialLoginRepository: SocialLoginRepository,
) {
    private val log = KotlinLogging.logger {}

    @Transactional
    fun upsertMemberBy(
        socialCode: String,
        externalId: String,
        accessToken: String,
        nickname: String,
        email: String,
    ): Long {
        val socialLogin = socialLoginRepository.findByExternalIdAndAccessToken(externalId, accessToken)

        if (socialLogin == null) {
            val newMember = createNewMember(nickname, email)
            createNewSocialLogin(newMember, socialCode, externalId, accessToken)
            return newMember.id!!
        }

        return updateMember(socialLogin, nickname, email).id!!
    }

    private fun createNewMember(
        nickname: String,
        email: String,
    ): Member {
        val newMember = Member.defaultMember(nickname, email)
        return memberRepository.save(newMember)
    }

    private fun createNewSocialLogin(
        member: Member,
        socialCode: String,
        externalId: String,
        accessToken: String,
    ) {
        val socialLogin = SocialLogin(socialCode, externalId, accessToken, member)
        socialLoginRepository.save(socialLogin)
    }

    private fun updateMember(
        socialLogin: SocialLogin,
        nickname: String,
        email: String,
    ): Member {
        val updatedMember = socialLogin.member.updateWith(nickname, email)
        return memberRepository.save(updatedMember)
    }
}
