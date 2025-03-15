package io.toasting.domain.post.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Expression
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.PathBuilder
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import io.toasting.domain.post.entity.Post
import io.toasting.domain.post.entity.QPost
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class CustomPostRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomPostRepository {

    private val post = QPost.post

    override fun searchByKeyword(keyword: String, pageable: Pageable): Page<Post> {
        val regexp = toRegexp(keyword)
        val query = queryFactory
            .selectDistinct(post)
            .from(post)
            .where(searchPostCondition(regexp))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
        sort(query, pageable)
        val postList = query.fetch();

        val totalCount = queryFactory
            .select(post.count())
            .from(post)
            .where(searchPostCondition(regexp))
            .fetchOne()?: 0L

        return PageImpl(postList, pageable, totalCount)
    }

    private fun toRegexp(keyword: String): String {
        return "%" + keyword + "%"
    }

    private fun searchPostCondition(keyword: String): BooleanBuilder {
        val booleanBuilder = BooleanBuilder()
        booleanBuilder.or(post.content.like(keyword))
        booleanBuilder.or(post.title.like(keyword))
        return booleanBuilder
    }

    private fun sort(query: JPAQuery<Post>, pageable: Pageable) {
        for (o in pageable.sort) {
            val pathBuilder = PathBuilder(post.type, post.metadata)

            query.orderBy(
                OrderSpecifier(
                    if (o.isAscending) Order.ASC else Order.DESC,
                    pathBuilder.get(o.property) as Expression<out Comparable<Post>>
                )
            )
        }
    }
}