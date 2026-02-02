package v.orm.service

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertNotNull

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import v.orm.domain.category.entity.Category
import v.orm.domain.category.repository.CategoryRepository
import v.orm.domain.course.entity.Course
import v.orm.domain.course.repository.CourseRepository
import v.orm.domain.user.entity.User
import v.orm.domain.user.entity.UserRole
import v.orm.domain.user.repository.UserRepository
import kotlin.test.assertEquals


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CourseServiceTest {

    @Autowired
    lateinit var courseService: CourseService

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun `createCourse saves course with teacher and category`() {
        val teacher = saveTeacher()
        val category = saveCategory("Math")

        val course = courseService.createCourse(
            title = "Algebra 101",
            description = "Basic Algebra",
            categoryId = category.id,
            teacherId = teacher.id
        )

        val loaded = loadCourse(course.id!!)

        assertAll(
            { assertEquals("Algebra 101", loaded.title) },
            { assertEquals("Basic Algebra", loaded.description) },
            { assertNotNull(loaded.teacher) },
            { assertNotNull(loaded.category) },
            { assertEquals(teacher.id, loaded.teacher!!.id) },
            { assertEquals(category.id, loaded.category!!.id) },
        )
    }

    @Test
    fun `addModule adds module to course`() {
        val teacher = saveTeacher()
        val course = saveCourse(title = "Physics", teacher = teacher)

        val module = courseService.addModule(course.id!!, "Module 1", "Module description")

        val loaded = loadCourse(course.id!!)

        assertEquals(1, loaded.modules.size)
        assertEquals(module.id, loaded.modules.first().id)
        assertEquals("Module 1", loaded.modules.first().title)

        // если у Module есть поле course / courseId — полезно проверить связь:
        // assertEquals(course.id, loaded.modules.first().course!!.id)
    }

    @Test
    fun `updateCourse modifies existing course`() {
        val teacher1 = saveTeacher(name = "Teacher1", email = "t1@test.com")
        val teacher2 = saveTeacher(name = "Teacher2", email = "t2@test.com")
        val course = saveCourse(title = "Old Title", teacher = teacher1)

        courseService.updateCourse(
            courseId = course.id!!,
            title = "New Title",
            description = "Updated description",
            teacherId = teacher2.id
        )

        val updated = loadCourse(course.id!!)

        assertAll(
            { assertEquals("New Title", updated.title) },
            { assertEquals("Updated description", updated.description) },
            { assertEquals(teacher2.id, updated.teacher!!.id) },
        )
    }

    @Test
    fun `deleteCourse removes course`() {
        val course = saveCourse(title = "Course to delete")

        courseService.deleteCourse(course.id!!)

        assertFalse(courseRepository.existsById(course.id!!))
    }

    @Test
    fun `getCourse returns existing course`() {
        val teacher = saveTeacher()
        val course = saveCourse(title = "Course", teacher = teacher)

        val loaded = courseService.getCourse(course.id!!)

        assertAll(
            { assertEquals(course.id, loaded.id) },
            { assertEquals("Course", loaded.title) },
            { assertEquals(teacher.id, loaded.teacher!!.id) },
        )
    }


    private fun saveTeacher(
        name: String = "Teacher",
        email: String = "teacher@test.com"
    ): User =
        userRepository.save(User(name, email, UserRole.TEACHER))

    private fun saveCategory(name: String = "Math"): Category =
        categoryRepository.save(Category(name))

    private fun saveCourse(
        title: String = "Course",
        teacher: User = saveTeacher()
    ): Course =
        courseRepository.save(Course(title = title, teacher = teacher))

    private fun loadCourse(id: Any): Course =
        courseRepository.findById(id as java.util.UUID).orElseThrow {
            AssertionError("Course not found by id=$id")
        }
}