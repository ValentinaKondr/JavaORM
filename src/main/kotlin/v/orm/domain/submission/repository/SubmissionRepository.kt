package v.orm.domain.submission.repository

import org.springframework.data.jpa.repository.JpaRepository
import v.orm.domain.assignment.entity.Assignment
import v.orm.domain.submission.entity.Submission
import v.orm.domain.user.entity.User
import java.util.*

interface SubmissionRepository : JpaRepository<Submission, UUID> {
    fun findByStudent_Id(studentId: UUID): List<Submission>
    fun findByAssignment_Id(assignmentId: UUID): List<Submission>
    fun existsByAssignmentAndStudent(assignment: Assignment, student: User): Boolean
}