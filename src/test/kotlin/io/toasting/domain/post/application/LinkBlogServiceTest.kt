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
import io.toasting.domain.post.repository.BookmarkRepository
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
    private lateinit var bookmarkRepository: BookmarkRepository

    @Autowired
    private lateinit var linkBlogService: LinkBlogService

    @Autowired
    private lateinit var postRepository: PostRepository

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @MockkBean
    private lateinit var postCrawler: PostCrawler

    private lateinit var member1: Member
    private lateinit var member2: Member
    private lateinit var member3: Member

    init {
        beforeSpec {
            member1 = Member.defaultMember("member1", "member1@test.com", UUID.randomUUID())
            member2 = Member.defaultMember("member2", "member2@test.com", UUID.randomUUID())
            member3 = Member.defaultMember("member3", "member3@test.com", UUID.randomUUID())
            memberRepository.saveAll(listOf(member1, member2, member3))
        }

        Given("member가 있고,") {
            every { postCrawler.crawlPost(any(), any()) } returns PostCreator.crawledPostList()
            When("tistory 블로그를 연동했을 때") {
                linkBlogService.linkBlog(member1.id!!, "test", SourceType.VELOG)

                val postList = postRepository.findAll()
                Then("tistory 게시글 10개가 저장된다.") {
                    postList.size shouldBe 10
                }
                Then("크롤링된 게시글과 저장된 게시글 정보가 일치한다.") {
                    val firstPost = postList.first()

                    firstPost.memberId shouldBe member1.id
                    firstPost.content!!.length shouldBeGreaterThan firstPost.shortContent!!.length
                    firstPost.shortContent!!.length shouldBeLessThanOrEqual 100
                    firstPost.postedAt!!.year shouldBe 2024
                    firstPost.postedAt!!.monthValue shouldBe 5
                    firstPost.postedAt!!.dayOfMonth shouldBe 2
                    firstPost.content shouldContain "<hr"
                }
                Then("블로그 id가 저장된다") {
                    val member = memberRepository.findById(member1.id!!).get()

                    member.velogId shouldBe "test"
                }
            }

            When("tistory 블로그를 연동하면") {
                member2.registerBlog(SourceType.TISTORY, "test")
                memberRepository.save(member2)
                Then("ALREADY_LINKED_BLOG 예외를 던진다.") {
                    shouldThrow<PostExceptionHandler.AlreadyLinkedBlog> {
                        linkBlogService.linkBlog(member2.id!!, "test", SourceType.TISTORY)
                    }
                }
            }

        }
    }

}