package io.toasting.domain.member.application

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.toasting.domain.member.application.input.AddFollowInput
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.repository.FollowRepository
import io.toasting.domain.member.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FollowServiceTest : BehaviorSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    @Autowired
    private lateinit var followService: FollowService

    @Autowired
    private lateinit var followRepository: FollowRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    init {
        given("저장된 유저 정보가 주어졌을 때") {
            val m1 = Member.defaultMember("test1", "test1@naver.com")
            val m2 = Member.defaultMember("test2", "test2@naver.com")

            memberRepository.saveAll(listOf(m1, m2))
            When("팔로우를 추가하면") {
                followService.addFollow(AddFollowInput(1L, 2L))
                Then("저장된 정보가 일치하여야 한다") {
                    val follow = followRepository.findById(1L).get()
                    follow.fromMember.nickname shouldBe m1.nickname
                    follow.toMember.nickname shouldBe m2.nickname
                }
            }
        }
    }
}
