package io.toasting.domain.message.applicatoin

import io.toasting.api.PageResponse
import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.exception.MemberExceptionHandler
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.message.applicatoin.input.CreateChatRoomInput
import io.toasting.domain.message.applicatoin.input.SendMessageInput
import io.toasting.domain.message.applicatoin.output.CreateChatRoomOutput
import io.toasting.domain.message.applicatoin.output.GetChatRoomListOutput
import io.toasting.domain.message.applicatoin.output.GetChatRoomMessagesOutput
import io.toasting.domain.message.applicatoin.output.GetMessageCountOutput
import io.toasting.domain.message.applicatoin.output.SendMessageOutput
import io.toasting.domain.message.entity.ChatMember
import io.toasting.domain.message.entity.ChatRoom
import io.toasting.domain.message.entity.Message
import io.toasting.domain.message.exception.MessageExceptionHandler.ChatMemberNotFoundException
import io.toasting.domain.message.exception.MessageExceptionHandler.ChatRoomAlreadyExistsException
import io.toasting.domain.message.exception.MessageExceptionHandler.ChatRoomNotFoundException
import io.toasting.domain.message.exception.MessageExceptionHandler.PartnerNotFoundException
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
    fun getUnreadMessageCount(memberId: Long): GetMessageCountOutput {
        val chatMemberList: MutableList<ChatMember> = chatMemberRepository.findByMemberId(memberId)
        val chatRoomList: MutableList<ChatRoom> = chatMemberList.map { it.chatRoom }.toMutableList()
        val unreadMessageCount =
            messageRepository.countByChatRoomInAndSenderIdNotAndIsRead(chatRoomList, memberId, false)
        return GetMessageCountOutput(unreadMessageCount)
    }

    @Transactional(readOnly = false)
    fun sendMessage(
        memberId: Long,
        chatRoomId: Long,
        input: SendMessageInput,
    ): SendMessageOutput {
        val chatRoom =
            chatRoomRepository
                .findById(chatRoomId)
                .orElseThrow { ChatRoomNotFoundException(ErrorStatus.CHAT_ROOM_NOT_FOUND) }
        chatMemberRepository
            .findByMemberIdAndChatRoomId(memberId, chatRoomId)
            .orElseThrow { ChatMemberNotFoundException(ErrorStatus.NOT_BELONG_TO_CHAT_ROOM) }

        var message = input.toMessageEntity(memberId, chatRoom)
        message = messageRepository.save(message)

        chatRoomRepository.save(input.toChatRoomEntity(memberId, chatRoom))

        val member = memberRepository.findById(memberId)
            .orElseThrow{ MemberExceptionHandler.MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND) }

        return SendMessageOutput.of(message, member.uuid)
    }

    @Transactional(readOnly = false)
    fun readAllMessage(
        memberId: Long,
        chatRoomId: Long,
    ) {
        val chatMember =
            chatMemberRepository
                .findByMemberIdAndChatRoomId(memberId, chatRoomId)
                .orElseThrow { ChatMemberNotFoundException(ErrorStatus.NOT_BELONG_TO_CHAT_ROOM) }
        val unreadMessageList: List<Message> =
            messageRepository.findByChatRoomAndSenderIdNotAndIsRead(chatMember.chatRoom, memberId, false)

        for (unreadMessage in unreadMessageList) {
            unreadMessage.read()
        }

        messageRepository.saveAll(unreadMessageList)
    }

    fun getChatRoomMessages(
        memberId: Long,
        chatRoomId: Long,
        pageable: Pageable,
    ): PageResponse<GetChatRoomMessagesOutput> {
        chatMemberRepository
            .findByMemberIdAndChatRoomId(memberId, chatRoomId)
            .orElseThrow { ChatMemberNotFoundException(ErrorStatus.NOT_BELONG_TO_CHAT_ROOM) }

        val chatRoom =
            chatRoomRepository
                .findById(chatRoomId)
                .orElseThrow { ChatRoomNotFoundException(ErrorStatus.NOT_BELONG_TO_CHAT_ROOM) }
        val messagePage = messageRepository.findByChatRoom(chatRoom, pageable)

        val senderIds = messagePage.content.map { it.senderId }

        val memberMapById = memberRepository.findAllById(senderIds).associateBy { it.id!! }

        val outputList = messagePage.content.map { message ->
            val sender = memberMapById[message.senderId]
                ?: throw MemberExceptionHandler.MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND)
            GetChatRoomMessagesOutput.fromEntity(message, sender.uuid)
        }

        return PageResponse.of(outputList, messagePage.totalElements, messagePage.totalPages)
    }

    fun getChatRooms(
        memberId: Long,
        pageable: Pageable,
    ): PageResponse<GetChatRoomListOutput> {
        val chatRoomPage = chatRoomRepository.findByMemberId(memberId, pageable)

        val outputList = mutableListOf<GetChatRoomListOutput>()
        for (chatRoom in chatRoomPage.content) {
            if (chatRoom.isActivated()) {
                val partnerId = getPartnerId(memberId, chatRoom)
                val partner =
                    memberRepository
                        .findById(partnerId)
                        .orElseThrow()

                val unreadMessageCount =
                    messageRepository.countByChatRoomAndSenderIdNotAndIsRead(chatRoom, memberId, false)
                val output =
                    GetChatRoomListOutput(
                        chatRoomId = chatRoom.id ?: throw ChatRoomNotFoundException(ErrorStatus.CHAT_ROOM_NOT_FOUND),
                        memberId = partner.uuid.toString(),
                        nickname = partner.nickname,
                        profilePicture = partner.profilePicture,
                        recentMessageContent = chatRoom.recentMessageContent!!,
                        recentSendAt = chatRoom.recentSendAt!!,
                        unreadMessageCount = unreadMessageCount.toInt(),
                    )
                outputList.add(output)
            }
        }

        return PageResponse.of(
            outputList,
            chatRoomPage.totalElements,
            chatRoomPage.totalPages,
        )
    }

    private fun getPartnerId(
        memberId: Long,
        chatRoom: ChatRoom,
    ): Long {
        val chatMemberList = chatMemberRepository.findByChatRoom(chatRoom)
        for (member in chatMemberList) {
            if (member.memberId != memberId) {
                return member.memberId
            }
        }

        throw PartnerNotFoundException(ErrorStatus.PARTNER_NOT_FOUND)
    }

    @Transactional(readOnly = false)
    fun createChatRoom(
        createChatRoomInput: CreateChatRoomInput,
    ): CreateChatRoomOutput {
        val myId = createChatRoomInput.myId
        val partnerId = createChatRoomInput.partnerId

        memberRepository
            .findById(createChatRoomInput.myId)
            .orElseThrow { MemberExceptionHandler.MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND) }

        memberRepository
            .findById(partnerId)
            .orElseThrow { MemberExceptionHandler.MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND) }

        val chatMemberList = chatMemberRepository.findByMemberId(myId)
        val chatRoomIdList = chatMemberList.map { it.chatRoom.id }
        val partnerChatMemberList = chatMemberRepository.findByChatRoomIdInAndMemberIdNot(chatRoomIdList, myId)
        val partnerChatRoom = partnerChatMemberList.find { it.memberId == partnerId }?.chatRoom
        if (partnerChatRoom != null) {
            if (partnerChatRoom.isActivated()) {
                throw ChatRoomAlreadyExistsException(ErrorStatus.CHAT_ROOM_ALREADY_EXISTS)
            }
            return CreateChatRoomOutput.from(partnerChatRoom)
        }

        val newChatRoom = createNewChatRoom(myId, partnerId)
        return CreateChatRoomOutput.from(newChatRoom)
    }

    private fun createNewChatRoom(
        myId: Long,
        partnerId: Long,
    ): ChatRoom {
        val chatRoom = chatRoomRepository.save(ChatRoom())
        val members =
            listOf(
                ChatMember(chatRoom = chatRoom, memberId = myId),
                ChatMember(chatRoom = chatRoom, memberId = partnerId),
            )
        chatMemberRepository.saveAll(members)
        return chatRoom
    }
}
