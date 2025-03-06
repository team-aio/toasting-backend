package io.toasting.domain.message.applicatoin

import io.toasting.api.PageResponse
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.message.applicatoin.`in`.SendMessageInput
import io.toasting.domain.message.applicatoin.out.GetChatRoomListOutput
import io.toasting.domain.message.applicatoin.out.GetChatRoomMessagesOutput
import io.toasting.domain.message.applicatoin.out.GetMessageCountOutput
import io.toasting.domain.message.applicatoin.out.SendMessageOutput
import io.toasting.domain.message.entity.ChatMember
import io.toasting.domain.message.entity.ChatRoom
import io.toasting.domain.message.entity.Message
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
        val member = memberRepository.findById(memberId) //TODO: NOT_FOUND_MEMBER 예외 처리
            .orElseThrow()
        var chatRoom = chatRoomRepository.findById(chatRoomId) //TODO: NOT_FOUND_CHAT_ROOM 예외 처리
            .orElseThrow()
        val chatMember = chatMemberRepository.findByMemberIdAndChatRoomId(memberId, chatRoomId) //TODO : NOT_BELONGS_TO_CHAT_ROOM 예외 처리
            .orElseThrow()

        var message: Message = input.toMessageEntity(memberId, chatRoom)
        message = messageRepository.save(message)

        chatRoom = input.toChatRoomEntity(memberId, chatRoom)
        chatRoom = chatRoomRepository.save(chatRoom)

        return SendMessageOutput.fromEntity(message)
    }

    @Transactional(readOnly = false)
    fun readAllMessage(memberDetails: MemberDetails, chatRoomId: Long) {
        val memberId = memberDetails.username.toLong()
        val chatMember = chatMemberRepository.findByMemberIdAndChatRoomId(memberId, chatRoomId)
            .orElseThrow() //TODO: NOT_BELONGS_TO_CHAT_ROOM 예외 처리
        val unreadMessageList: List<Message> = messageRepository.findByChatRoomAndSenderIdNotAndIsRead(chatMember.chatRoom, memberId, false)

        for (unreadMessage in unreadMessageList) {
            unreadMessage.read()
        }

        messageRepository.saveAll(unreadMessageList)
    }

    fun getChatRoomMessages(memberDetails: MemberDetails, chatRoomId: Long, pageable: Pageable): PageResponse<GetChatRoomMessagesOutput> {
        val memberId = memberDetails.username.toLong()
        val chatMember = chatMemberRepository.findByMemberIdAndChatRoomId(memberId, chatRoomId)
            .orElseThrow() //TODO: NOT_BELONGS_TO_CHAT_ROOM 예외 처리

        val chatRoom = chatRoomRepository.findById(chatRoomId)
            .orElseThrow() //TODO: NOT_FOUND_CHAT_ROOM 예외 처리
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

        return -1
    }

}