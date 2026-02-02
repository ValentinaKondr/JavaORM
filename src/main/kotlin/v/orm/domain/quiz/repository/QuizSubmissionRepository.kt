package v.orm.domain.quiz.repository

import org.springframework.data.jpa.repository.JpaRepository
import v.orm.domain.quiz.entity.Quiz
import v.orm.domain.quiz.entity.QuizSubmission
import v.orm.domain.user.entity.User
import java.util.*

interface QuizSubmissionRepository : JpaRepository<QuizSubmission, UUID> {
    fun findByStudent_Id(studentId: UUID): List<QuizSubmission>
    fun findByQuiz_Id(quizId: UUID): List<QuizSubmission>
    fun existsByQuizAndStudent(quiz: Quiz, student: User): Boolean
}