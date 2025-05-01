package io.toasting.domain.message.controller

import GetChatRoomListResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.toasting.api.PageResponse
import io.toasting.domain.member.application.converter.MemberUuidConverter
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.message.applicatoin.MessageService
import io.toasting.domain.message.controller.request.CreateChatRoomRequest
import io.toasting.domain.message.controller.request.SendMessageRequest
import io.toasting.domain.message.controller.response.CreateChatRoomResponse
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
@Tag(name = "Message", description = "채팅 관련 API")
class MessageController(
    private val messageService: MessageService,
    private val memberUuidConverter: MemberUuidConverter
) {
    @GetMapping("/messages/count")
    @Operation(summary = "읽지 않은 메세지 개수 조회", description = "전체 채팅방의 읽지 않은 메세지 개수를 조회한다.")
    fun getUnreadMessageCount(
        @AuthenticationPrincipal memberDetails: MemberDetails,
    ): ApiResponse<GetMessageCountResponse> {
        val memberId = memberUuidConverter.toMemberId(memberDetails.username)
        return ApiResponse.onSuccess(
            GetMessageCountResponse.fromOutput(
                messageService.getUnreadMessageCount(memberId)
            )
        )
    }

    @PostMapping("/{chatRoomId}/messages")
    @Operation(summary = "메세지 전송", description = "해당 채팅방에 메세지를 보낸다.")
    fun sendMessage(
        @AuthenticationPrincipal memberDetails: MemberDetails,
        @PathVariable("chatRoomId") chatRoomId: Long,
        @RequestBody @Valid request: SendMessageRequest,
    ): ApiResponse<SendMessageResponse> {
        val memberId = memberUuidConverter.toMemberId(memberDetails.username)

        return ApiResponse.onSuccess(
            SendMessageResponse.fromOutput(
                messageService.sendMessage(memberId, chatRoomId, request.toInput())
            )
        )
    }

    @GetMapping("/{chatRoomId}/messages")
    @Operation(summary = "채팅방 메세지 조회", description = "해당 채팅방의 메세지 리스트를 조회한다.")
    fun getChatRoomMessages(
        @AuthenticationPrincipal memberDetails: MemberDetails,
        @PathVariable("chatRoomId") chatRoomId: Long,
        @PageableDefault(size = 10, sort = ["id"], direction = Sort.Direction.DESC) pageable: Pageable,
    ): ApiResponse<PageResponse<GetChatRoomMessagesResponse>> {
        val memberId = memberUuidConverter.toMemberId(memberDetails.username)

        val output = messageService.getChatRoomMessages(memberId, chatRoomId, pageable)
        val responseList = output.content.map { GetChatRoomMessagesResponse.fromOutput(it) }
        return ApiResponse.onSuccess(
            PageResponse.of(responseList, output.totalElements, output.totalPages)
        )
    }

    @PutMapping("/{chatRoomId}/messages")
    @Operation(summary = "메세지 읽음 처리", description = "해당 채팅방의 모든 메세지를 읽음 처리한다.")
    fun readAllMessage(
        @AuthenticationPrincipal memberDetails: MemberDetails,
        @PathVariable("chatRoomId") chatRoomId: Long,
    ): ApiResponse<Unit> {
        val memberId = memberUuidConverter.toMemberId(memberDetails.username)
        messageService.readAllMessage(memberId, chatRoomId)
        return ApiResponse.onSuccess()
    }

    @GetMapping("/messages")
    @Operation(summary = "채팅방 리스트 조회", description = "참여 중인 채팅방 리스트를 조회한다. 최근에 메시지 보내거나 받은 순으로 정렬한다.")
    fun getChatRooms(
        @AuthenticationPrincipal memberDetails: MemberDetails,
        @PageableDefault(size = 10) pageable: Pageable,
    ): ApiResponse<PageResponse<GetChatRoomListResponse>> {
        val memberId = memberUuidConverter.toMemberId(memberDetails.username)
        val output = messageService.getChatRooms(memberId, pageable)
        val response = output.content.map { GetChatRoomListResponse.from(it) }
        return ApiResponse.onSuccess(
            PageResponse.of(
                response,
                output.totalElements,
                output.totalPages
            )
        )
    }

    @PostMapping("")
    @Operation(summary = "채팅방 생성", description = "나와 상대방의 채팅방을 생성합니다.")
    fun createChatRoom(
        @AuthenticationPrincipal memberDetails: MemberDetails,
        @RequestBody @Valid request: CreateChatRoomRequest,
    ): ApiResponse<CreateChatRoomResponse> {
        val myId = memberUuidConverter.toMemberId(memberDetails.username)
        val output = messageService.createChatRoom(request.toInput(myId))
        val response = CreateChatRoomResponse.from(output)

        return ApiResponse.onSuccess(
            response
        )
    }
}
