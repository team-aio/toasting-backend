package io.toasting.creator.member

import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.vo.RoleType
import java.util.UUID

object MemberCreator {
    fun defaultMember(
        id: Long,
        nickname: String,
        email: String,
        uuid: UUID = UUID.randomUUID()
    ) = Member(
        id = id,
        nickname = nickname,
        role = RoleType.ROLE_USER,
        email = email,
        uuid = uuid,
    )
}
