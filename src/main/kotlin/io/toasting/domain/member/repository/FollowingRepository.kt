package io.toasting.domain.member.repository

import io.toasting.domain.member.entity.Following
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FollowingRepository : JpaRepository<Following, Long>
