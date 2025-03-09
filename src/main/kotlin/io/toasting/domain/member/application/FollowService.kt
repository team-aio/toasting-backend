package io.toasting.domain.member.application

import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.application.input.AddFollowInput
import io.toasting.domain.member.application.input.CancelFollowInput
import io.toasting.domain.member.application.input.CheckFollowInput
import io.toasting.domain.member.entity.Follow
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.exception.MemberExceptionHandler.MemberNotFoundException
import io.toasting.domain.member.repository.FollowRepository
import io.toasting.domain.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
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

    @Transactional
    fun cancelFollow(cancelFollowInput: CancelFollowInput) {
        val fromMember = findMemberByIdOrThrow(cancelFollowInput.fromMemberId)
        val toMember = findMemberByIdOrThrow(cancelFollowInput.toMemberId)

        followRepository.deleteByFromMemberAndToMember(fromMember, toMember)
    }

    fun checkAlreadyFollow(checkFollowInput: CheckFollowInput): Boolean {
        val fromMember = findMemberByIdOrThrow(checkFollowInput.fromMemberId)
        val toMember = findMemberByIdOrThrow(checkFollowInput.toMemberId)

        return followRepository.existsByFromMemberAndToMember(fromMember, toMember)
    }

    private fun findMemberByIdOrThrow(memberId: Long): Member =
        memberRepository
            .findById(memberId)
            .orElseThrow { MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND) }
}
