package v.orm.service

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import v.orm.domain.course.entity.Course
import v.orm.domain.course.repository.CourseRepository
import v.orm.domain.module.entity.Module
import v.orm.domain.module.repository.ModuleRepository
import v.orm.domain.quiz.repository.QuizRepository
import v.orm.domain.quiz.repository.QuizSubmissionRepository
import v.orm.domain.user.entity.User
import v.orm.domain.user.entity.UserRole
import v.orm.domain.user.repository.UserRepository
import kotlin.test.assertEquals


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class QuizServiceTest {

    @Autowired
    lateinit var quizService: QuizService

    @Autowired
    lateinit var moduleRepository: ModuleRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var quizRepository: QuizRepository

    @Autowired
    lateinit var quizSubmissionRepository: QuizSubmissionRepository

    @Autowired
    lateinit var courseRepository: CourseRepository


    @Test
    fun `createQuiz successfully creates a quiz`() {
        val module = saveModule()
        val quiz = quizService.createQuiz(module.id!!, "Quiz 1", 30)

        val loadedQuiz = quizRepository.findById(quiz.id!!).orElseThrow {
            AssertionError("Quiz not found by id=${quiz.id}")
        }
        val loadedModule = moduleRepository.findById(module.id!!).orElseThrow {
            AssertionError("Module not found by id=${module.id}")
        }

        assertAll(
            { assertEquals("Quiz 1", loadedQuiz.title) },
            { assertEquals(30, loadedQuiz.timeLimit) },
            { assertNotNull(loadedQuiz.module) },
            { assertEquals(module.id, loadedQuiz.module!!.id) },
            // связь Module -> Quiz тоже выставилась
            { assertNotNull(loadedModule.quiz) },
            { assertEquals(loadedQuiz.id, loadedModule.quiz!!.id) },
        )
    }

    @Test
    fun `createQuiz throws if module already has a quiz`() {
        val module = saveModule()

        quizService.createQuiz(module.id!!, "Quiz 1", 30)

        val ex = assertThrows(IllegalStateException::class.java) {
            quizService.createQuiz(module.id!!, "Quiz 2", 20)
        }

        assertEquals("У модуля уже есть тест", ex.message)
        assertEquals(1, quizRepository.count()) // не создали второй квиз
    }

    @Test
    fun `submitQuiz successfully submits a quiz`() {
        val student = saveStudent()
        val module = saveModule()
        val quiz = quizService.createQuiz(module.id!!, "Quiz 1", 30)

        val submission = quizService.submitQuiz(quiz.id!!, student.id!!, 95)

        val loadedSubmission = quizSubmissionRepository.findById(submission.id!!).orElseThrow {
            AssertionError("Submission not found by id=${submission.id}")
        }

        assertAll(
            { assertEquals(95, loadedSubmission.score) },
            { assertEquals(student.id, loadedSubmission.student.id) },
            { assertEquals(quiz.id, loadedSubmission.quiz.id) },
            { assertEquals(1, quizSubmissionRepository.count()) },
        )
    }

    @Test
    fun `submitQuiz throws if student already took quiz`() {
        val student = saveStudent()
        val module = saveModule()
        val quiz = quizService.createQuiz(module.id!!, "Quiz 1", 30)

        quizService.submitQuiz(quiz.id!!, student.id!!, 90)
        assertEquals(1, quizSubmissionRepository.count())

        val ex = assertThrows(IllegalStateException::class.java) {
            quizService.submitQuiz(quiz.id!!, student.id!!, 95)
        }

        assertEquals("Этот тест уже был пройден данным студентом", ex.message)
        assertEquals(1, quizSubmissionRepository.count()) // дубль не появился
    }

    @Test
    fun `submitQuiz throws if score is out of range`() {
        val student = saveStudent()
        val module = saveModule()
        val quiz = quizService.createQuiz(module.id!!, "Quiz 1", 30)

        val ex = assertThrows(IllegalArgumentException::class.java) {
            quizService.submitQuiz(quiz.id!!, student.id!!, 120)
        }

        assertEquals("Оценка должна быть от 0 до 100", ex.message)
        assertEquals(0, quizSubmissionRepository.count()) // ничего не сохранилось
    }

    private fun saveTeacher(
        name: String = "Teacher",
        email: String = "teacher@test.com"
    ): User =
        userRepository.save(User(name, email, UserRole.TEACHER))

    private fun saveStudent(
        name: String = "Student",
        email: String = "student@test.com"
    ): User =
        userRepository.save(User(name, email, UserRole.STUDENT))

    private fun saveCourse(
        title: String = "Course",
        teacher: User = saveTeacher()
    ): Course =
        courseRepository.save(Course(title, teacher = teacher))

    private fun saveModule(
        title: String = "Module",
        course: Course = saveCourse()
    ): Module =
        moduleRepository.save(Module(course = course, title = title))
}
