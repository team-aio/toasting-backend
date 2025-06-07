package io.toasting.global.extension

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.toasting.global.api.exception.handler.FormatExceptionHandler
import java.time.LocalDate

class StringExtensionTest : BehaviorSpec({
    Given("파싱할 날짜가 올바르게 주어졌을 때") {
        val dateString = "2025-01-01"
        When("ToLocalDate를 통해 파싱을 하면") {
            val localDate = dateString.toLocalDateOrThrow()
            Then("올바른 LocalDate가 반환되어야 한다.") {
                localDate shouldBe LocalDate.of(2025, 1, 1)
            }
        }
    }
    Given("파싱할 날짜가 잘못 주어졌을 때") {
        val dateString = "2025-01-01 00:00:00"
        When("ToLocalDate를 통해 파싱을 하면") {
            Then("null이 나와야 한다.") {
                shouldThrow<FormatExceptionHandler.InvalidDateFormatException> {
                    dateString.toLocalDateOrThrow()
                }
            }
        }
    }
})