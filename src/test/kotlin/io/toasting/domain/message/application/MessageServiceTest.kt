package io.toasting.domain.message.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.date.shouldBeAfter
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.toasting.creator.ChatRoomCreator
import io.toasting.creator.MessageCreator
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.message.applicatoin.MessageService
import io.toasting.domain.message.applicatoin.input.CreateChatRoomInput
import io.toasting.domain.message.applicatoin.input.SendMessageInput
import io.toasting.domain.message.entity.ChatMember
import io.toasting.domain.message.entity.ChatRoom
import io.toasting.domain.message.entity.Message
import io.toasting.domain.message.exception.MessageExceptionHandler
import io.toasting.domain.message.repository.ChatMemberRepository
import io.toasting.domain.message.repository.ChatRoomRepository
import io.toasting.domain.message.repository.MessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MessageServiceTest :
    BehaviorSpec({
    }) {
    override fun extensions() = listOf(SpringTestExtension(SpringTestLifecycleMode.Root))

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var messageRepository: MessageRepository

    @Autowired
    private lateinit var chatRoomRepository: ChatRoomRepository

    @Autowired
    private lateinit var chatMemberRepository: ChatMemberRepository

    @Autowired
    private lateinit var messageService: MessageService

    init {

        Given("member1과 member2의 채팅방, member1과 member3의 채팅방, member2가 메세지 10개, member3이 메시지 5개 보낸 것이 주어지고,") {
            val member1 = Member.defaultMember("member1", "member1@test.com")
            val member2 = Member.defaultMember("member2", "member2@test.com")
            val member3 = Member.defaultMember("member3", "member3@test.com")
            memberRepository.saveAll(mutableListOf(member1, member2, member3))

            val chatRoom1 = ChatRoom()
            val chatRoom2 = ChatRoom()
            chatRoomRepository.saveAll(mutableListOf(chatRoom1, chatRoom2))

            val chatMember1With2 = ChatMember(null, chatRoom1, member1.id!!)
            val chatMember2 = ChatMember(null, chatRoom1, member2.id!!)
            val chatMember1With3 = ChatMember(null, chatRoom2, member1.id!!)
            val chatMember3 = ChatMember(null, chatRoom2, member3.id!!)
            chatMemberRepository.saveAll(mutableListOf(chatMember1With2, chatMember2, chatMember1With3, chatMember3))

            val messageList: MutableList<Message> = mutableListOf()
            for (i in 0 until 10) {
                val message = MessageCreator.readMessage("read message", member2.id!!, chatRoom1)
                messageList.add(message)
            }
            for (i in 0 until 10) {
                val message = MessageCreator.unreadMessage("2->1", member2.id!!, chatRoom1)
                messageList.add(message)
            }
            for (i in 0 until 5) {
                val message = MessageCreator.unreadMessage("3->1", member3.id!!, chatRoom2)
                messageList.add(message)
            }
            for (i in 0 until 5) {
                val message = MessageCreator.unreadMessage("1->2", member1.id!!, chatRoom1)
                messageList.add(message)
            }
            messageRepository.saveAll(messageList)

            When("member1이 읽지 않은 메세지 개수를 조회했을 때") {
                val memberDetails = MemberDetails.from(member1)
                val result = messageService.getUnreadMessageCount(memberDetails)

                Then("15가 반환되어야 한다.") {
                    result.count shouldBe 15
                }
            }
        }

        Given("member1과 member2의 채팅방이 주어지고,") {
            val member1 = Member.defaultMember("member1", "member1@test.com")
            val member2 = Member.defaultMember("member2", "member2@test.com")
            memberRepository.saveAll(mutableListOf(member1, member2))

            val chatRoom = ChatRoom()
            chatRoomRepository.save(chatRoom)

            val chatMember1 = ChatMember(null, chatRoom, member1.id!!)
            val chatMember2 = ChatMember(null, chatRoom, member2.id!!)
            chatMemberRepository.saveAll(mutableListOf(chatMember1, chatMember2))

            When("member2가 member1에게 메세지를 보냈을 때,") {
                val memberDetails = MemberDetails.from(member2)
                val input = SendMessageInput("2->1")
                val result = messageService.sendMessage(memberDetails, chatRoom.id!!, input)

                Then("메세지가 저장, 채팅방의 recent 값들이 바뀌고, 메시지의 정보가 반환된다.") {
                    val messageList = messageRepository.findAll()
                    val message = messageList.first()

                    messageList.size shouldBe 1
                    message.id shouldBe result.id
                    message.chatRoom.id shouldBe result.chatRoomId
                    message.senderId shouldBe result.senderId
                    message.content shouldBe input.content

                    val chatRoom = chatRoomRepository.findAll().first()
                    chatRoom.recentMessageContent shouldBe input.content
                    chatRoom.recentSenderId shouldBe member2.id
                }
            }
        }

        Given("member2와 member1의 채팅방과 member1이 읽지 않은 메세지가 10개 주어졌고,") {
            val member1 = Member.defaultMember("member1", "member1@test.com")
            val member2 = Member.defaultMember("member2", "member2@test.com")
            memberRepository.saveAll(mutableListOf(member1, member2))

            val chatRoom = ChatRoom()
            chatRoomRepository.save(chatRoom)

            val chatMember1 = ChatMember(null, chatRoom, member1.id!!)
            val chatMember2 = ChatMember(null, chatRoom, member2.id!!)
            chatMemberRepository.saveAll(mutableListOf(chatMember1, chatMember2))

            val messageList: MutableList<Message> = mutableListOf()
            for (i in 0 until 10) {
                val message = MessageCreator.unreadMessage("unread message", member2.id!!, chatRoom)
                messageList.add(message)
            }
            messageRepository.saveAll(messageList)

            When("member1이 해당 채팅방의 메세지를 모두 읽을 때,") {
                val memberDetails = MemberDetails.from(member1)
                val chatRoomId = chatRoom.id!!
                messageService.readAllMessage(memberDetails, chatRoomId)
                Then("읽지 않은 메세지의 개수가 0이 된다.") {
                    val messageList =
                        messageRepository.findByChatRoomAndSenderIdNotAndIsRead(chatRoom, member1.id!!, false)

                    messageList.size shouldBe 0
                }
            }

            When("member1이 해당 채팅방의 메세지 리스트롤 4개씩 0번 페이지를 조회하면,") {
                val memberDetails = MemberDetails.from(member1)
                val chatRoomId = chatRoom.id!!
                val page = PageRequest.of(0, 4, Sort.by(Sort.Direction.DESC, "id"))
                val result = messageService.getChatRoomMessages(memberDetails, chatRoomId, page)

                Then("데이터의 개수는 4, 페이지 개수는 3, 총 개수는 10이 된다.") {
                    result.content.size shouldBe 4
                    result.content.first().chatRoomId shouldBe chatRoomId
                    result.content.first().senderId shouldBe member2.id
                    result.content.get(0).id shouldBeGreaterThan result.content.get(1).id
                    result.totalPages shouldBe 3
                    result.totalElements shouldBe 10
                }
            }
        }

        Given("member1과 member2의 채팅방, member1과 member3의 채팅방, member1과 member4의 채팅방이 주어졌고,") {
            val member1 = Member.defaultMember("member1", "member1@test.com")
            val member2 = Member.defaultMember("member2", "member2@test.com")
            val member3 = Member.defaultMember("member3", "member3@test.com")
            val member4 = Member.defaultMember("member4", "member4@test.com")
            memberRepository.saveAll(mutableListOf(member1, member2, member3, member4))

            val chatRoom1With2 =
                ChatRoomCreator.activatedChatRoom(member2.id!!, "chatRoom1", LocalDateTime.of(2025, 1, 1, 0, 1, 10))
            val chatRoom1With3 =
                ChatRoomCreator.activatedChatRoom(member3.id!!, "chatRoom2", LocalDateTime.of(2025, 1, 1, 0, 1, 9))
            val chatRoom1With4 =
                ChatRoomCreator.activatedChatRoom(member4.id!!, "chatRoom3", LocalDateTime.of(2025, 1, 1, 0, 1, 8))
            chatRoomRepository.saveAll(mutableListOf(chatRoom1With2, chatRoom1With3, chatRoom1With4))

            val chatMember1 = ChatMember(null, chatRoom1With2, member1.id!!)
            val chatMember2 = ChatMember(null, chatRoom1With2, member2.id!!)
            val chatMember3 = ChatMember(null, chatRoom1With3, member1.id!!)
            val chatMember4 = ChatMember(null, chatRoom1With3, member3.id!!)
            val chatMember5 = ChatMember(null, chatRoom1With4, member1.id!!)
            val chatMember6 = ChatMember(null, chatRoom1With4, member4.id!!)
            chatMemberRepository.saveAll(
                mutableListOf(
                    chatMember1,
                    chatMember2,
                    chatMember3,
                    chatMember4,
                    chatMember5,
                    chatMember6,
                ),
            )

            val messageList: MutableList<Message> = mutableListOf()
            for (i in 0 until 5) {
                val message1 = MessageCreator.unreadMessage("message1", member2.id!!, chatRoom1With2)
                val message2 = MessageCreator.unreadMessage("message2", member3.id!!, chatRoom1With3)
                messageList.addAll(mutableListOf(message1, message2))
            }
            for (i in 0 until 3) {
                val message1 = MessageCreator.unreadMessage("message1", member2.id!!, chatRoom1With2)
                messageList.add(message1)
            }
            messageRepository.saveAll(messageList)

            When("member1이 채팅방 리스트를 조회했을 때") {
                val memberDetails = MemberDetails.from(member1)
                val pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "recentSendAt"))
                val chatRoomPage = messageService.getChatRooms(memberDetails, pageRequest)

                val firstChatRoom = chatRoomPage.content.get(0)
                val secondChatRoom = chatRoomPage.content.get(1)
                Then("가장 최근에 메세지가 보내진 순서대로 채팅방 리스트가 조회된다.") {
                    firstChatRoom.recentSendAt shouldBeAfter secondChatRoom.recentSendAt
                }

                Then("chatRoom1의 recentMessageContent는 chatRoom1이고, 읽지 않은 메세지 개수는 8개다.") {
                    firstChatRoom.recentMessageContent shouldBe "chatRoom1"
                    firstChatRoom.unreadMessageCount shouldBe 8
                }

                Then("chatRoom2의 recentMessageContent는 chatRoom2이고, 읽지 않은 메세지 개수는 5개다.") {
                    secondChatRoom.recentMessageContent shouldBe "chatRoom2"
                    secondChatRoom.unreadMessageCount shouldBe 5
                }
            }
        }

        Given("member1, member2, member3과 member2과 member3의 채팅방이 있고,") {
            val member1 = Member.defaultMember("member1", "member1@test.com")
            val member2 = Member.defaultMember("member2", "member2@test.com")
            val member3 = Member.defaultMember("member3", "member3@test.com")
            memberRepository.saveAll(mutableListOf(member1, member2, member3))

            var notActivatedChatRoom = ChatRoomCreator.notActivatedChatRoom()
            notActivatedChatRoom = chatRoomRepository.save(notActivatedChatRoom)
            val chatMember2With3 = ChatMember(chatRoom = notActivatedChatRoom, memberId = member2.id!!)
            val chatMember3With2 = ChatMember(chatRoom = notActivatedChatRoom, memberId = member3.id!!)
            chatMemberRepository.saveAll(mutableListOf(chatMember2With3, chatMember3With2))

            var activatedChatRoom =
                ChatRoomCreator.activatedChatRoom(
                    recentSenderId = member1.id!!,
                    recentMessage = "recentMessage",
                    recentSendAt = LocalDateTime.now(),
                )
            activatedChatRoom = chatRoomRepository.save(activatedChatRoom)
            val chatMember1With3 = ChatMember(chatRoom = activatedChatRoom, memberId = member1.id!!)
            val chatMember3With1 = ChatMember(chatRoom = activatedChatRoom, memberId = member3.id!!)
            chatMemberRepository.saveAll(mutableListOf(chatMember1With3, chatMember3With1))

            When("member1이 member2와의 채팅방을 생성하면") {
                val memberDetails = MemberDetails.from(member1)
                val input = CreateChatRoomInput(member2.id!!)

                val output = messageService.createChatRoom(memberDetails, input)
                Then("새로운 채팅방이 생성되고, 그 채팅방의 chatMemeber는 member1과 member2이다.") {
                    val outputChatRoom =
                        chatRoomRepository
                            .findById(output.chatRoomId)
                            .orElse(null)
                    outputChatRoom shouldNotBe null

                    val chatMemberList = chatMemberRepository.findByChatRoom(outputChatRoom)
                    val memberIdList = chatMemberList.map { it.memberId }
                    memberIdList shouldContain member1.id
                    memberIdList shouldContain member2.id
                }
            }

            When("member2가 member3과의 채팅방을 생성하면") {
                val memberDetails = MemberDetails.from(member2)
                val input = CreateChatRoomInput(member3.id!!)

                val output = messageService.createChatRoom(memberDetails, input)
                Then("기존에 생성되어 있던 활성화되지 않은 채팅방이 응답된다.") {
                    notActivatedChatRoom.id shouldBe output.chatRoomId
                }
            }

            When("member1이 member3과의 채팅방을 생성하면") {
                val memberDetails = MemberDetails.from(member1)
                val input = CreateChatRoomInput(member3.id!!)
                Then("ChatRoomAlreadyExistsException을 던진다.") {
                    shouldThrow<MessageExceptionHandler.ChatRoomAlreadyExistsException> {
                        messageService.createChatRoom(memberDetails, input)
                    }
                }
            }
        }
    }
}
