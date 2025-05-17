package io.toasting.domain.company.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.company.application.AddCompanyExperienceService
import io.toasting.domain.company.application.DeleteCompanyExperienceService
import io.toasting.domain.company.application.GetCompanyExperienceService
import io.toasting.domain.company.application.UpdateCompanyExperienceService
import io.toasting.domain.company.controller.request.AddCompanyExperienceRequest
import io.toasting.domain.company.controller.request.UpdateCompanyExperienceIsViewRequest
import io.toasting.domain.company.controller.request.UpdateCompanyExperienceRequest
import io.toasting.domain.company.controller.response.GetCompanyExperienceResponse
import io.toasting.domain.member.application.converter.MemberUuidConverter
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.member.exception.MemberExceptionHandler.MemberException
import io.toasting.global.api.ApiResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/v1/members")
@Tag(name = "Member", description = "회원 관련 API")
class CompanyExperienceController(
    private val memberUuidConverter: MemberUuidConverter,
    private val addCompanyExperienceService: AddCompanyExperienceService,
    private val getCompanyExperienceService: GetCompanyExperienceService,
    private val updateCompanyExperienceService: UpdateCompanyExperienceService,
    private val deleteCompanyExperienceService: DeleteCompanyExperienceService,
) {

    // TODO : 로그인을 안해도 해당 API를 호출할 수 있도록 JWT Filter를 뚫어놔야 함(현재 안뚫려있음)
    @GetMapping("{memberId}/experience/company")
    @Operation(summary = "유저 회사 경력 조회", description = "회사 경력을 조회합니다. 로그인이 필요 없습니다.")
    fun getCompanyExperience(
        @PathVariable("memberId") memberUuid: String,
    ): ApiResponse<GetCompanyExperienceResponse> {
        val memberId = memberUuidConverter.toMemberId(memberUuid)

        return getCompanyExperienceService
            .getCompanyExperience(memberId)
            .let { GetCompanyExperienceResponse.from(it) }
            .let { ApiResponse.onSuccess(it) }
    }

    @PostMapping("{memberId}/experience/company")
    @Operation(summary = "유저 회사 경력 추가", description = "회사 경력을 추가합니다.")
    fun addCompanyExperience(
        @PathVariable("memberId") memberUuid: String,
        @AuthenticationPrincipal memberDetails: MemberDetails,
        @RequestBody request: AddCompanyExperienceRequest,
    ): ApiResponse<Unit> {
        if (memberUuid != memberDetails.username) {
            throw MemberException(ErrorStatus.MEMBER_NOT_MINE)
        }
        val memberId = memberUuidConverter.toMemberId(memberDetails.username)

        when (request.isCustom) {
            true -> addCompanyExperienceService.addCustomCompanyExperience(request.toCustomInput(memberId))
            false -> addCompanyExperienceService.addExistCompanyExperience(request.toExistInput(memberId))
        }
        return ApiResponse.onSuccess()
    }

    @PutMapping("{memberId}/experience/company")
    @Operation(summary = "유저 회사 경력 수정", description = "회사 경력을 수정합니다.")
    fun updateCompanyExperience(
        @PathVariable("memberId") memberUuid: String,
        @AuthenticationPrincipal memberDetails: MemberDetails,
        @RequestBody request: UpdateCompanyExperienceRequest,
    ): ApiResponse<Unit> {
        if (memberUuid != memberDetails.username) {
            throw MemberException(ErrorStatus.MEMBER_NOT_MINE)
        }
        val memberId = memberUuidConverter.toMemberId(memberDetails.username)
        when (request.isCustom) {
            true -> updateCompanyExperienceService.updateCustomCompanyExperience(request.toCustomInput(memberId))
            false -> updateCompanyExperienceService.updateExistCompanyExperience(request.toExistInput(memberId))
        }
        return ApiResponse.onSuccess()
    }

    @PutMapping("{memberId}/experience/company/is-view")
    @Operation(summary = "유저 회사 경력 공개 여부 수정", description = "회사 경력을 공개할지 여부를 수정합니다.")
    fun updateCompanyExperienceIsView(
        @PathVariable("memberId") memberUuid: String,
        @AuthenticationPrincipal memberDetails: MemberDetails,
        @RequestBody request: UpdateCompanyExperienceIsViewRequest,
    ): ApiResponse<Unit> {
        if (memberUuid != memberDetails.username) {
            throw MemberException(ErrorStatus.MEMBER_NOT_MINE)
        }
        val memberId = memberUuidConverter.toMemberId(memberDetails.username)
        when (request.isCustom) {
            true -> updateCompanyExperienceService.updateCustomCompanyExperienceIsView(request.toCustomInput(memberId))
            false -> updateCompanyExperienceService.updateExistCompanyExperienceIsView(request.toExistInput(memberId))
        }
        return ApiResponse.onSuccess()
    }

    @DeleteMapping("{memberId}/experience/company")
    @Operation(summary = "유저 회사 경력 삭제", description = "회사 경력을 삭제합니다.")
    fun deleteCompanyExperience(
        @PathVariable("memberId") memberUuid: String,
        @RequestParam("isCustom") isCustom: Boolean,
        @RequestParam("experienceId") experienceId: Long,
        @AuthenticationPrincipal memberDetails: MemberDetails,
    ): ApiResponse<Unit> {
        if (memberUuid != memberDetails.username) {
            throw MemberException(ErrorStatus.MEMBER_NOT_MINE)
        }
        val memberId = memberUuidConverter.toMemberId(memberDetails.username)
        when (isCustom) {
            true -> deleteCompanyExperienceService.deleteCustomCompanyExperience(memberId, experienceId)
            false -> deleteCompanyExperienceService.deleteExistCompanyExperience(memberId, experienceId)
        }
        return ApiResponse.onSuccess()
    }
}