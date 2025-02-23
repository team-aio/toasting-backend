package io.toasting.domain.member.application

import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.member.repository.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class MemberDetailService(
    private val memberRepository: MemberRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails? {
        return memberRepository
            .findById(username?.toLongOrNull() ?: return null)
            .map { MemberDetails.from(it) }
            .getOrNull()
    }
}
