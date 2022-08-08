package edu.school21.restful.repository;

import edu.school21.restful.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface CoursesRepository extends JpaRepository<Course, Long> {
}
