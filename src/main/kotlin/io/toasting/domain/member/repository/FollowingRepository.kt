package team.toasting.repository.member

import Following
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FollowingRepository : JpaRepository<Following, Long>
