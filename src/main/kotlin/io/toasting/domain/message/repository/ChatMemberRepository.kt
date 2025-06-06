package io.toasting.domain.message.repository

import io.toasting.domain.message.entity.ChatMember
import io.toasting.domain.message.entity.ChatRoom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChatMemberRepository : JpaRepository<ChatMember, Long> {
    fun findByMemberId(toLong: Long): MutableList<ChatMember>
    fun findByMemberIdAndChatRoomId(memberId: Long, chatRoomId: Long): Optional<ChatMember>
    fun findByChatRoom(chatRoom: ChatRoom):MutableList<ChatMember>
    fun findByChatRoomIdInAndMemberIdNot(chatRoomIdList: List<Long?>, myId: Long): List<ChatMember>
}