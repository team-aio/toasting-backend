package io.toasting.domain.member.application

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.toasting.creator.member.MemberCreator
import io.toasting.creator.member.SocialLoginCreator
import io.toasting.creator.post.BookmarkCreator
import io.toasting.creator.post.LikeCreator
import io.toasting.creator.post.PostCreator
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.member.repository.SocialLoginRepository
import io.toasting.domain.post.repository.BookmarkRepository
import io.toasting.domain.post.repository.LikeRepository
import io.toasting.domain.post.repository.PostRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class DeleteMemberServiceTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringExtension)

    @Autowired
    private lateinit var deleteMemberService: DeleteMemberService

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var socialLoginRepository: SocialLoginRepository

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var bookmarkRepository: BookmarkRepository

    @Autowired
    private lateinit var likeRepository: LikeRepository

    init {
        Given("구글 로그인 멤버와 멤버가 저장되어 있을때,") {

            val member = MemberCreator.defaultMember(null, "test", "test@test.com")
            socialLoginRepository.save(
                SocialLoginCreator.defaultGoogleLogin(
                    "1234",
                    member
                )
            )
            val post =
                postRepository.save(PostCreator.defaultPost("test", "test", "test", member.id!!, LocalDateTime.now()))
            likeRepository.save(LikeCreator.default(post, member.id!!))
            bookmarkRepository.save(BookmarkCreator.default(post, member.id!!))
            When("회원 탈퇴 이벤트가 발행되면") {
                deleteMemberService.deleteMember(member.id!!)
                Then("멤버가 삭제되어야 한다.") {
                    memberRepository.findAll().filter { it.id == member.id }.size shouldBe 0
                }
                Then("포스팅이 삭제되어야 한다") {
                    postRepository.findAll().filter { it.memberId == member.id }.size shouldBe 0
                }
                Then("좋아요가 삭제되어야 한다.") {
                    likeRepository.findAll().filter { it.memberId == member.id }.size shouldBe 0
                }
                Then("북마크가 삭제되어야 한다") {
                    bookmarkRepository.findAll().filter { it.memberId == member.id }.size shouldBe 0
                }
            }
        }
    }
}