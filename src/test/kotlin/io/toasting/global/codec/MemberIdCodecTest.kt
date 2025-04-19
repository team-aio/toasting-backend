package io.toasting.global.codec

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.toasting.creator.member.MemberCreator
import io.toasting.domain.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemberIdCodecTest : BehaviorSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    @Autowired
    private lateinit var memberIdCodec: MemberIdCodec

    @Autowired
    private lateinit var memberRepository: MemberRepository

    init {
        Given("새로운 멤버가 저장되어 있을때") {
            val memberIdHash = memberIdCodec.encode(1L)
            val newMember = MemberCreator.defaultMember(1L, "test", "test@test.com", memberIdHash)
            memberRepository.save(newMember)
            When("그 멤버의 해쉬값으로 디코딩하면") {
                val memberId = memberIdCodec.decode(memberIdHash)
                Then("멤버의 id가 반환되어야 한다.") {
                    memberId shouldBe 1L
                }
            }
        }

    }

}