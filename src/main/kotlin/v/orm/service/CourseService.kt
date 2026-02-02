package v.orm.service


import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import v.orm.domain.category.entity.Category
import v.orm.domain.category.repository.CategoryRepository
import v.orm.domain.course.entity.Course
import v.orm.domain.course.repository.CourseRepository
import v.orm.domain.module.entity.Module
import v.orm.domain.module.repository.ModuleRepository
import v.orm.domain.user.entity.User
import v.orm.domain.user.repository.UserRepository
import java.util.*

@Service
@Transactional
class CourseService(
    private val courseRepository: CourseRepository,
    private val categoryRepository: CategoryRepository,
    private val userRepository: UserRepository,
    private val moduleRepository: ModuleRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun createCourse(
        title: String,
        description: String?,
        categoryId: UUID?,
        teacherId: UUID?
    ): Course {
        log.info("Запрос на создание курса: title='{}'", title)

        validateCourseTitle(title)

        val category = categoryId?.let { findCategoryOrThrow(it) }
        val teacher = teacherId?.let { findTeacherOrThrow(it) }

        val course = buildCourse(title, description, category, teacher)

        val saved = courseRepository.save(course)

        log.info("Курс успешно создан: courseId={}", saved.id)

        return saved
    }

    fun addModule(
        courseId: UUID,
        title: String,
        description: String?
    ): Module {
        log.info(
            "Добавление модуля в курс: courseId={}, title='{}'",
            courseId,
            title
        )

        validateModuleTitle(title)

        val course = findCourseOrThrow(courseId)

        val module = buildModule(course, title, description)

        course.modules.add(module)

        val saved = moduleRepository.save(module)

        log.info(
            "Модуль добавлен: moduleId={}, courseId={}",
            saved.id,
            courseId
        )

        return saved
    }

    fun updateCourse(
        courseId: UUID,
        title: String?,
        description: String?,
        teacherId: UUID?
    ) {
        log.info("Обновление курса: courseId={}", courseId)

        val course = findCourseOrThrow(courseId)

        updateTitleIfPresent(course, title)
        updateDescriptionIfPresent(course, description)
        updateTeacherIfPresent(course, teacherId)

        courseRepository.save(course)

        log.info("Курс обновлён: courseId={}", courseId)
    }

    fun deleteCourse(courseId: UUID) {
        log.warn("Запрос на удаление курса: courseId={}", courseId)

        if (!courseRepository.existsById(courseId)) {
            log.error("Попытка удалить несуществующий курс: {}", courseId)
            throw IllegalArgumentException("Курс не найден")
        }

        courseRepository.deleteById(courseId)

        log.info("Курс удалён: courseId={}", courseId)
    }


    fun getCourse(courseId: UUID): Course {
        log.debug("Запрос курса по id: {}", courseId)

        val course = findCourseOrThrow(courseId)

        log.debug("Курс найден: courseId={}", courseId)

        return course
    }

    private fun validateCourseTitle(title: String) {
        if (title.isBlank()) {
            log.warn("Попытка создать курс с пустым названием")
            throw IllegalArgumentException("Название курса не может быть пустым")
        }
    }


    private fun validateModuleTitle(title: String) {
        if (title.isBlank()) {
            log.warn("Попытка создать модуль с пустым названием")
            throw IllegalArgumentException("Название модуля не может быть пустым")
        }
    }

    private fun findCourseOrThrow(id: UUID): Course =
        courseRepository.findById(id)
            .orElseThrow {
                log.warn("Курс не найден: {}", id)
                IllegalArgumentException("Курс не найден")
            }

    private fun findCategoryOrThrow(id: UUID): Category =
        categoryRepository.findById(id)
            .orElseThrow {
                log.warn("Категория не найдена: {}", id)
                IllegalArgumentException("Категория не найдена")
            }

    private fun findTeacherOrThrow(id: UUID): User =
        userRepository.findById(id)
            .orElseThrow {
                log.warn("Преподаватель не найден: {}", id)
                IllegalArgumentException("Преподаватель не найден")
            }

    private fun updateTitleIfPresent(course: Course, title: String?) {
        if (title == null) return

        if (title.isBlank()) {
            log.warn("Попытка установить пустое название курса: courseId={}", course.id)
            throw IllegalArgumentException("Название курса не может быть пустым")
        }

        log.debug(
            "Обновление названия курса: courseId={}, newTitle='{}'",
            course.id,
            title
        )

        course.title = title
    }

    private fun updateDescriptionIfPresent(
        course: Course,
        description: String?
    ) {
        if (description == null) return

        log.debug(
            "Обновление описания курса: courseId={}",
            course.id
        )

        course.description = description
    }

    private fun updateTeacherIfPresent(
        course: Course,
        teacherId: UUID?
    ) {
        if (teacherId == null) return

        val teacher = findTeacherOrThrow(teacherId)

        log.debug(
            "Обновление преподавателя курса: courseId={}, teacherId={}",
            course.id,
            teacherId
        )

        course.teacher = teacher
    }

    private fun buildCourse(
        title: String,
        description: String?,
        category: Category?,
        teacher: User?
    ): Course =
        Course(
            title = title,
            description = description,
            category = category,
            teacher = teacher
        )

    private fun buildModule(
        course: Course,
        title: String,
        description: String?
    ): Module =
        Module(
            course = course,
            title = title,
            description = description
        )
}