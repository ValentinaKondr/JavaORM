package v.orm.service

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import v.orm.domain.assignment.repository.AssignmentRepository
import v.orm.domain.course.entity.Course
import v.orm.domain.course.repository.CourseRepository
import v.orm.domain.lesson.entity.Lesson
import v.orm.domain.lesson.repository.LessonRepository
import v.orm.domain.module.entity.Module
import v.orm.domain.module.repository.ModuleRepository
import v.orm.domain.submission.repository.SubmissionRepository
import v.orm.domain.user.entity.User
import v.orm.domain.user.entity.UserRole
import v.orm.domain.user.repository.UserRepository
import java.time.Instant
import kotlin.test.assertEquals

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AssignmentServiceTest {

    @Autowired
    lateinit var assignmentService: AssignmentService

    @Autowired
    lateinit var lessonRepository: LessonRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var moduleRepository: ModuleRepository

    @Autowired
    lateinit var assignmentRepository: AssignmentRepository

    @Autowired
    lateinit var submissionRepository: SubmissionRepository


    @Test
    fun `createAssignment saves assignment and links to lesson`() {

        // given
        val lesson = prepareLessonWithTeacher()

        // when
        val assignment = assignmentService.createAssignment(
            lessonId = lesson.id!!,
            title = "Test Assignment",
            dueDate = Instant.now()
        )

        // then
        val storedAssignment = assignmentRepository
            .findById(assignment.id!!)
            .orElseThrow()

        assertEquals("Test Assignment", storedAssignment.title)
        assertEquals(lesson.id, storedAssignment.lesson?.id)

        val storedLesson = lessonRepository
            .findById(lesson.id!!)
            .orElseThrow()

        assertTrue(
            storedLesson.assignments.any { it.id == assignment.id }
        )
    }


    @Test
    fun `submitAssignment saves submission and prevents duplicate`() {

        // given
        val lesson = prepareLessonWithTeacher()
        val student = createStudent("student@test.com")

        val assignment = assignmentService.createAssignment(
            lesson.id!!,
            "Assignment 1",
            Instant.now()
        )

        // when
        val submission = assignmentService.submitAssignment(
            assignment.id!!,
            student.id!!,
            90
        )

        // then
        val storedSubmission = submissionRepository
            .findById(submission.id!!)
            .orElseThrow()

        assertEquals(90, storedSubmission.score)
        assertEquals(assignment.id, storedSubmission.assignment.id)
        assertEquals(student.id, storedSubmission.student.id)
    }


    @Test
    fun `gradeSubmission updates score`() {

        // given
        val lesson = prepareLessonWithTeacher()
        val student = createStudent("student@test.com")

        val assignment = assignmentService.createAssignment(
            lesson.id!!,
            "Assignment 1",
            Instant.now()
        )

        val submission = assignmentService.submitAssignment(
            assignment.id!!,
            student.id!!,
            null
        )

        // when
        assignmentService.gradeSubmission(submission.id!!, 85)

        // then
        val updatedSubmission = submissionRepository
            .findById(submission.id!!)
            .orElseThrow()

        assertEquals(85, updatedSubmission.score)
    }


    @Test
    fun `getAssignmentSubmissions returns all submissions`() {

        // given
        val lesson = prepareLessonWithTeacher()

        val student1 = createStudent("s1@test.com")
        val student2 = createStudent("s2@test.com")

        val assignment = assignmentService.createAssignment(
            lesson.id!!,
            "Assignment 1",
            Instant.now()
        )

        val firstSubmission = assignmentService.submitAssignment(
            assignment.id!!,
            student1.id!!,
            90
        )

        val secondSubmission = assignmentService.submitAssignment(
            assignment.id!!,
            student2.id!!,
            95
        )

        // when
        val submissions =
            assignmentService.getAssignmentSubmissions(assignment.id!!)

        // then
        assertEquals(2, submissions.size)

        assertTrue(
            submissions.any { it.id == firstSubmission.id }
        )

        assertTrue(
            submissions.any { it.id == secondSubmission.id }
        )
    }

    private fun prepareLessonWithTeacher(): Lesson {

        val teacher = createTeacher()

        val course = courseRepository.save(
            Course(
                title = "Course",
                teacher = teacher
            )
        )

        val module = moduleRepository.save(
            Module(
                course = course,
                title = "Module"
            )
        )

        return lessonRepository.save(
            Lesson(
                module = module,
                title = "Lesson"
            )
        )
    }


    private fun createTeacher(): User =
        userRepository.save(
            User(
                "Teacher",
                "teacher@test.com",
                UserRole.TEACHER
            )
        )


    private fun createStudent(email: String): User =
        userRepository.save(
            User(
                "Student",
                email,
                UserRole.STUDENT
            )
        )
}