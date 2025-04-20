package io.toasting.domain.post.application

import io.toasting.api.PageResponse
import io.toasting.api.code.status.ErrorStatus
import io.toasting.domain.member.exception.MemberExceptionHandler
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.post.application.out.SearchPostsOutput
import io.toasting.domain.post.repository.PostRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class NonMemberPostService(
    private val postRepository: PostRepository,
    private val memberRepository: MemberRepository,
) {
    fun searchPostWithoutMember(keyword: String?, pageable: Pageable): PageResponse<SearchPostsOutput> {
        val postPage = postRepository.searchByKeyword(keyword, pageable)
        val postList = postPage.content

        val memberIdList = postList.map { it.memberId }
        val memberList = memberRepository.findAllById(memberIdList)
        val memberMapById = memberList.associateBy { it.id!! }

        val outputList = mutableListOf<SearchPostsOutput>()
        for (post in postList) {
            val memberId = post.memberId
            val member = memberMapById[memberId]
            if (member == null) {
                throw MemberExceptionHandler.MemberNotFoundException(ErrorStatus.MEMBER_NOT_FOUND)
            }
            val output = SearchPostsOutput.of(post, member, false)
            outputList.add(output)
        }

        return PageResponse.of(
            outputList,
            postPage.totalElements,
            postPage.totalPages
        )
    }
}