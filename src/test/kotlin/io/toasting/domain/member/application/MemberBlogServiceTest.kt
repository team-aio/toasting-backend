package io.toasting.domain.member.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.exception.MemberExceptionHandler
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.post.vo.SourceType
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MemberBlogServiceTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringTestExtension(SpringTestLifecycleMode.Root))

    @Autowired
    private lateinit var memberBlogService: MemberBlogService

    @Autowired
    private lateinit var memberRepository: MemberRepository

    private lateinit var member: Member

    init {
        beforeSpec {
            member = Member.defaultMember("testMember", "test@example.com", UUID.randomUUID())
            memberRepository.save(member)
        }

        Given("멤버가 존재하고") {
            When("블로그 연동 상태를 조회하면") {
                val result = memberBlogService.getLinkedBlogStatus(member.id!!)

                Then("초기 상태는 모든 블로그가 연동되지 않은 상태다") {
                    result.velogId shouldBe null
                    result.tistoryId shouldBe null
                }
            }

            When("Velog가 연동된 멤버의 블로그 상태를 조회하면") {
                member.registerBlog(SourceType.VELOG, "testVelogId")
                memberRepository.save(member)

                val result = memberBlogService.getLinkedBlogStatus(member.id!!)

                Then("Velog ID는 있고 Tistory ID는 없어야 한다") {
                    result.velogId shouldBe "testVelogId"
                    result.tistoryId shouldBe null
                }
            }

            When("Tistory도 연동 후 블로그 상태를 조회하면") {
                member.registerBlog(SourceType.TISTORY, "testTistoryId")
                memberRepository.save(member)

                val result = memberBlogService.getLinkedBlogStatus(member.id!!)

                Then("Tistory ID는 있고 Velog ID는 없어야 한다") {
                    result.velogId shouldBe "testVelogId"
                    result.tistoryId shouldBe "testTistoryId"
                }
            }
        }

        Given("존재하지 않는 멤버 ID로") {
            When("블로그 연동 상태를 조회하면") {
                Then("MemberNotFoundException이 발생한다") {
                    shouldThrow<MemberExceptionHandler.MemberNotFoundException> {
                        memberBlogService.getLinkedBlogStatus(0L)
                    }
                }
            }
        }
    }
}