package edu.school21.restful.repository;

import edu.school21.restful.models.Lesson;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Query("select c.lessons from Course c where c.id=:courseId")
    List<Lesson> findLessonsByCourse(@Param("courseId") Long courseId, Pageable pageable);
}
