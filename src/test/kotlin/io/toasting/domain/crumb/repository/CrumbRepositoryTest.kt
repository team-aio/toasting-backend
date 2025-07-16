package io.toasting.domain.crumb.repository

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.toasting.domain.crumb.entity.Crumb
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor
import java.time.LocalDate

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CrumbRepositoryTest(
    private val crumbRepository: CrumbRepository,
    private val entityManager: EntityManager,
) : BehaviorSpec() {
    override fun extensions() = listOf(SpringTestExtension(SpringTestLifecycleMode.Root))

    init {
        Given("Crumb 엔티티가 저장되어 있을떄") {
            val crumb1 = Crumb.create(memberId = 1L, activityDate = LocalDate.of(2023, 5, 15))
            val crumb2 = Crumb.create(memberId = 1L, activityDate = LocalDate.of(2023, 5, 16))
            val crumb3 = Crumb.create(memberId = 1L, activityDate = LocalDate.of(2023, 5, 17))
            val crumb4 = Crumb.create(memberId = 1L, activityDate = LocalDate.of(2023, 5, 18))
            val crumb5 = Crumb.create(memberId = 1L, activityDate = LocalDate.of(2023, 5, 19))

            crumbRepository.saveAll(listOf(crumb1, crumb2, crumb3, crumb4, crumb5))
            entityManager.flush()
            entityManager.clear()

            When("5월 15일부터 ~ 19일까지 지정해서 조회하면") {
                val startDate = LocalDate.of(2023, 5, 15)
                val endDate = LocalDate.of(2023, 5, 19)
                val result = crumbRepository.findByMemberIdAndActivityDateBetween(1L, startDate, endDate)
                
                Then("5개의 엔티티가 조회되어야 한다.") {
                    result.size shouldBe 5
                }
            }
        }
    }
}