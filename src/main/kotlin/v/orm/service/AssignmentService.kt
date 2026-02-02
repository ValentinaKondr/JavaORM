package v.orm.service


import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import v.orm.domain.assignment.entity.Assignment
import v.orm.domain.assignment.repository.AssignmentRepository
import v.orm.domain.lesson.entity.Lesson
import v.orm.domain.lesson.repository.LessonRepository
import v.orm.domain.submission.entity.Submission
import v.orm.domain.submission.repository.SubmissionRepository
import v.orm.domain.user.entity.User
import v.orm.domain.user.repository.UserRepository
import java.time.Instant
import java.util.*

@Service
@Transactional
class AssignmentService(
    private val assignmentRepository: AssignmentRepository,
    private val submissionRepository: SubmissionRepository,
    private val lessonRepository: LessonRepository,
    private val userRepository: UserRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun createAssignment(
        lessonId: UUID,
        title: String,
        dueDate: Instant?
    ): Assignment {
        log.info("Запрос на создание задания: lessonId={}, title='{}'", lessonId, title)

        validateTitle(title)

        val lesson = findLessonOrThrow(lessonId)

        val assignment = buildAssignment(lesson, title, dueDate)

        lesson.assignments.add(assignment)

        val saved = assignmentRepository.save(assignment)

        log.info("Задание успешно создано: assignmentId={}", saved.id)

        return saved
    }

    fun submitAssignment(
        assignmentId: UUID,
        studentId: UUID,
        score: Int?
    ): Submission {
        log.info(
            "Попытка отправки задания: assignmentId={}, studentId={}",
            assignmentId,
            studentId
        )

        val assignment = findAssignmentOrThrow(assignmentId)
        val student = findStudentOrThrow(studentId)

        checkDuplicateSubmission(assignment, student)

        val submission = Submission(
            assignment = assignment,
            student = student,
            score = score
        )

        val saved = submissionRepository.save(submission)

        log.info(
            "Задание отправлено студентом: submissionId={}, studentId={}",
            saved.id,
            studentId
        )

        return saved
    }

    fun gradeSubmission(submissionId: UUID, score: Int) {
        log.info(
            "Выставление оценки: submissionId={}, score={}",
            submissionId,
            score
        )

        validateScore(score)

        val submission = findSubmissionOrThrow(submissionId)

        submission.score = score

        submissionRepository.save(submission)

        log.info(
            "Оценка сохранена: submissionId={}, score={}",
            submissionId,
            score
        )
    }

    fun getAssignmentSubmissions(assignmentId: UUID): List<Submission> {
        log.debug("Запрос списка отправок для задания: {}", assignmentId)

        if (!assignmentRepository.existsById(assignmentId)) {
            log.warn("Попытка получить отправки для несуществующего задания: {}", assignmentId)
            throw IllegalArgumentException("Задание не найдено")
        }

        val submissions =
            submissionRepository.findByAssignment_Id(assignmentId)

        log.debug(
            "Найдено {} отправок для assignmentId={}",
            submissions.size,
            assignmentId
        )

        return submissions
    }

    private fun validateTitle(title: String) {
        if (title.isBlank()) {
            log.warn("Попытка создать задание с пустым названием")
            throw IllegalArgumentException("Название задания не может быть пустым")
        }
    }

    private fun validateScore(score: Int) {
        if (score !in 0..100) {
            log.warn("Некорректная оценка: {}", score)
            throw IllegalArgumentException("Оценка должна быть от 0 до 100")
        }
    }

    private fun checkDuplicateSubmission(
        assignment: Assignment,
        student: User
    ) {
        val exists = submissionRepository
            .existsByAssignmentAndStudent(assignment, student)

        if (exists) {
            log.warn(
                "Повторная отправка задания: assignmentId={}, studentId={}",
                assignment.id,
                student.id
            )

            throw IllegalStateException("Задание уже было отправлено этим студентом")
        }
    }

    private fun findLessonOrThrow(id: UUID): Lesson =
        lessonRepository.findById(id)
            .orElseThrow {
                log.warn("Урок не найден: {}", id)
                IllegalArgumentException("Урок не найден")
            }

    private fun findAssignmentOrThrow(id: UUID): Assignment =
        assignmentRepository.findById(id)
            .orElseThrow {
                log.warn("Задание не найдено: {}", id)
                IllegalArgumentException("Задание не найдено")
            }

    private fun findStudentOrThrow(id: UUID): User =
        userRepository.findById(id)
            .orElseThrow {
                log.warn("Студент не найден: {}", id)
                IllegalArgumentException("Студент не найден")
            }

    private fun findSubmissionOrThrow(id: UUID): Submission =
        submissionRepository.findById(id)
            .orElseThrow {
                log.warn("Отправка не найдена: {}", id)
                IllegalArgumentException("Отправка не найдена")
            }

    private fun buildAssignment(
        lesson: Lesson,
        title: String,
        dueDate: Instant?
    ): Assignment =
        Assignment(
            lesson = lesson,
            title = title,
            dueDate = dueDate
        )
}