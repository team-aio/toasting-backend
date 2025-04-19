package io.toasting.global.extension

import io.toasting.domain.member.entity.MemberDetails
import io.toasting.global.codec.MemberIdCodec

fun MemberDetails.toMemberId(memberIdCodec: MemberIdCodec) =
    this.username.let { memberIdHash -> memberIdCodec.decode(memberIdHash) }
