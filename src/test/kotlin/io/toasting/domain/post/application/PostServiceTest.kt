package io.toasting.domain.post.application

import com.ninjasquad.springmockk.MockkBean
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.toasting.creator.member.PostCreator
import io.toasting.domain.member.entity.Member
import io.toasting.domain.member.entity.MemberDetails
import io.toasting.domain.member.exception.MemberExceptionHandler
import io.toasting.domain.member.repository.MemberRepository
import io.toasting.domain.post.repository.PostRepository
import io.toasting.global.external.crawler.PostCrawler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
@Transactional
class PostServiceTest : BehaviorSpec(){
    override fun extensions() = listOf(SpringTestExtension(SpringTestLifecycleMode.Root))

    @Autowired
    private lateinit var postService: PostService
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
            member1 = Member.defaultMember("member1", "member1@test.com")
            member2 = Member.defaultMember("member2", "member2@test.com")
            member3 = Member.defaultMember("member3", "member3@test.com")
            memberRepository.saveAll(listOf(member1, member2, member3))
        }

        Given("게시글 5개가 주어지고, ") {
            val post1 = PostCreator.defaultPost("title1", "content1, content1, content1, content1, ", "content1", member1.id!!, LocalDateTime.of(2025, 1, 1,12,0, 1))
            val post2 = PostCreator.defaultPost("title2", "content2, content2, content2, content2, ", "content2", member2.id!!, LocalDateTime.of(2025, 1, 1,12,0, 0))
            val post3 = PostCreator.defaultPost("dummy1", "dummy1, dummy1, dummy1, dummy1, ", "dummy1", member1.id!!, LocalDateTime.of(2025, 1, 1,12,0, 0))
            val post4 = PostCreator.defaultPost("dummy2", "dummy2, dummy2, dummy2, dummy2, ", "dummy2", member2.id!!, LocalDateTime.of(2025, 1, 1,12,0, 0))
            val post5 = PostCreator.defaultPost("exception", "exception, exception, exception, exception, ", "exception", 0L, LocalDateTime.of(2025, 1, 1,12,0, 0))
            postRepository.saveAll(listOf(post1, post2, post3, post4, post5))

            When("content라는 글이 포함된 게시글을 검색했을 떄,") {
                val pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "postedAt"))
                val keyword = "content"
                val output = postService.searchPost(keyword, pageable)
                val postList = output.content

                Then("검색 결과의 총 개수는 2개이다.") {
                    output.totalElements shouldBe 2
                    postList.size shouldBe 2
                    output.totalPages shouldBe 1

                }
                Then("포스팅 날짜 내림차순으로 정렬되어 조회된다.") {
                    val firstPost = postList[0]
                    val secondPost = postList[1]
                    firstPost.id shouldBe post1.id
                    firstPost.memberId shouldBe member1.id
                    secondPost.id shouldBe post2.id
                    secondPost.memberId shouldBe member2.id
                }
            }

            When("게시글 작성자를 찾을 수 없을 때") {
                val pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "postedAt"))
                val keyword = "exception"
                Then("MemberNotFoundException을 던진다.") {
                    shouldThrow<MemberExceptionHandler.MemberNotFoundException> {
                        postService.searchPost(keyword, pageable)
                    }
                }
            }
        }

        Given("member가 있고,") {
            every { postCrawler.crawlPost(any(), any()) } returns PostCreator.crawledPostList()

            When("tistory 블로그를 연동했을 때") {
                val memberDetails = MemberDetails.from(member1)
                postService.linkBlog(memberDetails, "test", "tistory")

                val postList = postRepository.findAll()
                Then("tistory 게시글 10개가 저장된다.") {
                    postList.size shouldBe 10
                }
                Then("크롤링된 게시글과 저장된 게시글 정보가 일치한다.") {
                    val firstPost = postList.first()

                    firstPost.memberId shouldBe member1.id
                    firstPost.content!!.length shouldBeGreaterThan firstPost.shortContent!!.length
                    firstPost.shortContent!!.length shouldBeLessThanOrEqual 100
                    firstPost.postedAt!!.year shouldBe 2023
                    firstPost.postedAt!!.monthValue shouldBe 8
                    firstPost.postedAt!!.dayOfMonth shouldBe 15
                }
            }
        }

        Given("member1이 작성한 게시글1이 있고,") {
            val post = PostCreator.defaultPost("title", "content", "short content", member1.id!!, LocalDateTime.of(2025, 1, 1,12,0, 0))
            postRepository.save(post)
            When("게시글1을 조회하면") {
                val output = postService.getPostDetail(post.id!!)
                Then("게시글1의 정보와 member1의 정보가 반환된다.") {
                    output.id shouldNotBe null
                    output.sourceType shouldBe post.sourceType
                    output.url shouldBe post.url
                    output.postedAt shouldBe post.postedAt
                    output.content shouldBe post.content
                    output.title shouldBe post.title
                    output.likeCount shouldBe post.likeCount
                    output.memberId shouldBe post.memberId
                    output.nickname shouldBe member1.nickname
                    output.profilePicture shouldBe member1.profilePicture
                }
            }
        }
    }
}