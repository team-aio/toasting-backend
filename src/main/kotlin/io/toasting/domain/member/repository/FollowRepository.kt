package io.toasting.domain.member.repository

import io.toasting.domain.member.entity.Follow
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FollowRepository : JpaRepository<Follow, Long>
