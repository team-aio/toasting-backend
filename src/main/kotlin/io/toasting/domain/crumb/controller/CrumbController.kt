package io.toasting.domain.crumb.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.toasting.domain.crumb.application.FindCrumbService
import io.toasting.domain.crumb.controller.request.FindCrumbsBetweenDateRequest
import io.toasting.domain.crumb.controller.response.FindCrumbsBetweenDateResponse
import io.toasting.domain.member.application.converter.MemberUuidConverter
import io.toasting.global.api.ApiResponse
import io.toasting.global.extension.toLocalDateOrThrow
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/members")
@Tag(name = "Member", description = "회원 관련 API")
class CrumbController(
    private val findCrumbService: FindCrumbService,
    private val memberUuidConverter: MemberUuidConverter,
) {
    @GetMapping("/{memberId}/crumbs")
    @Operation(summary = "빵가루 리스트 조회", description = "빵가루를 시작 날짜와 종료 날짜를 기준으로 조회합니다. 시작 날짜와 종료 날짜를 포함합니다.")
    fun findCrumbsBetweenDate(
        @PathVariable("memberId") memberUuid: String,
        @RequestBody request: FindCrumbsBetweenDateRequest
    ): ApiResponse<List<FindCrumbsBetweenDateResponse>> {
        val memberId = memberUuidConverter.toMemberId(memberUuid)

        return findCrumbService.findAllBy(
            memberId = memberId,
            startDate = request.startDate.toLocalDateOrThrow(),
            endDate = request.endDate.toLocalDateOrThrow()
        ).map {
            FindCrumbsBetweenDateResponse.from(it)
        }.let { ApiResponse.onSuccess(it) }
    }
}