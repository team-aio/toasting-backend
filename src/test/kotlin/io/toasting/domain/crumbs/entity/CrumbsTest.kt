package io.toasting.domain.crumbs.entity

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class CrumbsTest : BehaviorSpec({
    Given("멤버 ID와 활동 날짜가 주어졌을 때") {
        val memberId = 1L
        val activityDate = LocalDate.of(2023, 5, 15)

        When("Crumbs를 생성하면") {
            val crumbs = Crumbs.create(memberId, activityDate)

            Then("멤버 ID와 활동 날짜가 올바르게 설정되어야 한다") {
                crumbs.memberId shouldBe memberId
                crumbs.activityDate shouldBe activityDate
            }

            Then("활동 카운트는 0으로 초기화되어야 한다") {
                crumbs.activityCount shouldBe 0
            }

            Then("댓글 ID 리스트는 비어있어야 한다") {
                crumbs.commentIds.shouldBeEmpty()
            }
        }
    }
})
