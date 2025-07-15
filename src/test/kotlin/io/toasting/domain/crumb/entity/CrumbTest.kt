package io.toasting.domain.crumb.entity

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class CrumbTest : BehaviorSpec({
    Given("멤버 ID와 활동 날짜가 주어졌을 때") {
        val memberId = 1L
        val activityDate = LocalDate.of(2023, 5, 15)

        When("Crumbs를 생성하면") {
            val crumb = Crumb.create(memberId, activityDate)

            Then("멤버 ID와 활동 날짜가 올바르게 설정되어야 한다") {
                crumb.memberId shouldBe memberId
                crumb.activityDate shouldBe activityDate
            }

            Then("활동 카운트는 0으로 초기화되어야 한다") {
                crumb.activityCount shouldBe 0
            }

            Then("모든 ID 리스트는 비어있어야 한다") {
                crumb.commentIds.shouldBeEmpty()
                crumb.postIds.shouldBeEmpty()
                crumb.bookmarkIds.shouldBeEmpty()
                crumb.likeIds.shouldBeEmpty()
            }
        }
    }

    Given("Crumbs 엔티티가 있을 때") {
        val memberId = 1L
        val activityDate = LocalDate.now()
        val crumb = Crumb.create(memberId, activityDate)

        When("댓글 ID를 추가하면") {
            val commentId = 123L
            crumb.addCommentId(commentId)

            Then("commentIds 리스트에 추가되고 활동 카운트가 증가해야 한다") {
                crumb.commentIds shouldContainExactly listOf(commentId)
                crumb.activityCount shouldBe 1
            }
        }

        When("포스트 ID를 추가하면") {
            val postId = 456L
            crumb.addPostId(postId)

            Then("postIds 리스트에 추가되고 활동 카운트가 증가해야 한다") {
                crumb.postIds shouldContainExactly listOf(postId)
                crumb.activityCount shouldBe 2
            }
        }

        When("북마크 ID를 추가하면") {
            val bookmarkId = 789L
            crumb.addBookmarkId(bookmarkId)

            Then("bookmarkIds 리스트에 추가되고 활동 카운트가 증가해야 한다") {
                crumb.bookmarkIds shouldContainExactly listOf(bookmarkId)
                crumb.activityCount shouldBe 3
            }
        }

        When("좋아요 ID를 추가하면") {
            val likeId = 101112L
            crumb.addLikeId(likeId)

            Then("likeIds 리스트에 추가되고 활동 카운트가 증가해야 한다") {
                crumb.likeIds shouldContainExactly listOf(likeId)
                crumb.activityCount shouldBe 4
            }
        }
    }

    Given("여러 활동이 추가된 Crumbs 엔티티가 있을 때") {
        val memberId = 1L
        val activityDate = LocalDate.now()
        val crumb = Crumb.create(memberId, activityDate)

        val commentId = 123L
        val postId = 456L
        val bookmarkId = 789L
        val likeId = 101112L

        crumb.addCommentId(commentId)
        crumb.addPostId(postId)
        crumb.addBookmarkId(bookmarkId)
        crumb.addLikeId(likeId)
        When("댓글 ID를 제거하면") {
            crumb.removeCommentId(commentId)

            Then("commentIds 리스트에서 제거되고 활동 카운트가 감소해야 한다") {
                crumb.commentIds.shouldBeEmpty()
            }
        }
    }
})
