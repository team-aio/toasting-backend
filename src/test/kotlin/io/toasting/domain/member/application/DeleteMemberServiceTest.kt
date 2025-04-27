package io.toasting.domain.member.application

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.toasting.creator.member.MemberCreator
import io.toasting.creator.member.SocialLoginCreator
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.member.repository.SocialLoginRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class DeleteMemberServiceTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var deleteMemberService: DeleteMemberService

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var socialLoginRepository: SocialLoginRepository

    init {
        Given("구글 로그인 멤버와 멤버가 저장되어 있을때,") {

            socialLoginRepository.save(
                SocialLoginCreator.defaultGoogleLogin(
                    "1234",
                    MemberCreator.defaultMember(null, "test", "test@test.com")
                )
            )
            When("회원 탈퇴 이벤트가 발행되면") {
                deleteMemberService.deleteMember(1L)
                Then("멤버가 삭제되어야 한다.") {
                    memberRepository.findAll().size shouldBe 0
                }
            }
        }
    }
}