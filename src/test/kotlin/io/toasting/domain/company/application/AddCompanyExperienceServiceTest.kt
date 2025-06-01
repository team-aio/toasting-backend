package io.toasting.domain.company.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.toasting.domain.company.application.input.AddCustomCompanyExperienceInput
import io.toasting.domain.company.application.input.AddExistCompanyExperienceInput
import io.toasting.domain.company.entity.Company
import io.toasting.domain.company.exception.CompanyExceptionHandler
import io.toasting.domain.company.repository.CompanyExperienceRepository
import io.toasting.domain.company.repository.CompanyRepository
import io.toasting.domain.company.repository.CustomCompanyExperienceRepository
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.exception.MemberExceptionHandler
import io.toasting.domain.member.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class AddCompanyExperienceServiceTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringTestExtension(SpringTestLifecycleMode.Root))

    @Autowired
    private lateinit var addCompanyExperienceService: AddCompanyExperienceService

    @Autowired
    private lateinit var companyRepository: CompanyRepository

    @Autowired
    private lateinit var companyExperienceRepository: CompanyExperienceRepository

    @Autowired
    private lateinit var customCompanyExperienceRepository: CustomCompanyExperienceRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    private lateinit var member: Member
    private lateinit var company: Company

    init {
        beforeSpec {
            customCompanyExperienceRepository.deleteAll()
            companyExperienceRepository.deleteAll()
            companyRepository.deleteAll()
            memberRepository.deleteAll()

            member = memberRepository.save(
                Member.defaultMember("testUser", "testUser@test.com", UUID.randomUUID())
            )
            company = companyRepository.save(
                Company(
                    name = "테스트회사",
                    profileImage = "profile.png"
                )
            )
        }

        Given("존재하는 회사 경험 추가 입력이 주어질 때") {
            val input = AddExistCompanyExperienceInput(
                memberId = member.id!!,
                companyId = company.id!!,
                startDate = LocalDate.of(2021, 1, 1),
                endDate = LocalDate.of(2023, 1, 1),
                position = "Backend",
                activities = "백엔드 개발 참여",
            )

            When("addExistCompanyExperience를 호출하면") {
                addCompanyExperienceService.addExistCompanyExperience(input)

                Then("CompanyExperience가 성공적으로 저장된다") {
                    val all = companyExperienceRepository.findAll()
                    all.size shouldBe 1
                    val experience = all.first()
                    experience.memberId shouldBe member.id
                    experience.companyId shouldBe company.id
                    experience.position shouldBe "Backend"
                    experience.activities shouldBe "백엔드 개발 참여"
                    experience.isView shouldBe true
                }
            }
        }

        Given("존재하지 않는 멤버 ID로 회사 경험을 추가하려고 할 때") {
            val badInput = AddExistCompanyExperienceInput(
                memberId = -1L,
                companyId = company.id!!,
                startDate = LocalDate.of(2021, 1, 1),
                endDate = LocalDate.of(2023, 1, 1),
                position = "Backend",
                activities = "백엔드 개발 참여",
            )

            When("addExistCompanyExperience를 호출하면") {
                Then("MemberNotFoundException이 발생한다") {
                    shouldThrow<MemberExceptionHandler.MemberNotFoundException> {
                        addCompanyExperienceService.addExistCompanyExperience(badInput)
                    }
                }
            }
        }

        Given("존재하지 않는 회사 ID로 회사 경험을 추가하려고 할 때") {
            val badCompanyInput = AddExistCompanyExperienceInput(
                memberId = member.id!!,
                companyId = -1L,
                startDate = LocalDate.of(2021, 1, 1),
                endDate = LocalDate.of(2023, 1, 1),
                position = "Backend",
                activities = "백엔드 개발 참여",
            )

            When("addExistCompanyExperience를 호출하면") {
                Then("CompanyNotFoundException이 발생한다") {
                    shouldThrow<CompanyExceptionHandler.CompanyNotFoundException> {
                        addCompanyExperienceService.addExistCompanyExperience(badCompanyInput)
                    }
                }
            }
        }

        Given("직접 입력 회사 경험 추가 입력이 주어질 때") {
            val input = AddCustomCompanyExperienceInput(
                memberId = member.id!!,
                name = "사이드프로젝트팀",
                startDate = LocalDate.of(2020, 1, 1),
                endDate = null,
                position = "Fullstack",
                activities = "개발 및 운영",
                imageUrl = "custom.png",
            )

            When("addCustomCompanyExperience를 호출하면") {
                addCompanyExperienceService.addCustomCompanyExperience(input)

                Then("CustomCompanyExperience가 성공적으로 저장된다") {
                    val all = customCompanyExperienceRepository.findAll()
                    all.size shouldBe 1
                    val exp = all.first()
                    exp.memberId shouldBe member.id
                    exp.companyName shouldBe "사이드프로젝트팀"
                    exp.position shouldBe "Fullstack"
                    exp.isView shouldBe true
                }
            }
        }

        Given("존재하지 않는 멤버 ID로 커스텀 회사 경험을 추가하려고 할 때") {
            val badCustomInput = AddCustomCompanyExperienceInput(
                memberId = -1L,
                name = "사이드프로젝트팀",
                startDate = LocalDate.of(2020, 1, 1),
                endDate = null,
                position = "Fullstack",
                activities = "개발 및 운영",
                imageUrl = "custom.png",
            )

            When("addCustomCompanyExperience를 호출하면") {
                Then("MemberNotFoundException이 발생한다") {
                    shouldThrow<MemberExceptionHandler.MemberNotFoundException> {
                        addCompanyExperienceService.addCustomCompanyExperience(badCustomInput)
                    }
                }
            }
        }
    }
}