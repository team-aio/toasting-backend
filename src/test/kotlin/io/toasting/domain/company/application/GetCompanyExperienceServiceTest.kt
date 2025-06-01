package io.toasting.domain.company.application

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldBeSortedWith
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.toasting.domain.company.application.output.GetCompanyExperienceOutput
import io.toasting.domain.company.entity.Company
import io.toasting.domain.company.entity.CompanyExperience
import io.toasting.domain.company.entity.CustomCompanyExperience
import io.toasting.domain.company.repository.CompanyExperienceRepository
import io.toasting.domain.company.repository.CompanyRepository
import io.toasting.domain.company.repository.CustomCompanyExperienceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class GetCompanyExperienceServiceTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringTestExtension(SpringTestLifecycleMode.Root))

    @Autowired
    private lateinit var getCompanyExperienceService: GetCompanyExperienceService

    @Autowired
    private lateinit var companyRepository: CompanyRepository

    @Autowired
    private lateinit var companyExperienceRepository: CompanyExperienceRepository

    @Autowired
    private lateinit var customCompanyExperienceRepository: CustomCompanyExperienceRepository

    private lateinit var company: Company
    private lateinit var company2: Company

    init {
        beforeSpec {
            customCompanyExperienceRepository.deleteAll()
            companyExperienceRepository.deleteAll()
            companyRepository.deleteAll()

            company = companyRepository.save(
                Company(
                    name = "토스",
                    profileImage = "profile_image.png",
                )
            )
            company2 = companyRepository.save(
                Company(
                    name = "카카오",
                    profileImage = "profile_image.png",
                )
            )
        }

        Given("회원 ID로 회사 경험이 등록되어 있을 때") {
            val memberId = 1L

            val experience = CompanyExperience(
                companyId = company.id!!,
                memberId = memberId,
                position = "Backend Developer",
                startDate = LocalDate.of(2022, 1, 1),
                endDate = LocalDate.of(2023, 1, 1),
                activities = "백엔드 개발 및 배포",
                isView = true,
            )

            val experience2 = CompanyExperience(
                companyId = company2.id!!,
                memberId = memberId,
                position = "Frontend Developer",
                startDate = LocalDate.of(2023, 2, 1),
                endDate = null,
                activities = "프론트 개발",
                isView = false,
            )

            companyExperienceRepository.saveAll(listOf(experience, experience2))

            val customExperience = CustomCompanyExperience(
                memberId = memberId,
                companyName = "사이드프로젝트팀",
                position = "Fullstack",
                startDate = LocalDate.of(2022, 6, 1),
                endDate = null,
                profileImage = "custom-img.png",
                activities = "프로젝트 리딩 및 개발",
                isView = true,
            )
            customCompanyExperienceRepository.save(customExperience)

            When("GetCompanyExperienceService로 전체 경험을 조회하면") {
                val results = getCompanyExperienceService.getCompanyExperience(memberId)

                Then("총 3개의 경험이 반환된다") {
                    results shouldHaveSize 3
                }

                Then("회사명, 포지션 등 주요 정보가 일치한다") {
                    val exp = results.find { it.name == company.name }
                    exp?.position shouldBe "Backend Developer"
                    exp?.activities shouldBe "백엔드 개발 및 배포"

                    val exp2 = results.find { it.name == company2.name }
                    exp2?.position shouldBe "Frontend Developer"
                    exp2?.isView shouldBe false

                    val customExp = results.find { it.isCustom }
                    customExp?.name shouldBe "사이드프로젝트팀"
                    customExp?.activities shouldBe "프로젝트 리딩 및 개발"
                }

                Then("각 경험의 isView 값이 올바르게 매핑된다") {
                    results.find { it.name == company.name }?.isView shouldBe true
                    results.find { it.name == company2.name }?.isView shouldBe false
                    results.find { it.name == "사이드프로젝트팀" }?.isView shouldBe true
                }

                Then("endDate가 null이 아닌 것이 먼저 오고, endDate 내림차순, 동일한 endDate 내에서는 startDate 내림차순으로 정렬된다.") {
                    results.shouldBeSortedWith(
                        compareBy<GetCompanyExperienceOutput>(
                            { it.endDate == null }
                        ).thenByDescending { it.endDate }
                            .thenByDescending { it.startDate }
                    )
                }

                Then("CompanyExperience isCustom == false, CustomCompanyExperience isCustom == true") {
                    results.find { it.name == company.name }?.isCustom shouldBe false
                    results.find { it.name == company2.name }?.isCustom shouldBe false
                    results.find { it.name == "사이드프로젝트팀" }?.isCustom shouldBe true
                }
                Then("CompanyExperience의 imageUrl은 회사의 profileImage와 같다") {
                    results.find { it.name == company.name }?.imageUrl shouldBe company.profileImage
                    results.find { it.name == company2.name }?.imageUrl shouldBe company2.profileImage
                }
                Then("CustomCompanyExperience의 imageUrl은 커스텀 경험의 profileImage와 같다") {
                    val customExp = customCompanyExperienceRepository.findAll().first()
                    results.find { it.isCustom }?.imageUrl shouldBe customExp.profileImage
                }

            }
        }

        Given("등록된 경험이 없는 회원일 때") {
            val memberId = -1L
            When("경험을 조회하면") {
                val results = getCompanyExperienceService.getCompanyExperience(memberId)
                Then("빈 리스트가 반환된다") {
                    results shouldHaveSize 0
                }
            }
        }
    }
}