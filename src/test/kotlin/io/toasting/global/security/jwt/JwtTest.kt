package io.toasting.global.security.jwt

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.toasting.creator.member.MemberCreator
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.member.vo.RoleType
import io.toasting.global.codec.MemberIdCodec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class JwtTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var memberIdCodec: MemberIdCodec

    private lateinit var jwtFactory: JwtFactory

    init {
        val memberIdHash = memberIdCodec.encode(1)
        beforeTest {
            jwtFactory =
                JwtFactory(
                    secret = "asdfsdfsdfdsf",
                    accessExpiredMs = 60 * 60 * 2,
                    refreshExpiredMs = 60 * 60 * 24 * 7,
                )

            memberRepository.saveAndFlush(
                MemberCreator.defaultMember(
                    1,
                    "etes",
                    "tesdff",
                    memberIdHash = memberIdHash,
                ),
            )
        }

        Given("JWT 생성 테스트에서") {
            val role = RoleType.ROLE_USER.name
            When("JWT를 생성했을 때") {
                val token = jwtFactory.createAccessToken(memberIdHash, role, 60 * 60 * 2)
                Then("담긴 값들이 모두 추출되어야한다.") {
                    jwtFactory.role(token) shouldBe role
                    jwtFactory.memberId(token) shouldBe "1"
                }
                Then("잘못된 토큰으로 추출하려고 하면 null을 반환한다.") {
                    val result = jwtFactory.role("invalidToken.dsfd.ss")
                    result shouldBe null
                }
            }
        }
    }
}
