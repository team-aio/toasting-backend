package io.toasting.domain.post.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import io.toasting.creator.post.PostCreator
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.post.entity.PinnedPost
import io.toasting.domain.post.entity.Post
import io.toasting.domain.post.exception.PostExceptionHandler
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
class PinPostServiceTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringTestExtension(SpringTestLifecycleMode.Root))

    @Autowired
    private lateinit var pinPostService: PinPostService

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var pinnedPostRepository: PinnedPostRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

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

            When("member1이 post1을 고정하면") {
                pinPostService.pinPost(member1.id!!, post1.id!!)

                Then("고정 게시글이 저장된다") {
                    val pinnedPosts = pinnedPostRepository.findAll()
                    pinnedPosts.size shouldBe 1

                    val pinnedPost = pinnedPosts.first()
                    pinnedPost.post.id shouldBe post1.id
                    pinnedPost.memberId shouldBe member1.id
                }
            }

            When("member1이 이미 고정된 post1을 다시 고정하려고 하면") {
                Then("AlreadyPinnedPostException이 발생한다") {
                    shouldThrow<PostExceptionHandler.AlreadyPinnedPostException> {
                        pinPostService.pinPost(member1.id!!, post1.id!!)
                    }
                }
            }

            When("존재하지 않는 게시글을 고정하려고 하면") {
                Then("PostNotFoundException이 발생한다") {
                    shouldThrow<PostExceptionHandler.PostNotFoundException> {
                        pinPostService.pinPost(member1.id!!, 0L)
                    }
                }
            }

            When("member1이 고정된 post1을 고정 해제하면") {
                pinPostService.unpinPost(member1.id!!, post1.id!!)

                Then("고정 게시글이 삭제된다") {
                    val pinnedPosts = pinnedPostRepository.findAll()
                    pinnedPosts.size shouldBe 0
                }
            }

            When("고정되지 않은 게시글을 고정 해제하려고 하면") {
                Then("NotPinnedPostException이 발생한다") {
                    shouldThrow<PostExceptionHandler.NotPinnedPostException> {
                        pinPostService.unpinPost(member1.id!!, post1.id!!)
                    }
                }
            }

            When("다른 사용자가 고정한 게시글을 고정 해제하려고 하면") {
                val pinnedPost = PinnedPost(
                    post = post1,
                    memberId = member1.id!!
                )
                pinnedPostRepository.save(pinnedPost)

                Then("NotPinnedPostException이 발생한다") {
                    shouldThrow<PostExceptionHandler.NotPinnedPostException> {
                        pinPostService.unpinPost(member2.id!!, post1.id!!)
                    }
                }
            }
        }
    }
}