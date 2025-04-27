package io.toasting.global.security.jwt

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.toasting.domain.member.vo.RoleType

class JwtTest :
    BehaviorSpec({
        val jwtFactory =
            JwtFactory(
                secret = "asdfsdfsdfdsf",
                accessExpiredMs = 60 * 60 * 2,
                refreshExpiredMs = 60 * 60 * 24 * 7,
            )
        Given("JWT 생성 테스트에서") {
            val username = "howudong"
            val role = RoleType.ROLE_USER.name
            When("JWT를 생성했을 때") {
                val token = jwtFactory.createAccessToken(username, role, 60 * 60 * 2)
                Then("담긴 값들이 모두 추출되어야한다.") {
                    jwtFactory.role(token) shouldBe role
                    jwtFactory.memberUuid(token) shouldBe username
                }
                Then("잘못된 토큰으로 추출하려고 하면 null을 반환한다.") {
                    val result = jwtFactory.role("invalidToken.dsfd.ss")
                    result shouldBe null
                }
            }
        }
    })
