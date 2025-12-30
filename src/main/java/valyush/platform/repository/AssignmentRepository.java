package valyush.platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import valyush.platform.entity.Assignment;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByLessonId(Long lessonId);

    @Query("SELECT a FROM Assignment a JOIN a.lesson l WHERE l.courseModule.course.id = :courseId")
    List<Assignment> findByCourseId(@Param("courseId") Long courseId);
}
