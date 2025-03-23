package io.toasting.domain.member.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.toasting.creator.member.SignUpSocialLoginInputCreator
import io.toasting.domain.member.exception.MemberExceptionHandler
import io.toasting.domain.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Transactional
class SignUpMemberServiceTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var signUpMemberService: SignUpMemberService

    @Autowired
    private lateinit var memberRepository: MemberRepository

    init {
        Given("회원 가입 정보가 주어졌을 때,") {
            val googleMember1 =
                SignUpSocialLoginInputCreator.googleDefault("tjdvy963@naver.com", "tjdvy963", "tjdvy963", "123456")
            val googleMember3 =
                SignUpSocialLoginInputCreator.googleDefault("tjdvy963@naver.com", "tjdvy963", "howudong", "123456")

            When("회원 가입을 시도하면") {
                val savedMember1 = signUpMemberService.signUpBySocialLogin(googleMember1)
                Then("회원이 저장되어야 한다") {
                    val member = memberRepository.findById(savedMember1).get()
                    member.nickname shouldBe "tjdvy963"
                    member.email shouldBe "tjdvy963@naver.com"
                }
            }
            When("이미 가입된 회원이 회원가입을 시도하면") {
                Then("SocialMemberDuplicationException 이 발생해야 한다") {
                    shouldThrow<MemberExceptionHandler.SocialMemberAlreadySignUpException> {
                        signUpMemberService.signUpBySocialLogin(googleMember3)
                    }
                }
            }
        }
    }
}
