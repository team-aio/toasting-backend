package io.toasting.domain.member.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.toasting.creator.member.MemberCreator
import io.toasting.domain.member.application.input.AddFollowInput
import io.toasting.domain.member.application.input.CancelFollowInput
import io.toasting.domain.member.application.input.ExistsFollowInput
import io.toasting.domain.member.exception.FollowExceptionHandler
import io.toasting.domain.member.repository.FollowRepository
import io.toasting.domain.member.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class FollowServiceTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringTestExtension(SpringTestLifecycleMode.Root))

    @Autowired
    private lateinit var followService: FollowService

    @Autowired
    private lateinit var followRepository: FollowRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    init {
        given("저장된 유저 정보가 주어졌을 때") {
            val m1 = MemberCreator.defaultMember(null, "test1", "test1@naver.com")
            val m2 = MemberCreator.defaultMember(null, "test2", "test2@naver.com")

            memberRepository.saveAll(listOf(m1, m2))
            When("팔로우를 추가하면") {
                followService.addFollow(AddFollowInput(m1.id!!, m2.id!!))
                Then("저장된 정보가 일치하여야 한다") {
                    val follows = followRepository.findAll()
                    val follow = follows.find { it.fromMember.id == m1.id && it.toMember.id == m2.id }!!
                    follow.fromMember.nickname shouldBe m1.nickname
                    follow.toMember.nickname shouldBe m2.nickname
                }
            }
            When(" 이미 팔로우 했는지 확인했을 때") {
                val result = followService.existsFollow(ExistsFollowInput(m1.id!!, m2.id!!))
                Then("팔로우를 했으므로 true를 반환해야 한다") {
                    result shouldBe true
                }
            }
            When("팔로우를 취소하면") {
                followService.cancelFollow(CancelFollowInput(m1.id!!, m2.id!!))
                Then("저장된 정보가 없어야 한다") {
                    val follow = followRepository.findAll()
                    follow.isEmpty() shouldBe true
                }
            }
            When("팔로우를 취소하고 이미 팔로우 했는지 확인했을 때") {
                followService.cancelFollow(CancelFollowInput(m1.id!!, m2.id!!))
                val result = followService.existsFollow(ExistsFollowInput(m1.id!!, m2.id!!))
                Then("팔로우를 취소했으므로 false를 반환해야 한다") {
                    result shouldBe false
                }
            }
            When("내 자신을 팔로우 하려고 하면") {
                Then("예외가 발생해야 한다") {
                    shouldThrow<FollowExceptionHandler.SelfFollowException> {
                        followService.addFollow(AddFollowInput(m1.id!!, m1.id!!))
                    }
                }
            }
        }
    }
}
