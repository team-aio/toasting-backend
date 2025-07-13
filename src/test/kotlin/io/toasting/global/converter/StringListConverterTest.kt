package io.toasting.global.converter

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

class StringListConverterTest : BehaviorSpec({
    val converter = StringListConverter()

    Given("StringListConverter가 주어졌을 때") {
        When("List<Long>을 데이터베이스 컬럼으로 변환하면") {
            val list = listOf(1L, 2L, 3L)
            val result = converter.convertToDatabaseColumn(list)

            Then("쉼표로 구분된 문자열이 반환되어야 한다") {
                result shouldBe "1,2,3"
            }
        }

        When("데이터베이스 컬럼 값을 List<Long>으로 변환하면") {
            val dbData = "1,2,3"
            val result = converter.convertToEntityAttribute(dbData)

            Then("문자열이 리스트로 분리되어야 한다") {
                result shouldContainExactly listOf(1, 2, 3)
            }
        }

        When("빈 문자열이 주어지면") {
            val dbData = ""
            val result = converter.convertToEntityAttribute(dbData)

            Then("빈 리스트가 반환되어야 한다") {
                result shouldBe emptyList()
            }
        }

        When("null이 주어지면") {
            val result = converter.convertToEntityAttribute(null)

            Then("빈 리스트가 반환되어야 한다") {
                result shouldBe emptyList()
            }
        }
    }
})