package io.toasting.domain.message.applicatoin

import io.toasting.api.PageResponse
import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.member.exception.MemberExceptionHandler
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.message.applicatoin.input.CreateChatRoomInput
import io.toasting.domain.message.applicatoin.input.SendMessageInput
import io.toasting.domain.message.applicatoin.output.*
import io.toasting.domain.message.entity.ChatMember
import io.toasting.domain.message.entity.ChatRoom
import io.toasting.domain.message.entity.Message
import io.toasting.domain.message.exception.MessageExceptionHandler
import io.toasting.domain.message.repository.ChatMemberRepository
import io.toasting.domain.message.repository.ChatRoomRepository
import io.toasting.domain.message.repository.MessageRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MessageService(
    private val chatRoomRepository: ChatRoomRepository,
    private val chatMemberRepository: ChatMemberRepository,
    private val memberRepository: MemberRepository,
    private val messageRepository: MessageRepository,
) {
    fun getUnreadMessageCount(memberDetails: MemberDetails): GetMessageCountOutput {
        val memberId = memberDetails.username.toLong()
        val chatMemberList: MutableList<ChatMember> = chatMemberRepository.findByMemberId(memberId)
        val chatRoomList: MutableList<ChatRoom> = chatMemberList.map { it.chatRoom }.toMutableList()
        val unreadMessageCount = messageRepository.countByChatRoomInAndSenderIdNotAndIsRead(chatRoomList, memberId, false)
        return GetMessageCountOutput(unreadMessageCount)
    }

    @Transactional(readOnly = false)
    fun sendMessage(memberDetails: MemberDetails, chatRoomId: Long, input: SendMessageInput): SendMessageOutput {
        val memberId = memberDetails.username.toLong()
        val chatRoom = chatRoomRepository.findById(chatRoomId)
            .orElseThrow { MessageExceptionHandler.ChatRoomNotFoundException(ErrorStatus.CHAT_ROOM_NOT_FOUND) }
        chatMemberRepository.findByMemberIdAndChatRoomId(memberId, chatRoomId)
            .orElseThrow { MessageExceptionHandler.ChatMemberNotFoundException(ErrorStatus.NOT_BELONG_TO_CHAT_ROOM) }

        var message: Message = input.toMessageEntity(memberId, chatRoom)
        message = messageRepository.save(message)

        chatRoomRepository.save(input.toChatRoomEntity(memberId, chatRoom))

        return SendMessageOutput.fromEntity(message)
    }

    @Transactional(readOnly = false)
    fun readAllMessage(memberDetails: MemberDetails, chatRoomId: Long) {
        val memberId = memberDetails.username.toLong()
        val chatMember = chatMemberRepository.findByMemberIdAndChatRoomId(memberId, chatRoomId)
            .orElseThrow{ MessageExceptionHandler.ChatMemberNotFoundException(ErrorStatus.NOT_BELONG_TO_CHAT_ROOM) }
        val unreadMessageList: List<Message> = messageRepository.findByChatRoomAndSenderIdNotAndIsRead(chatMember.chatRoom, memberId, false)

        for (unreadMessage in unreadMessageList) {
            unreadMessage.read()
        }

        messageRepository.saveAll(unreadMessageList)
    }

    fun getChatRoomMessages(memberDetails: MemberDetails, chatRoomId: Long, pageable: Pageable): PageResponse<GetChatRoomMessagesOutput> {
        val memberId = memberDetails.username.toLong()
        chatMemberRepository.findByMemberIdAndChatRoomId(memberId, chatRoomId)
            .orElseThrow{ MessageExceptionHandler.ChatMemberNotFoundException(ErrorStatus.NOT_BELONG_TO_CHAT_ROOM) }

        val chatRoom = chatRoomRepository.findById(chatRoomId)
            .orElseThrow{ MessageExceptionHandler.ChatRoomNotFoundException(ErrorStatus.NOT_BELONG_TO_CHAT_ROOM) }
        val messagePage = messageRepository.findByChatRoom(chatRoom, pageable)

        val outputList = messagePage.content.map { GetChatRoomMessagesOutput.fromEntity(it) }

        return PageResponse.of(outputList, messagePage.totalElements, messagePage.totalPages)
    }

    fun getChatRooms(memberDetails: MemberDetails, pageable: Pageable): PageResponse<GetChatRoomListOutput> {
        val memberId = memberDetails.username.toLong()
        val chatRoomPage = chatRoomRepository.findByMemberId(memberId, pageable)

        val outputList = mutableListOf<GetChatRoomListOutput>()
        for (chatRoom in chatRoomPage.content) {
            if (chatRoom.isActivated()) {
                val partnerId = getPartnerId(memberId, chatRoom)
                val partner = memberRepository.findById(partnerId)
                    .orElseThrow()

                val unreadMessageCount = messageRepository.countByChatRoomAndSenderIdNotAndIsRead(chatRoom, memberId, false)
                val output = GetChatRoomListOutput(
                    memberId = partnerId,
                    profilePicture = partner.profilePicture,
                    recentMessageContent = chatRoom.recentMessageContent!!,
                    recentSendAt = chatRoom.recentSendAt!!,
                    unreadMessageCount.toInt()
                )
                outputList.add(output)
            }
        }

        return PageResponse.of(
            outputList,
            chatRoomPage.totalElements,
            chatRoomPage.totalPages
        )
    }

    private fun getPartnerId(memberId:Long, chatRoom: ChatRoom): Long {
        val chatMemberList = chatMemberRepository.findByChatRoom(chatRoom)
        for (member in chatMemberList) {
            if (member.memberId != memberId) {
                return member.memberId
            }
        }

        throw MessageExceptionHandler.PartnerNotFoundException(ErrorStatus.PARTNER_NOT_FOUND)
    }

    @Transactional(readOnly = false)
    fun createChatRoom(memberDetails: MemberDetails, request: CreateChatRoomInput): CreateChatRoomOutput {
        val myId = memberDetails.username.toLong()
        memberRepository.findById(memberDetails.username.toLong())
            .orElseThrow{ MemberExceptionHandler.MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND) }
        val partnerId = request.partnerId
        memberRepository.findById(partnerId)
            .orElseThrow{ MemberExceptionHandler.MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND) }

        val chatMemberList = chatMemberRepository.findByMemberId(myId)
        val chatRoomIdList = chatMemberList.map { it.chatRoom.id }
        val partnerChatMemberList = chatMemberRepository.findByChatRoomIdInAndMemberIdNot(chatRoomIdList, myId)
        val partnerChatRoom = partnerChatMemberList.find { it.chatRoom.id == partnerId }?.chatRoom
        if (partnerChatRoom != null) {
            if (partnerChatRoom.isActivated()) {
                throw MessageExceptionHandler.ChatRoomAlreadyExistsException(ErrorStatus.CHAT_ROOM_ALREADY_EXISTS)
            }
            return CreateChatRoomOutput.from(partnerChatRoom)
        }

        val newChatRoom = createNewChatRoom(myId, partnerId)
        return CreateChatRoomOutput.from(newChatRoom)
    }

    private fun createNewChatRoom(myId: Long, partnerId: Long): ChatRoom {
        val chatRoom = chatRoomRepository.save(ChatRoom())
        val members = listOf(
            ChatMember(chatRoom = chatRoom, memberId = myId),
            ChatMember(chatRoom = chatRoom, memberId = partnerId)
        )
        chatMemberRepository.saveAll(members)
        return chatRoom
    }
}