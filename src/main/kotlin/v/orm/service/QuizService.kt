package v.orm.service


import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import v.orm.domain.module.entity.Module
import v.orm.domain.module.repository.ModuleRepository
import v.orm.domain.quiz.entity.Quiz
import v.orm.domain.quiz.entity.QuizSubmission
import v.orm.domain.quiz.repository.QuizRepository
import v.orm.domain.quiz.repository.QuizSubmissionRepository
import v.orm.domain.user.entity.User
import v.orm.domain.user.repository.UserRepository
import java.time.Instant
import java.util.*

@Service
@Transactional
class QuizService(
    private val quizRepository: QuizRepository,
    private val quizSubmissionRepository: QuizSubmissionRepository,
    private val moduleRepository: ModuleRepository,
    private val userRepository: UserRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)


    fun createQuiz(
        moduleId: UUID,
        title: String,
        timeLimit: Int?
    ): Quiz {
        log.info("Запрос на создание теста: moduleId={}, title='{}'", moduleId, title)

        validateQuizTitle(title)

        val module = findModuleOrThrow(moduleId)

        ensureModuleHasNoQuiz(module)

        val quiz = buildQuiz(module, title, timeLimit)

        module.quiz = quiz

        val saved = quizRepository.save(quiz)

        log.info("Тест успешно создан: quizId={}, moduleId={}", saved.id, moduleId)

        return saved
    }

    fun submitQuiz(
        quizId: UUID,
        studentId: UUID,
        score: Int
    ): QuizSubmission {
        log.info(
            "Попытка сдачи теста: quizId={}, studentId={}, score={}",
            quizId,
            studentId,
            score
        )

        validateScore(score)

        val quiz = findQuizOrThrow(quizId)
        val student = findStudentOrThrow(studentId)

        ensureNotSubmittedYet(quiz, student)

        val submission = QuizSubmission(
            quiz = quiz,
            student = student,
            score = score,
            takenAt = Instant.now()
        )

        val saved = quizSubmissionRepository.save(submission)

        log.info(
            "Тест сдан: submissionId={}, quizId={}, studentId={}",
            saved.id,
            quizId,
            studentId
        )

        return saved
    }

    private fun validateQuizTitle(title: String) {
        if (title.isBlank()) {
            log.warn("Попытка создать тест с пустым названием")
            throw IllegalArgumentException("Название теста не может быть пустым")
        }
    }

    private fun validateScore(score: Int) {
        if (score !in 0..100) {
            log.warn("Некорректный балл за тест: {}", score)
            throw IllegalArgumentException("Оценка должна быть от 0 до 100")
        }
    }

    private fun ensureModuleHasNoQuiz(module: Module) {
        if (module.quiz != null) {
            log.warn("Модуль уже содержит тест: moduleId={}", module.id)
            throw IllegalStateException("У модуля уже есть тест")
        }
    }

    private fun ensureNotSubmittedYet(quiz: Quiz, student: User) {
        val exists = quizSubmissionRepository.existsByQuizAndStudent(quiz, student)
        if (exists) {
            log.warn("Повторная попытка сдачи теста: quizId={}, studentId={}", quiz.id, student.id)
            throw IllegalStateException("Этот тест уже был пройден данным студентом")
        }
    }

    private fun findModuleOrThrow(moduleId: UUID): Module =
        moduleRepository.findById(moduleId)
            .orElseThrow {
                log.warn("Модуль не найден: {}", moduleId)
                IllegalArgumentException("Модуль не найден")
            }

    private fun findQuizOrThrow(quizId: UUID): Quiz =
        quizRepository.findById(quizId)
            .orElseThrow {
                log.warn("Тест не найден: {}", quizId)
                IllegalArgumentException("Тест не найден")
            }

    private fun findStudentOrThrow(studentId: UUID): User =
        userRepository.findById(studentId)
            .orElseThrow {
                log.warn("Студент не найден: {}", studentId)
                IllegalArgumentException("Студент не найден")
            }

    private fun buildQuiz(
        module: Module,
        title: String,
        timeLimit: Int?
    ): Quiz =
        Quiz(
            module = module,
            title = title,
            timeLimit = timeLimit
        )
}