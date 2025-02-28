package io.toasting.domain.member.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class MemberDetails(
    private val role: String,
    private val memberId: Long,
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(GrantedAuthority { role })

    override fun getPassword(): String? = null

    override fun getUsername(): String = memberId.toString()

    companion object {
        fun from(member: Member): MemberDetails =
            MemberDetails(
                role = "ROLE_USER",
                memberId = member.id ?: throw IllegalArgumentException("Member id must not be null."),
            )
    }
}
