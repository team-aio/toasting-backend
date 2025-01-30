package team.toasting.repository.post

import Like
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LikeRepository : JpaRepository<Like, Long>
