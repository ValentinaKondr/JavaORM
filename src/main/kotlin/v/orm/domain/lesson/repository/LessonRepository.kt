package v.orm.domain.lesson.repository

import org.springframework.data.jpa.repository.JpaRepository
import v.orm.domain.lesson.entity.Lesson
import java.util.*

interface LessonRepository : JpaRepository<Lesson, UUID> {
    fun findByModule_Id(moduleId: UUID): List<Lesson>
}