package io.toasting.domain.message.applicatoin

import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.message.applicatoin.`in`.SendMessageInput
import io.toasting.domain.message.applicatoin.out.GetMessageCountOutput
import io.toasting.domain.message.applicatoin.out.SendMessageOutput
import io.toasting.domain.message.entity.Message
import io.toasting.domain.message.repository.MessageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MessageService(
    private val messageRepository: MessageRepository,
    private val memberRepository: MemberRepository,
) {
    fun getUnreadMessageCount(memberDetails: MemberDetails): GetMessageCountOutput {
        val unreadMessageCount = messageRepository.countByReceiverIdAndIsRead(memberDetails.username.toLong(), false)
        return GetMessageCountOutput(unreadMessageCount)
    }

    @Transactional(readOnly = false)
    fun sendMessage(memberDetails: MemberDetails, input: SendMessageInput): SendMessageOutput {
        //TODO: 예외 처리
        val sender: Member = memberRepository.findById(memberDetails.username.toLong())
            .orElseThrow()
        val receiver: Member = memberRepository.findById(input.receiverId)
            .orElseThrow()

        var message: Message = input.toEntity(sender)
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