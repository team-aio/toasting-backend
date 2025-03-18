package io.toasting.domain.member.application

import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.application.output.GetMyProfileOutput
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.exception.MemberExceptionHandler
import io.toasting.domain.member.repository.FollowRepository
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.post.repository.PostRepository
import org.springframework.stereotype.Service

@Service
class GetProfileService(
    private val followRepository: FollowRepository,
    private val postRepository: PostRepository,
    private val memberRepository: MemberRepository,
) {
    fun getMyProfile(memberId: Long): GetMyProfileOutput {
        val member = findMemberOrThrow(memberId)
        val (followingCount, followerCount) = getFollowCounts(member)
        val postCount = postRepository.countByMemberId(memberId)

        return GetMyProfileOutput(
            nickname = member.nickname,
            followingCount = followingCount,
            followerCount = followerCount,
            postCount = postCount,
            velogId = member.velogId,
            tistoryId = member.tistoryId,
        )
    }

    private fun findMemberOrThrow(memberId: Long): Member =
        memberRepository
            .findById(memberId)
            .orElseThrow { throw MemberExceptionHandler.MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND) }

    private fun getFollowCounts(member: Member): Pair<Long, Long> {
        val followingCount = followRepository.countByFromMember(member)
        val followerCount = followRepository.countByToMember(member)
        return followingCount to followerCount
    }
}
