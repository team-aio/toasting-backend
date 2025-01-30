import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import team.toasting.entity.post.Category

@Repository
interface CategoryRepository : JpaRepository<Category, Long>
