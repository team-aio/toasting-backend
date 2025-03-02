package io.toasting.domain.message.applicatoin

import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.message.applicatoin.out.GetMessageCountOutput
import io.toasting.domain.message.repository.MessageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MessageService(
    private val messageRepository: MessageRepository
) {
    fun getUnreadMessageCount(memberDetails: MemberDetails): GetMessageCountOutput {
        val unreadMessageCount = messageRepository.countByReceiverIdAndIsRead(memberDetails.username.toLong(), false)
        return GetMessageCountOutput(unreadMessageCount)
    }

}