package io.toasting.domain.post.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.toasting.creator.post.PostCreator
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.exception.MemberExceptionHandler
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.post.entity.Bookmark
import io.toasting.domain.post.entity.PinnedPost
import io.toasting.domain.post.entity.Post
import io.toasting.domain.post.repository.BookmarkRepository
import io.toasting.domain.post.repository.PinnedPostRepository
import io.toasting.domain.post.repository.PostRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class GetPinnedPostServiceTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringTestExtension(SpringTestLifecycleMode.Root))

    @Autowired
    private lateinit var getPinnedPostService: GetPinnedPostService

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var pinnedPostRepository: PinnedPostRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var bookmarkRepository: BookmarkRepository

    private lateinit var member1: Member
    private lateinit var member2: Member
    private lateinit var member3: Member
    private lateinit var post1: Post
    private lateinit var post2: Post
    private lateinit var post3: Post

    init {
        beforeSpec {
            member1 = Member.defaultMember("member1", "member1@test.com", UUID.randomUUID())
            member2 = Member.defaultMember("member2", "member2@test.com", UUID.randomUUID())
            member3 = Member.defaultMember("member3", "member3@test.com", UUID.randomUUID())
            memberRepository.saveAll(listOf(member1, member2, member3))
        }

        Given("게시글이 주어지고,") {
            post1 = PostCreator.defaultPost(
                "title1",
                "content1",
                "shortContent1",
                member1.id!!,
                LocalDateTime.of(2025, 1, 1, 12, 0, 0)
            )
            post2 = PostCreator.defaultPost(
                "title2",
                "content2",
                "shortContent2",
                member2.id!!,
                LocalDateTime.of(2025, 1, 2, 12, 0, 0)
            )
            post3 = PostCreator.defaultPost(
                "title3",
                "content3",
                "shortContent3",
                member3.id!!,
                LocalDateTime.of(2025, 1, 3, 12, 0, 0)
            )
            postRepository.saveAll(listOf(post1, post2, post3))


            When("member1이 post1과 post2를 고정하고 member2가 post1을 북마크했을 때, member2가 조회하면") {
                val pinnedPost1 = PinnedPost(
                    post = post1,
                    memberId = member1.id!!
                )
                val pinnedPost2 = PinnedPost(
                    post = post2,
                    memberId = member1.id!!
                )
                pinnedPostRepository.saveAll(listOf(pinnedPost1, pinnedPost2))

                val bookmark = Bookmark(
                    post = post1,
                    memberId = member2.id!!
                )
                bookmarkRepository.save(bookmark)

                val result = getPinnedPostService.getPinnedPostListWithLogin(member2.id!!, member1.id!!)

                Then("고정된 게시글 목록이 반환된다") {
                    result.size shouldBe 2
                }

                Then("북마크 정보가 포함된다") {
                    val post1Result = result.find { it.id == post1.id }
                    val post2Result = result.find { it.id == post2.id }

                    post1Result shouldNotBe null
                    post2Result shouldNotBe null

                    post1Result!!.isBookmarked shouldBe true
                    post2Result!!.isBookmarked shouldBe false
                }

                Then("작성자 정보가 포함된다") {
                    val post1Result = result.find { it.id == post1.id }
                    post1Result!!.memberId shouldBe member1.id
                    post1Result.nickname shouldBe member1.nickname
                }
            }

            When("member1이 post1과 post2를 고정하고 비로그인 상태에서 고정 게시글 목록을 조회하면") {
                val result = getPinnedPostService.getPinnedPostListWithoutLogin(member1.id!!)

                Then("고정된 게시글 목록이 반환된다") {
                    result.size shouldBe 2
                }

                Then("모든 게시글의 북마크 정보가 false다") {
                    result.forEach { post ->
                        post.isBookmarked shouldBe false
                    }
                }

                Then("작성자 정보가 포함된다") {
                    val post1Result = result.find { it.id == post1.id }
                    post1Result!!.memberId shouldBe member1.id
                    post1Result.nickname shouldBe member1.nickname
                }
            }

            When("존재하지 않는 작성자의 고정 게시글을 로그인 상태에서 조회하면") {
                Then("MemberNotFoundException이 발생한다") {
                    shouldThrow<MemberExceptionHandler.MemberNotFoundException> {
                        getPinnedPostService.getPinnedPostListWithLogin(member1.id!!, 0L)
                    }
                }
            }

            When("존재하지 않는 작성자의 고정 게시글을 비로그인 상태에서 조회하면") {
                Then("MemberNotFoundException이 발생한다") {
                    shouldThrow<MemberExceptionHandler.MemberNotFoundException> {
                        getPinnedPostService.getPinnedPostListWithoutLogin(0L)
                    }
                }
            }

            When("고정 게시글이 없는 사용자의 목록을 조회하면") {
                val result = getPinnedPostService.getPinnedPostListWithLogin(member1.id!!, member2.id!!)

                Then("빈 목록이 반환된다") {
                    result.size shouldBe 0
                }
            }

        }
    }
}