package io.toasting.domain.member.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.toasting.creator.member.MemberCreator
import io.toasting.domain.member.application.input.AddFollowInput
import io.toasting.domain.member.application.input.CancelFollowInput
import io.toasting.domain.member.application.input.ExistsFollowInput
import io.toasting.domain.member.exception.FollowExceptionHandler
import io.toasting.domain.member.repository.FollowRepository
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.global.codec.MemberIdCodec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class FollowServiceTest : BehaviorSpec() {
    override fun extensions(): List<Extension> = listOf(SpringExtension)

    @Autowired
    private lateinit var followService: FollowService

    @Autowired
    private lateinit var followRepository: FollowRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var memberIdCodec: MemberIdCodec

    init {
        given("저장된 유저 정보가 주어졌을 때") {
            val m1 = MemberCreator.defaultMember(1L, "test1", "test1@naver.com", memberIdCodec.encode(1))
            val m2 = MemberCreator.defaultMember(2L, "test2", "test2@naver.com", memberIdCodec.encode(2))

            memberRepository.saveAll(listOf(m1, m2))
            When("팔로우를 추가하면") {
                followService.addFollow(AddFollowInput(1L, 2L))
                Then("저장된 정보가 일치하여야 한다") {
                    val follow = followRepository.findById(1L).get()
                    follow.fromMember.nickname shouldBe m1.nickname
                    follow.toMember.nickname shouldBe m2.nickname
                }
            }
            When(" 이미 팔로우 했는지 확인했을 때") {
                val result = followService.existsFollow(ExistsFollowInput(1L, 2L))
                Then("팔로우를 했으므로 true를 반환해야 한다") {
                    result shouldBe true
                }
            }
            When("팔로우를 취소하면") {
                followService.cancelFollow(CancelFollowInput(1L, 2L))
                Then("저장된 정보가 없어야 한다") {
                    val follow = followRepository.findAll()
                    follow.isEmpty() shouldBe true
                }
            }
            When("팔로우를 취소하고 이미 팔로우 했는지 확인했을 때") {
                followService.cancelFollow(CancelFollowInput(1L, 2L))
                val result = followService.existsFollow(ExistsFollowInput(1L, 2L))
                Then("팔로우를 취소했으므로 false를 반환해야 한다") {
                    result shouldBe false
                }
            }
            When("내 자신을 팔로우 하려고 하면") {
                Then("예외가 발생해야 한다") {
                    shouldThrow<FollowExceptionHandler.SelfFollowException> {
                        followService.addFollow(AddFollowInput(1L, 1L))
                    }
                }
            }
        }
    }
}
