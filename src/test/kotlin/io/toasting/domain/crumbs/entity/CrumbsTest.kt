package io.toasting.domain.crumbs.entity

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class CrumbsTest : BehaviorSpec({
    Given("멤버 ID와 활동 날짜가 주어졌을 때") {
        val memberId = 1L
        val activityDate = LocalDate.of(2023, 5, 15)

        When("Crumbs를 생성하면") {
            val crumbs = Crumbs.create(memberId, activityDate)

            Then("멤버 ID와 활동 날짜가 올바르게 설정되어야 한다") {
                crumbs.memberId shouldBe memberId
                crumbs.activityDate shouldBe activityDate
            }

            Then("활동 카운트는 0으로 초기화되어야 한다") {
                crumbs.activityCount shouldBe 0
            }

            Then("모든 ID 리스트는 비어있어야 한다") {
                crumbs.commentIds.shouldBeEmpty()
                crumbs.postIds.shouldBeEmpty()
                crumbs.bookmarkIds.shouldBeEmpty()
                crumbs.likeIds.shouldBeEmpty()
            }
        }
    }

    Given("Crumbs 엔티티가 있을 때") {
        val memberId = 1L
        val activityDate = LocalDate.now()
        val crumbs = Crumbs.create(memberId, activityDate)

        When("댓글 ID를 추가하면") {
            val commentId = 123L
            crumbs.addCommentId(commentId)

            Then("commentIds 리스트에 추가되고 활동 카운트가 증가해야 한다") {
                crumbs.commentIds shouldContainExactly listOf(commentId)
                crumbs.activityCount shouldBe 1
            }
        }

        When("포스트 ID를 추가하면") {
            val postId = 456L
            crumbs.addPostId(postId)

            Then("postIds 리스트에 추가되고 활동 카운트가 증가해야 한다") {
                crumbs.postIds shouldContainExactly listOf(postId)
                crumbs.activityCount shouldBe 2
            }
        }

        When("북마크 ID를 추가하면") {
            val bookmarkId = 789L
            crumbs.addBookmarkId(bookmarkId)

            Then("bookmarkIds 리스트에 추가되고 활동 카운트가 증가해야 한다") {
                crumbs.bookmarkIds shouldContainExactly listOf(bookmarkId)
                crumbs.activityCount shouldBe 3
            }
        }

        When("좋아요 ID를 추가하면") {
            val likeId = 101112L
            crumbs.addLikeId(likeId)

            Then("likeIds 리스트에 추가되고 활동 카운트가 증가해야 한다") {
                crumbs.likeIds shouldContainExactly listOf(likeId)
                crumbs.activityCount shouldBe 4
            }
        }
    }

    Given("여러 활동이 추가된 Crumbs 엔티티가 있을 때") {
        val memberId = 1L
        val activityDate = LocalDate.now()
        val crumbs = Crumbs.create(memberId, activityDate)

        val commentId = 123L
        val postId = 456L
        val bookmarkId = 789L
        val likeId = 101112L

        crumbs.addCommentId(commentId)
        crumbs.addPostId(postId)
        crumbs.addBookmarkId(bookmarkId)
        crumbs.addLikeId(likeId)
        When("댓글 ID를 제거하면") {
            crumbs.removeCommentId(commentId)

            Then("commentIds 리스트에서 제거되고 활동 카운트가 감소해야 한다") {
                crumbs.commentIds.shouldBeEmpty()
            }
        }
    }
})
