package edu.school21.restful.repository;

import edu.school21.restful.models.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
}
