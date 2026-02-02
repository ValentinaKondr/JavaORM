package v.orm.service


import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import v.orm.domain.course.entity.Course
import v.orm.domain.course.repository.CourseRepository
import v.orm.domain.enrollment.entity.Enrollment
import v.orm.domain.enrollment.repository.EnrollmentRepository
import v.orm.domain.user.entity.User
import v.orm.domain.user.repository.UserRepository
import java.util.*

@Service
@Transactional
class EnrollmentService(
    private val enrollmentRepository: EnrollmentRepository,
    private val userRepository: UserRepository,
    private val courseRepository: CourseRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)


    fun enrollStudent(courseId: UUID, studentId: UUID): Enrollment {
        log.info("Запрос на запись студента на курс: courseId={}, studentId={}", courseId, studentId)

        val course = findCourseOrThrow(courseId)
        val student = findStudentOrThrow(studentId)

        ensureNotEnrolled(studentId, courseId)

        val enrollment = Enrollment(
            student = student,
            course = course
        )

        val saved = enrollmentRepository.save(enrollment)

        log.info("Студент записан на курс: enrollmentId={}, courseId={}, studentId={}", saved.id, courseId, studentId)

        return saved
    }

    fun unenrollStudent(courseId: UUID, studentId: UUID) {
        log.warn("Запрос на отписку студента от курса: courseId={}, studentId={}", courseId, studentId)

        val enrollment = findEnrollmentOrThrow(studentId, courseId)

        enrollmentRepository.delete(enrollment)

        log.info("Студент отписан от курса: courseId={}, studentId={}", courseId, studentId)
    }

    fun getStudentCourses(studentId: UUID): List<Course> {
        log.debug("Запрос списка курсов студента: studentId={}", studentId)

        // Проверяем, что студент существует (чтобы не возвращать "пустоту" для несуществующего id)
        findStudentOrThrow(studentId)

        val courses = enrollmentRepository.findByStudent_Id(studentId)
            .map { it.course }

        log.debug("Найдено курсов у студента: studentId={}, count={}", studentId, courses.size)

        return courses
    }

    fun getCourseStudents(courseId: UUID): List<User> {
        log.debug("Запрос списка студентов курса: courseId={}", courseId)

        // Проверяем, что курс существует
        findCourseOrThrow(courseId)

        val students = enrollmentRepository.findByCourse_Id(courseId)
            .map { it.student }

        log.debug("Найдено студентов на курсе: courseId={}, count={}", courseId, students.size)

        return students
    }

    private fun ensureNotEnrolled(studentId: UUID, courseId: UUID) {
        val alreadyEnrolled = enrollmentRepository
            .existsByStudent_IdAndCourse_Id(studentId, courseId)

        if (alreadyEnrolled) {
            log.warn("Повторная запись на курс: courseId={}, studentId={}", courseId, studentId)
            throw IllegalStateException("Студент уже записан на этот курс")
        }
    }

    private fun findStudentOrThrow(studentId: UUID): User =
        userRepository.findById(studentId)
            .orElseThrow {
                log.warn("Студент не найден: {}", studentId)
                IllegalArgumentException("Студент не найден")
            }

    private fun findCourseOrThrow(courseId: UUID): Course =
        courseRepository.findById(courseId)
            .orElseThrow {
                log.warn("Курс не найден: {}", courseId)
                IllegalArgumentException("Курс не найден")
            }

    private fun findEnrollmentOrThrow(studentId: UUID, courseId: UUID): Enrollment =
        enrollmentRepository.findByStudent_IdAndCourse_Id(studentId, courseId)
            .orElseThrow {
                log.warn("Запись (enrollment) не найдена: courseId={}, studentId={}", courseId, studentId)
                IllegalArgumentException("Запись на курс не найдена")
            }
}