package io.toasting.domain.member.controller

import io.toasting.global.api.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/member")
class MemberController {
    @PostMapping("/login/google")
    fun loginGoogle(): ApiResponse<Unit> = ApiResponse.onSuccess()
}
