package io.toasting.domain.company.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.toasting.domain.company.application.input.*
import io.toasting.domain.company.entity.Company
import io.toasting.domain.company.entity.CompanyExperience
import io.toasting.domain.company.entity.CustomCompanyExperience
import io.toasting.domain.company.exception.CompanyExceptionHandler
import io.toasting.domain.company.exception.CompanyExperienceExceptionHandler
import io.toasting.domain.company.exception.CustomCompanyExperienceExceptionHandler
import io.toasting.domain.company.repository.CompanyExperienceRepository
import io.toasting.domain.company.repository.CompanyRepository
import io.toasting.domain.company.repository.CustomCompanyExperienceRepository
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.*
import kotlin.jvm.optionals.getOrNull

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UpdateCompanyExperienceServiceTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringTestExtension(SpringTestLifecycleMode.Root))

    @Autowired
    private lateinit var updateCompanyExperienceService: UpdateCompanyExperienceService

    @Autowired
    private lateinit var companyRepository: CompanyRepository

    @Autowired
    private lateinit var companyExperienceRepository: CompanyExperienceRepository

    @Autowired
    private lateinit var customCompanyExperienceRepository: CustomCompanyExperienceRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    private lateinit var member: Member
    private lateinit var anotherMember: Member
    private lateinit var company: Company
    private lateinit var companyExperience: CompanyExperience
    private lateinit var customCompanyExperience: CustomCompanyExperience

    init {
        beforeSpec {
            companyExperienceRepository.deleteAll()
            customCompanyExperienceRepository.deleteAll()
            companyRepository.deleteAll()
            memberRepository.deleteAll()

            member = memberRepository.save(
                Member.defaultMember("userX", "userX@toasting.io", UUID.randomUUID())
            )
            anotherMember = memberRepository.save(
                Member.defaultMember("userY", "userY@toasting.io", UUID.randomUUID())
            )
            company = companyRepository.save(
                Company(
                    name = "업데이트회사",
                    profileImage = "after.png",
                    )
            )
            companyExperience = companyExperienceRepository.save(
                CompanyExperience(
                    companyId = company.id!!,
                    memberId = member.id!!,
                    position = "백엔드 개발자",
                    startDate = LocalDate.of(2021, 1, 1),
                    endDate = LocalDate.of(2022, 1, 1),
                    activities = "초기 작업",
                    isView = true,
                )
            )
            customCompanyExperience = customCompanyExperienceRepository.save(
                CustomCompanyExperience(
                    memberId = member.id!!,
                    companyName = "커스텀회사",
                    position = "프론트 개발자",
                    startDate = LocalDate.of(2020, 1, 1),
                    endDate = null,
                    profileImage = "custom-before.png",
                    activities = "UI 설계",
                    isView = true,
                )
            )
        }

        Given("존재하는 회사 경험이 있을 때") {
            When("정상적으로 회사 경험을 수정하면") {
                val input = UpdateExistCompanyExperienceInput(
                    memberId = member.id!!,
                    experienceId = companyExperience.id!!,
                    companyId = company.id!!,
                    startDate = LocalDate.of(2021, 2, 1),
                    endDate = LocalDate.of(2022, 2, 1),
                    position = "시니어 백엔드",
                    activities = "업데이트된 작업",
                )
                updateCompanyExperienceService.updateExistCompanyExperience(input)
                Then("회사 경험 정보가 정상적으로 수정된다") {
                    val updated = companyExperienceRepository.findById(companyExperience.id!!).get()
                    updated.position shouldBe "시니어 백엔드"
                    updated.activities shouldBe "업데이트된 작업"
                    updated.startDate shouldBe LocalDate.of(2021, 2, 1)
                    updated.endDate shouldBe LocalDate.of(2022, 2, 1)
                    updated.companyId shouldBe company.id
                }
            }
            When("존재하지 않는 회사 경험 id로 수정하면") {
                val input = UpdateExistCompanyExperienceInput(
                    memberId = member.id!!,
                    experienceId = -1L,
                    companyId = company.id!!,
                    startDate = LocalDate.now(),
                    endDate = LocalDate.now(),
                    position = "직함",
                    activities = "내용",
                )
                Then("CompanyExperienceNotFoundException이 발생한다") {
                    shouldThrow<CompanyExperienceExceptionHandler.CompanyExperienceNotFoundException> {
                        updateCompanyExperienceService.updateExistCompanyExperience(input)
                    }
                }
            }
            When("다른 사용자가 회사 경험을 수정하면") {
                val input = UpdateExistCompanyExperienceInput(
                    memberId = anotherMember.id!!,
                    experienceId = companyExperience.id!!,
                    companyId = company.id!!,
                    startDate = LocalDate.now(),
                    endDate = LocalDate.now(),
                    position = "직함",
                    activities = "내용",
                )
                Then("UnauthorizedUpdateException이 발생한다") {
                    shouldThrow<CompanyExperienceExceptionHandler.UnauthorizedUpdateException> {
                        updateCompanyExperienceService.updateExistCompanyExperience(input)
                    }
                }
            }
            When("존재하지 않는 회사 id로 수정하면") {
                val input = UpdateExistCompanyExperienceInput(
                    memberId = member.id!!,
                    experienceId = companyExperience.id!!,
                    companyId = -1L,
                    startDate = LocalDate.now(),
                    endDate = LocalDate.now(),
                    position = "직함",
                    activities = "내용",
                )
                Then("CompanyNotFoundException이 발생한다") {
                    shouldThrow<CompanyExceptionHandler.CompanyNotFoundException> {
                        updateCompanyExperienceService.updateExistCompanyExperience(input)
                    }
                }
            }
        }

        Given("존재하는 커스텀 회사 경험이 있을 때") {
            When("정상적으로 커스텀 회사 경험을 수정하면") {
                val input = UpdateCustomCompanyExperienceInput(
                    memberId = member.id!!,
                    experienceId = customCompanyExperience.id!!,
                    name = "새커스텀회사",
                    startDate = LocalDate.of(2020, 5, 1),
                    endDate = LocalDate.of(2021, 1, 1),
                    position = "디자이너",
                    activities = "UI/UX 설계",
                    imageUrl = "custom-after.png"
                )
                updateCompanyExperienceService.updateCustomCompanyExperience(input)
                Then("커스텀 회사 경험 정보가 정상적으로 수정된다") {
                    val updated = customCompanyExperienceRepository.findById(customCompanyExperience.id!!).get()
                    updated.position shouldBe "디자이너"
                    updated.companyName shouldBe "새커스텀회사"
                    updated.profileImage shouldBe "custom-after.png"
                    updated.activities shouldBe "UI/UX 설계"
                }
            }
            When("존재하지 않는 경험 id로 수정하면") {
                val input = UpdateCustomCompanyExperienceInput(
                    memberId = member.id!!,
                    experienceId = -1L,
                    name = "임의",
                    startDate = LocalDate.now(),
                    endDate = null,
                    position = "임시",
                    activities = "테스트",
                    imageUrl = "test.png"
                )
                Then("CustomCompanyExperienceNotFoundException이 발생한다") {
                    shouldThrow<CustomCompanyExperienceExceptionHandler.CustomCompanyExperienceNotFoundException> {
                        updateCompanyExperienceService.updateCustomCompanyExperience(input)
                    }
                }
            }
            When("다른 사용자가 커스텀 경험을 수정하면") {
                val input = UpdateCustomCompanyExperienceInput(
                    memberId = anotherMember.id!!,
                    experienceId = customCompanyExperience.id!!,
                    name = "임의2",
                    startDate = LocalDate.now(),
                    endDate = null,
                    position = "임시2",
                    activities = "테스트2",
                    imageUrl = "test2.png"
                )
                Then("UnauthorizedUpdateException이 발생한다") {
                    shouldThrow<CustomCompanyExperienceExceptionHandler.UnauthorizedUpdateException> {
                        updateCompanyExperienceService.updateCustomCompanyExperience(input)
                    }
                }
            }
        }

        Given("회사 경험의 isView를 수정할 때") {
            When("본인 memberId로 isView 값을 변경하면") {
                val input = UpdateExistCompanyExperienceIsViewInput(
                    memberId = member.id!!,
                    experienceId = companyExperience.id!!,
                    isView = false
                )
                updateCompanyExperienceService.updateExistCompanyExperienceIsView(input)
                Then("정상적으로 isView 값이 변경된다") {
                    val updated = companyExperienceRepository.findById(companyExperience.id!!).get()
                    updated.isView shouldBe false
                }
            }
            When("타인 memberId로 isView 값을 변경하면") {
                val input = UpdateExistCompanyExperienceIsViewInput(
                    memberId = anotherMember.id!!,
                    experienceId = companyExperience.id!!,
                    isView = false
                )
                Then("UnauthorizedUpdateIsViewException이 발생한다") {
                    shouldThrow<CompanyExperienceExceptionHandler.UnauthorizedUpdateIsViewException> {
                        updateCompanyExperienceService.updateExistCompanyExperienceIsView(input)
                    }
                }
            }
            When("존재하지 않는 경험 id로 isView 값을 변경하면") {
                val input = UpdateExistCompanyExperienceIsViewInput(
                    memberId = member.id!!,
                    experienceId = -1L,
                    isView = true
                )
                Then("CompanyExperienceNotFoundException이 발생한다") {
                    shouldThrow<CompanyExperienceExceptionHandler.CompanyExperienceNotFoundException> {
                        updateCompanyExperienceService.updateExistCompanyExperienceIsView(input)
                    }
                }
            }
        }

        Given("커스텀 회사 경험의 isView를 수정할 때") {
            When("본인 memberId로 isView 값을 변경하면") {
                val input = UpdateCustomCompanyExperienceIsViewInput(
                    memberId = member.id!!,
                    experienceId = customCompanyExperience.id!!,
                    isView = false
                )
                updateCompanyExperienceService.updateCustomCompanyExperienceIsView(input)
                Then("정상적으로 isView 값이 변경된다") {
                    val updated = customCompanyExperienceRepository.findById(customCompanyExperience.id!!).get()
                    updated.isView shouldBe false
                }
            }
            When("타인 memberId로 isView 값을 변경하면") {
                val input = UpdateCustomCompanyExperienceIsViewInput(
                    memberId = anotherMember.id!!,
                    experienceId = customCompanyExperience.id!!,
                    isView = false
                )
                Then("UnauthorizedUpdateIsViewException이 발생한다") {
                    shouldThrow<CustomCompanyExperienceExceptionHandler.UnauthorizedUpdateIsViewException> {
                        updateCompanyExperienceService.updateCustomCompanyExperienceIsView(input)
                    }
                }
            }
            When("존재하지 않는 경험 id로 isView 값을 변경하면") {
                val input = UpdateCustomCompanyExperienceIsViewInput(
                    memberId = member.id!!,
                    experienceId = -1L,
                    isView = false
                )
                Then("CustomCompanyExperienceNotFoundException이 발생한다") {
                    shouldThrow<CustomCompanyExperienceExceptionHandler.CustomCompanyExperienceNotFoundException> {
                        updateCompanyExperienceService.updateCustomCompanyExperienceIsView(input)
                    }
                }
            }
        }
    }
}