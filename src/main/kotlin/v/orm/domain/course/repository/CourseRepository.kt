package v.orm.domain.course.repository

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import v.orm.domain.course.entity.Course
import java.util.*

interface CourseRepository : JpaRepository<Course, UUID> {

    fun findByCategory_Id(categoryId: UUID): List<Course>

    @EntityGraph(attributePaths = ["modules", "modules.lessons"])
    fun findDetailedById(id: UUID): Course?
}