package io.toasting.domain.message.application

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.toasting.creator.MessageCreator
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.member.vo.RoleType
import io.toasting.domain.message.applicatoin.MessageService
import io.toasting.domain.message.applicatoin.`in`.SendMessageInput
import io.toasting.domain.message.applicatoin.out.SendMessageOutput
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
    private lateinit var memberRepository: MemberRepository
    @Autowired
    private lateinit var messageRepository: MessageRepository
    @Autowired
    private lateinit var messageService: MessageService

    init {
        afterTest {
            messageRepository.deleteAll()
            memberRepository.deleteAll()
        }

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

        Given("sender와 receiver가 주어지고,") {
            val sender = Member.defaultMember("sender", "sender@test.com")
            val receiver = Member.defaultMember("receiver", "receiver@test.com")
            memberRepository.saveAll(listOf(sender, receiver))

            val memberDetail = MemberDetails.from(sender)
            val input = SendMessageInput(2, 3, "test")

            When("sender가 receiver에게 'test'라는 메세지를 보내면,") {
                val result: SendMessageOutput = messageService.sendMessage(memberDetail, input)

                Then("메세지가 저장되고, 응답으로 메세지 정보가 반환된다.") {

                    val messageList: MutableList<Message> = messageRepository.findAll();
                    messageList.size shouldBe 1

                    result.id shouldNotBe null
                    result.receiverId shouldBe 2
                    result.postId shouldBe 3
                    result.content shouldBe "test"
                    result.createdAt shouldNotBe null
                }
            }
        }

        Given("id가 2인 상대방이 메세지를 10개 보내고,") {
            val memberDetails = MemberDetails(RoleType.ROLE_USER.name, 1)
            val partnerId: Long = 2
            val messageList: MutableList<Message> = mutableListOf()
            for (i in 0 until 10) {
                messageList.add(MessageCreator.defaultMessage("test", partnerId, memberDetails.username.toLong(), false))
            }
            When("채팅방에 들어가서 메세지를 모두 읽으면,") {
                messageService.readAllMessage(memberDetails, partnerId)
                Then("메세지륾 모두 읽음 처리 한다.") {
                    val messageList = messageRepository.findBySenderIdAndReceiverIdAndIsRead(partnerId, memberDetails.username.toLong(), false)

                    messageList.size shouldBe 0
                }
            }
        }
    }

}