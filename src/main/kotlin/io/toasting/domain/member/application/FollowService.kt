package io.toasting.domain.member.application

import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.application.input.AddFollowInput
import io.toasting.domain.member.entity.Follow
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.exception.MemberExceptionHandler.MemberNotFoundException
import io.toasting.domain.member.repository.FollowRepository
import io.toasting.domain.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class FollowService(
    private val followRepository: FollowRepository,
    private val memberRepository: MemberRepository,
) {
    @Transactional
    fun addFollow(addFollowInput: AddFollowInput) {
        // TODO: fromMemberId와 toMemberId가 같은 경우 예외처리
        // TODO: 이미 팔로우한 경우 예외처리
        val fromMember = findMemberByIdOrThrow(addFollowInput.fromMemberId)
        val toMember = findMemberByIdOrThrow(addFollowInput.toMemberId)

        val newFollow = Follow(fromMember, toMember)
        followRepository.save(newFollow)
    }

    private fun findMemberByIdOrThrow(memberId: Long): Member =
        memberRepository
            .findById(memberId)
            .orElseThrow { MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND) }
}
