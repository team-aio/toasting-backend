package io.toasting.domain.member.repository

import io.toasting.domain.member.entity.Follower
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FollowerRepository : JpaRepository<Follower, Long>
