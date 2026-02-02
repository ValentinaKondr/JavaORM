package v.orm.domain.assignment.repository

import org.springframework.data.jpa.repository.JpaRepository
import v.orm.domain.assignment.entity.Assignment
import java.util.*

interface AssignmentRepository : JpaRepository<Assignment, UUID> {
    fun findByLesson_Id(lessonId: UUID): List<Assignment>
}