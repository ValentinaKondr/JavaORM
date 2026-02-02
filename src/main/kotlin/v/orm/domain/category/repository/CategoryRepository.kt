package v.orm.domain.category.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import v.orm.domain.category.entity.Category
import java.util.*

@Repository
interface CategoryRepository : JpaRepository<Category, UUID> {
    fun findByName(name: String): Category?
    fun existsByName(name: String): Boolean
}