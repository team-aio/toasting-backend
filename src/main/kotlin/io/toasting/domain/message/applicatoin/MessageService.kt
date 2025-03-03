package io.toasting.domain.message.applicatoin

import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.message.applicatoin.`in`.SendMessageInput
import io.toasting.domain.message.applicatoin.out.GetMessageCountOutput
import io.toasting.domain.message.applicatoin.out.SendMessageOutput
import io.toasting.domain.message.entity.ChatMember
import io.toasting.domain.message.entity.ChatRoom
import io.toasting.domain.message.entity.Message
import io.toasting.domain.message.repository.ChatMemberRepository
import io.toasting.domain.message.repository.ChatRoomRepository
import io.toasting.domain.message.repository.MessageRepository
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
        val chatMemberList: MutableList<ChatMember> = chatMemberRepository.findByMemberId(memberDetails.username.toLong())
        val chatRoomList: MutableList<ChatRoom> = chatMemberList.map { it.chatRoom }.toMutableList()
        val unreadMessageCount: Long = messageRepository.countByChatRoomInAndIsRead(chatRoomList, false)
        return GetMessageCountOutput(unreadMessageCount)
    }

    @Transactional(readOnly = false)
    fun sendMessage(memberDetails: MemberDetails, chatRoomId: Long, input: SendMessageInput): SendMessageOutput {
        val memberId = memberDetails.username.toLong()
        val member = memberRepository.findById(memberId) //TODO: NOT_FOUND_MEMBER 예외 처리
            .orElseThrow()
        val chatRoom = chatRoomRepository.findById(chatRoomId) //TODO: NOT_FOUND_CHAT_ROOM 예외 처리
            .orElseThrow()
        val chatMember = chatMemberRepository.findByMemberIdAndChatRoomId(memberId, chatRoomId) //TODO : NOT_BELONGS_TO_CHAT_ROOM 예외 처리
            .orElseThrow()

        var message: Message = input.toEntity(memberId, chatRoom)
        message = messageRepository.save(message)

        return SendMessageOutput.fromEntity(message)
    }

    @Transactional(readOnly = false)
    fun readAllMessage(memberDetails: MemberDetails, partnerId: Long) {
        val unreadMessageList: List<Message> = messageRepository.findBySenderIdAndReceiverIdAndIsRead(partnerId, memberDetails.username.toLong(), false)

        for (unreadMessage in unreadMessageList) {
            unreadMessage.read()
        }

        messageRepository.saveAll(unreadMessageList)
    }

}