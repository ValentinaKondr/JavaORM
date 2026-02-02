package v.orm.domain.enrollment.repository

import org.springframework.data.jpa.repository.JpaRepository
import v.orm.domain.enrollment.entity.Enrollment
import java.util.*

interface EnrollmentRepository : JpaRepository<Enrollment, UUID> {
    fun findByStudent_Id(studentId: UUID): List<Enrollment>
    fun findByCourse_Id(courseId: UUID): List<Enrollment>
    fun existsByStudent_IdAndCourse_Id(studentId: UUID, courseId: UUID): Boolean
    fun findByStudent_IdAndCourse_Id(studentId: UUID, courseId: UUID): Optional<Enrollment>
}