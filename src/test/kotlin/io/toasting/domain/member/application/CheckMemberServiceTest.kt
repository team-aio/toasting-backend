package io.toasting.domain.member.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.exception.MemberExceptionHandler
import io.toasting.domain.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Transactional
class CheckMemberServiceTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var checkMemberService: CheckMemberService

    @Autowired
    private lateinit var memberRepository: MemberRepository

    init {
        Given("멤버가 저장되어 있고") {
            val member1 = Member.defaultMember("test1", "test1@naver.com")
            memberRepository.save(member1)
            When("중복된 멤버를 저장하려고 하면") {
                Then("중복된 것이 있다는 예외가 발생한다.") {
                    shouldThrow<MemberExceptionHandler.MemberNameDuplicationException> {
                        checkMemberService.checkMemberNicknameIsDuplicated("test1")
                    }
                }
            }
            When("중복되지 않은 멤버를 저장하려고 하면") {
                Then("중복된 것이 없다는 예외가 발생하지 않는다.") {
                    checkMemberService.checkMemberNicknameIsDuplicated("test2")
                }
            }
        }
    }
}
