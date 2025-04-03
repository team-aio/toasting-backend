package io.toasting.global.util

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class HashUtilTest :
    BehaviorSpec({
        Given("값이 주어졌을 때") {
            val rawData = "test"
            val rawData2 = "test2"
            When("해시값을 생성하고, 생성한 값과 비교한다면") {
                val hashedData = HashUtil.encode(rawData)
                Then("해시값이 같아야 한다") {
                    HashUtil.matches(rawData, hashedData) shouldBe true
                }
            }
            When("해시값을 생성하고, 다른 값과 비교한다면") {
                val hashedData = HashUtil.encode(rawData)
                Then("해시값이 달라야 한다") {
                    HashUtil.matches(rawData2, hashedData) shouldBe false
                }
            }
        }
    })
