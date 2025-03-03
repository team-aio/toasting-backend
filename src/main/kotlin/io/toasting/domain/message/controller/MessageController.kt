package io.toasting.domain.message.controller

import GetMessagesResponse
import io.toasting.api.PageResponse
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.message.applicatoin.MessageService
import io.toasting.domain.message.controller.request.SendMessageRequest
import io.toasting.domain.message.controller.response.GetMessageCountResponse
import io.toasting.domain.message.controller.response.SendMessageResponse
import io.toasting.global.api.ApiResponse
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/chat-rooms")
class MessageController (
    private val messageService : MessageService,
){
    @GetMapping("/messages/count")
    fun getUnreadMessageCount(
        @AuthenticationPrincipal memberDetails: MemberDetails,
    ): ApiResponse<GetMessageCountResponse> {
        return ApiResponse.onSuccess(
            GetMessageCountResponse.fromOutput(
                messageService.getUnreadMessageCount(memberDetails)
            )
        )
    }

    @PostMapping("/{chatRoomId}/messages")
    fun sendMessage(
        @AuthenticationPrincipal memberDetails: MemberDetails,
        @PathVariable("chatRoomId") chatRoomId: Long,
        @RequestBody @Valid request: SendMessageRequest,
    ): ApiResponse<SendMessageResponse> {
        return ApiResponse.onSuccess(
            SendMessageResponse.fromOutput(
                messageService.sendMessage(memberDetails, chatRoomId, request.toInput())
            )
        )
    }

    @GetMapping
    fun getMessages(
        @PageableDefault(page = 0, size = 10) pageable: Pageable,
    ): ApiResponse<PageResponse<GetMessagesResponse>> {
        val content = List(10) { GetMessagesResponse.mock() }
        return ApiResponse.onSuccess(PageResponse.of(content, 10, 100))
    }

    @PutMapping("/{chatRoomId}/messages")
    fun readAllMessage(
        @AuthenticationPrincipal memberDetails: MemberDetails,
        @PathVariable("chatRoomId") chatRoomId: Long,
    ): ApiResponse<Unit> {
        messageService.readAllMessage(memberDetails, chatRoomId)
        return ApiResponse.onSuccess()
    }
}
