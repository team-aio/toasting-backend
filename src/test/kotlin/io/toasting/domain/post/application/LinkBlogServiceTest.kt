package io.toasting.domain.post.application

import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.toasting.creator.post.PostCreator
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.post.exception.PostExceptionHandler
import io.toasting.domain.post.repository.PostRepository
import io.toasting.domain.post.vo.SourceType
import io.toasting.global.external.crawler.PostCrawler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class LinkBlogServiceTest : BehaviorSpec() {
    override fun extensions() = listOf(SpringTestExtension(SpringTestLifecycleMode.Root))

    @Autowired
    private lateinit var linkBlogService: LinkBlogService

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @MockkBean
    private lateinit var postCrawler: PostCrawler

    private lateinit var member: Member

    init {
        beforeSpec {
            member = Member.defaultMember("member", "member@test.com", UUID.randomUUID())
            memberRepository.save(member)
        }

        Given("member가 있고,") {
            every { postCrawler.crawlPost(any(), any()) } returns PostCreator.crawledPostList()
            When("tistory 블로그를 연동했을 때") {
                linkBlogService.linkBlog(member.id!!, "test", SourceType.TISTORY)

                val postList = postRepository.findAll()
                Then("tistory 게시글 10개가 저장된다.") {
                    postList.size shouldBe 10
                }
                Then("크롤링된 게시글과 저장된 게시글 정보가 일치한다.") {
                    val firstPost = postList.first()

                    firstPost.memberId shouldBe member.id
                    firstPost.content!!.length shouldBeGreaterThan firstPost.shortContent!!.length
                    firstPost.shortContent!!.length shouldBeLessThanOrEqual 100
                    firstPost.postedAt!!.year shouldBe 2024
                    firstPost.postedAt!!.monthValue shouldBe 5
                    firstPost.postedAt!!.dayOfMonth shouldBe 2
                    firstPost.content shouldContain "<hr"
                }
                Then("블로그 id가 저장된다") {
                    val member = memberRepository.findById(member.id!!).get()

                    member.tistoryId shouldBe "test"
                }
            }

            When("tistory 블로그가 연동되어있는데, 또 연동하면") {
                Then("ALREADY_LINKED_BLOG 예외를 던진다.") {
                    shouldThrow<PostExceptionHandler.AlreadyLinkedBlog> {
                        linkBlogService.linkBlog(member.id!!, "test", SourceType.TISTORY)
                    }
                }
            }

            When("tistory 블로그 연동을 취소하면") {
                linkBlogService.unlinkBlog(member.id!!, SourceType.TISTORY)
                Then("tistory 게시글은 모두 삭제되고, tistoryId는 null이 된다.") {
                    val member = memberRepository.findById(member.id!!).get()
                    member.tistoryId shouldBe null

                    postRepository.findAll().size shouldBe 0
                }
            }
        }
    }

}