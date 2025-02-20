package io.toasting.creator

import io.toasting.domain.member.entity.Member

class MemberCreator {
    companion object {
        fun defaultMember(
            nickname: String,
            email: String,
        ) = Member(
            nickname = nickname,
            email = email,
        )
    }
}
