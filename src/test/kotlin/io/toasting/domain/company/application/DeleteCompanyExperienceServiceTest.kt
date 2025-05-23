package io.toasting.domain.company.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.toasting.domain.company.entity.Company
import io.toasting.domain.company.entity.CompanyExperience
import io.toasting.domain.company.entity.CustomCompanyExperience
import io.toasting.domain.company.exception.CompanyExperienceExceptionHandler.CompanyExperienceNotFoundException
import io.toasting.domain.company.exception.CompanyExperienceExceptionHandler.UnauthorizedDeleteCompanyExperienceException
import io.toasting.domain.company.exception.CustomCompanyExperienceExceptionHandler.CustomCompanyExperienceNotFoundException
import io.toasting.domain.company.exception.CustomCompanyExperienceExceptionHandler.UnauthorizedDeleteCustomCompanyExperienceException
import io.toasting.domain.company.repository.CompanyExperienceRepository
import io.toasting.domain.company.repository.CompanyRepository
import io.toasting.domain.company.repository.CustomCompanyExperienceRepository
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.repository.MemberRepository
import jakarta.persistence.EntityManager
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
class DeleteCompanyExperienceServiceTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringTestExtension(SpringTestLifecycleMode.Root))

    @Autowired
    private lateinit var deleteCompanyExperienceService: DeleteCompanyExperienceService

    @Autowired
    private lateinit var companyExperienceRepository: CompanyExperienceRepository

    @Autowired
    private lateinit var customCompanyExperienceRepository: CustomCompanyExperienceRepository

    @Autowired
    private lateinit var companyRepository: CompanyRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var entityManager: EntityManager

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
                Member.defaultMember("user1", "user1@toasting.io", UUID.randomUUID())
            )
            anotherMember = memberRepository.save(
                Member.defaultMember("user2", "user2@toasting.io", UUID.randomUUID())
            )
            company = companyRepository.save(Company(name = "테스트회사"))
            companyExperience = companyExperienceRepository.save(
                CompanyExperience(
                    companyId = company.id!!,
                    memberId = member.id!!,
                    position = "Backend Developer",
                    startDate = LocalDate.of(2022, 1, 1),
                    endDate = LocalDate.of(2023, 1, 1),
                    profileImage = "profile_image.png",
                    activities = "백엔드 개발 및 배포",
                    isView = true,
                )
            )
            customCompanyExperience = customCompanyExperienceRepository.save(
                CustomCompanyExperience(
                    memberId = member.id!!,
                    companyName = "사이드프로젝트팀",
                    position = "Fullstack",
                    startDate = LocalDate.of(2022, 6, 1),
                    endDate = null,
                    profileImage = "custom-img.png",
                    activities = "프로젝트 리딩 및 개발",
                    isView = true,
                )
            )
        }

        Given("존재하는 회사 경험이 있을 때") {
            When("타인 memberId로 회사 경험을 삭제하면") {
                Then("UnauthorizedDeleteCompanyExperienceException이 발생한다") {
                    shouldThrow<UnauthorizedDeleteCompanyExperienceException> {
                        deleteCompanyExperienceService.deleteExistCompanyExperience(anotherMember.id!!, companyExperience.id!!)
                    }
                }
            }
            When("존재하지 않는 경험 id로 삭제하면") {
                Then("CompanyExperienceNotFoundException이 발생한다") {
                    shouldThrow<CompanyExperienceNotFoundException> {
                        deleteCompanyExperienceService.deleteExistCompanyExperience(member.id!!, -1L)
                    }
                }
            }
            When("본인 memberId로 회사 경험을 삭제하면") {
                deleteCompanyExperienceService.deleteExistCompanyExperience(member.id!!, companyExperience.id!!)
                Then("정상적으로 삭제된다") {
                    companyExperienceRepository.findById(companyExperience.id!!).getOrNull() shouldBe null
                }
            }
        }

        Given("존재하는 커스텀 회사 경험이 있을 때") {
            When("타인 memberId로 커스텀 회사 경험을 삭제하면") {
                Then("UnauthorizedDeleteCustomCompanyExperienceException이 발생한다") {
                    shouldThrow<UnauthorizedDeleteCustomCompanyExperienceException> {
                        deleteCompanyExperienceService.deleteCustomCompanyExperience(
                            anotherMember.id!!,
                            customCompanyExperience.id!!
                        )
                    }
                }
            }
            When("존재하지 않는 경험 id로 삭제하면") {
                Then("CustomCompanyExperienceNotFoundException이 발생한다") {
                    shouldThrow<CustomCompanyExperienceNotFoundException> {
                        deleteCompanyExperienceService.deleteCustomCompanyExperience(member.id!!, -1L)
                    }
                }
            }
            When("본인 memberId로 커스텀 회사 경험을 삭제하면") {
                deleteCompanyExperienceService.deleteCustomCompanyExperience(member.id!!, customCompanyExperience.id!!)
                Then("정상적으로 삭제된다") {
                    customCompanyExperienceRepository.findById(customCompanyExperience.id!!).getOrNull() shouldBe null
                }
            }
        }
    }

}