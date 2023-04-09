package edu.school21.restful.repository;

import edu.school21.restful.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.transaction.Transactional;

@Transactional
@RepositoryRestResource
public interface CoursesRepository extends JpaRepository<Course, Long> {
}
