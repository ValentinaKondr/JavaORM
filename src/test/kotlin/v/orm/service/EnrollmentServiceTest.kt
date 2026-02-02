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
import v.orm.domain.enrollment.repository.EnrollmentRepository
import v.orm.domain.user.entity.User
import v.orm.domain.user.entity.UserRole
import v.orm.domain.user.repository.UserRepository
import kotlin.test.assertEquals


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EnrollmentServiceTest {

    @Autowired
    lateinit var enrollmentService: EnrollmentService

    @Autowired
    lateinit var enrollmentRepository: EnrollmentRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var courseRepository: CourseRepository


    @Test
    fun `enrollStudent saves enrollment`() {
        val teacher = saveTeacher()
        val student = saveStudent()
        val course = saveCourse(teacher = teacher)

        val enrollment = enrollmentService.enrollStudent(course.id!!, student.id!!)

        assertAll(
            { assertNotNull(enrollment.id) },
            { assertEquals(student.id, enrollment.student.id) },
            { assertEquals(course.id, enrollment.course.id) },
            { assertEquals(1, enrollmentRepository.count()) },
        )
    }

    @Test
    fun `enrollStudent throws when already enrolled`() {
        val teacher = saveTeacher()
        val student = saveStudent()
        val course = saveCourse(teacher = teacher)

        enrollmentService.enrollStudent(course.id!!, student.id!!)

        val ex = assertThrows(IllegalStateException::class.java) {
            enrollmentService.enrollStudent(course.id!!, student.id!!)
        }

        assertEquals("Студент уже записан на этот курс", ex.message)
        assertEquals(1, enrollmentRepository.count()) // важно: не создался дубль
    }

    @Test
    fun `unenrollStudent deletes enrollment`() {
        val teacher = saveTeacher()
        val student = saveStudent()
        val course = saveCourse(teacher = teacher)

        enrollmentService.enrollStudent(course.id!!, student.id!!)
        assertEquals(1, enrollmentRepository.count())

        enrollmentService.unenrollStudent(course.id!!, student.id!!)

        assertEquals(0, enrollmentRepository.count())
    }

    @Test
    fun `getStudentCourses returns courses of student`() {
        val teacher = saveTeacher()
        val student = saveStudent()
        val course1 = saveCourse(title = "Course 1", teacher = teacher)
        val course2 = saveCourse(title = "Course 2", teacher = teacher)

        enrollmentService.enrollStudent(course1.id!!, student.id!!)
        enrollmentService.enrollStudent(course2.id!!, student.id!!)

        val courses = enrollmentService.getStudentCourses(student.id!!)

        val ids = courses.mapNotNull { it.id }.toSet()
        assertEquals(setOf(course1.id, course2.id), ids)
    }

    @Test
    fun `getCourseStudents returns students of course`() {
        val teacher = saveTeacher()
        val student1 = saveStudent(name = "Student1", email = "s1@test.com")
        val student2 = saveStudent(name = "Student2", email = "s2@test.com")
        val course = saveCourse(teacher = teacher)

        enrollmentService.enrollStudent(course.id!!, student1.id!!)
        enrollmentService.enrollStudent(course.id!!, student2.id!!)

        val students = enrollmentService.getCourseStudents(course.id!!)

        val ids = students.mapNotNull { it.id }.toSet()
        assertEquals(setOf(student1.id, student2.id), ids)
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
}
