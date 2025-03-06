package io.toasting.domain.member.application

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.toasting.creator.member.SocialLoginCreator
import io.toasting.domain.member.application.input.LoginGoogleInput
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.entity.SocialLogin
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.member.repository.RefreshTokenRepository
import io.toasting.domain.member.repository.SocialLoginRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Transactional
class LoginMemberServiceTest private constructor() : BehaviorSpec() {
    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var loginMemberService: LoginMemberService

    @Autowired
    private lateinit var socialLoginRepository: SocialLoginRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    init {
        Given("소셜 멤버가 주어졌을 때,") {
            val member = Member.defaultMember("test", "test@naver.com")
            val newSocialMember =
                SocialLoginCreator.defaultGoogleLogin(
                    externalId = "test1234",
                    member = member,
                )
            When("구글 로그인을 새로운 사용자가 시도하면") {
                val result = loginMemberService.loginGoogle(newSocialMember.toInput())
                Then("결과는 null을 반환해야 한다") {
                    result shouldBe null
                }
                Then("멤버가 새롭게 저장되어야 한다") {
                    val socialMember =
                        socialLoginRepository.findBySocialTypeAndExternalId(
                            newSocialMember.socialType,
                            newSocialMember.externalId,
                        )
                    socialMember!!.member.email shouldBe member.email
                    socialMember.member.nickname shouldBe member.nickname
                }
            }
            When("구글 로그인을 기존 사용자가 시도하면") {
                val result = loginMemberService.loginGoogle(newSocialMember.toInput())
                Then("결과는 accessToken과 refreshToken이 반환되어야 한다") {
                    result shouldNotBe null
                }
                Then("리프레시 토큰이 저장되어야 한다") {
                    refreshTokenRepository.findAll().size shouldBe 1
                }
            }
        }
    }

    private fun SocialLogin.toInput() =
        LoginGoogleInput(
            socialType = socialType,
            externalId = externalId,
            email = member.email,
            username = member.nickname,
        )
}
