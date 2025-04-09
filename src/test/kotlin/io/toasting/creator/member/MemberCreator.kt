package io.toasting.creator.member

import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.vo.RoleType

object MemberCreator {
    fun defaultMember(
        id: Long,
        nickname: String,
        email: String,
        memberIdHash: String,
    ) = Member(
        id = id,
        nickname = nickname,
        role = RoleType.ROLE_USER,
        email = email,
        memberIdHash = memberIdHash,
    )
}
