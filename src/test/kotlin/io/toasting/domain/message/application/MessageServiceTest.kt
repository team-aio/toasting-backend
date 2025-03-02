package io.toasting.domain.message.application

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.toasting.creator.MessageCreator
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.member.vo.RoleType
import io.toasting.domain.message.applicatoin.MessageService
import io.toasting.domain.message.entity.Message
import io.toasting.domain.message.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class MessageServiceTest private constructor() : BehaviorSpec() {
    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var messageRepository: MessageRepository

    @Autowired
    private lateinit var messageService: MessageService

    init {
        Given("member가 읽지 않은 메세지가 10개라면,") {
            val memberDetails = MemberDetails(RoleType.ROLE_USER.name, 1)
            val messageList: MutableList<Message> = mutableListOf()
            for (i in 0 until 10) {
                messageList.add(MessageCreator.defaultMessage("test", (i + 2).toLong(), memberDetails.username.toLong(), false))
            }
            for (i in 11 until 20) {
                messageList.add(MessageCreator.defaultMessage("test", (i + 2).toLong(), memberDetails.username.toLong(), true))
            }
            messageRepository.saveAllAndFlush(messageList)

            When("읽지 않은 메세지 개수를 조회했을 때") {
                val result = messageService.getUnreadMessageCount(memberDetails);

                Then("10이 반환되어야 한다.") {
                    result.count shouldBe 10
                }
            }
        }
    }

}