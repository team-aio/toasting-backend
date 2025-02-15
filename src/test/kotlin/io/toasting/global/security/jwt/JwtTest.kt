package io.toasting.global.security.jwt

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class JwtTest :
    BehaviorSpec({
        val jwt = Jwt(secret = "asdfsdfsdfdsf")
        Given("JWT 생성 테스트에서") {
            val username = "howudong"
            val role = "ROLE_USER"
            When("JWT를 생성했을 때") {
                val token = jwt.createAccessToken(username, role, 60 * 60 * 2)
                Then("담긴 값들이 모두 추출되어야한다.") {
                    jwt.role(token) shouldBe role
                    jwt.memberId(token) shouldBe username
                }
                Then("잘못된 토큰으로 추출하려고 하면 null을 반환한다.") {
                    val result = jwt.role("invalidToken.dsfd.ss")
                    result shouldBe null
                }
            }
        }
    })
