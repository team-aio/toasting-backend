package io.toasting.domain.member.application

import org.springframework.stereotype.Service

@Service
class MemberDetailService
// JWT 기반 인증을 사용하기 때문에 현재 불필요한 코드여서 주석처리
//    private val memberRepository: MemberRepository,
//) : UserDetailsService {
//    override fun loadUserByUsername(username: String?): UserDetails? {
//        println("username: $username")
//        return memberRepository
//            .findById(username?.toLongOrNull() ?: return null)
//            .map { MemberDetails.from(it) }
//            .getOrNull()
//    }
