package io.toasting.domain.message.controller

import GetChatRoomListResponse
import io.toasting.api.PageResponse
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.message.applicatoin.MessageService
import io.toasting.domain.message.controller.request.SendMessageRequest
import io.toasting.domain.message.controller.response.GetChatRoomMessagesResponse
import io.toasting.domain.message.controller.response.GetMessageCountResponse
import io.toasting.domain.message.controller.response.SendMessageResponse
import io.toasting.global.api.ApiResponse
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
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

    @GetMapping("/{chatRoomId}/messages")
    fun getChatRoomMessages(
        @AuthenticationPrincipal memberDetails: MemberDetails,
        @PathVariable("chatRoomId") chatRoomId: Long,
        @PageableDefault(size = 10, sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable,
    ): ApiResponse<PageResponse<GetChatRoomMessagesResponse>> {
        val output = messageService.getChatRoomMessages(memberDetails, chatRoomId, pageable)
        val responseList = output.content.map {GetChatRoomMessagesResponse.fromOutput(it) }
        return ApiResponse.onSuccess(
            PageResponse.of(responseList, output.totalElements, output.totalPages)
        )
    }

    @PutMapping("/{chatRoomId}/messages")
    fun readAllMessage(
        @AuthenticationPrincipal memberDetails: MemberDetails,
        @PathVariable("chatRoomId") chatRoomId: Long,
    ): ApiResponse<Unit> {
        messageService.readAllMessage(memberDetails, chatRoomId)
        return ApiResponse.onSuccess()
    }

    @GetMapping("/messages")
    fun getChatRooms(
        @AuthenticationPrincipal memberDetails: MemberDetails,
        @PageableDefault(size = 10) pageable: Pageable,
    ): ApiResponse<PageResponse<GetChatRoomListResponse>> {
        val output = messageService.getChatRooms(memberDetails, pageable)
        val response = output.content.map { GetChatRoomListResponse.from(it) }
        return ApiResponse.onSuccess(
            PageResponse.of(
                response,
                output.totalElements,
                output.totalPages
            )
        )
    }
}
