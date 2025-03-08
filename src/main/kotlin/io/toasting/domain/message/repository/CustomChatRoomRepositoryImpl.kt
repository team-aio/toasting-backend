package io.toasting.domain.message.repository

import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import io.toasting.domain.message.entity.ChatRoom
import io.toasting.domain.message.entity.QChatMember
import io.toasting.domain.message.entity.QChatRoom
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class CustomChatRoomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : CustomChatRoomRepository {
    private val chatRoom: QChatRoom = QChatRoom.chatRoom
    private val chatMember: QChatMember = QChatMember.chatMember

    override fun findByMemberId(memberId: Long, pageable: Pageable): Page<ChatRoom> {
        val query = jpaQueryFactory
            .select(chatRoom)
            .from(chatRoom)
            .leftJoin(chatRoom.chatMemberList, chatMember).fetchJoin()
            .where(chatMember.memberId.eq(memberId))
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
        sort(query, pageable)

        val chatRoomList = query.fetch()

        val totalCount = jpaQueryFactory
            .selectDistinct(chatRoom.count())
            .from(chatRoom)
            .join(chatRoom.chatMemberList, chatMember)
            .where(chatMember.memberId.eq(memberId))
            .fetchOne()

        return PageImpl(chatRoomList, pageable, totalCount!!)
    }

    private fun sort(query: JPAQuery<ChatRoom>, pageable: Pageable) {
        for (o in pageable.sort) {
            val pathBuilder = PathBuilder(chatRoom.type, chatRoom.metadata)

            query.orderBy(
                OrderSpecifier(
                    if (o.isAscending) Order.ASC else Order.DESC,
                    pathBuilder.get(o.property) as Expression<out Comparable<ChatRoom>>
                )
            )
        }
    }
}