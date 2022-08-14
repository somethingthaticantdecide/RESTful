package edu.school21.restful.services;

import edu.school21.restful.models.Course;
import edu.school21.restful.models.User;
import edu.school21.restful.models.dto.CourseDto;
import edu.school21.restful.repository.CoursesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoursesService {
    private final CoursesRepository coursesRepository;

    public CoursesService(CoursesRepository coursesRepository) {
        this.coursesRepository = coursesRepository;
    }

    public List<Course> findAll() {
        return coursesRepository.findAll();
    }

    public Course findById(Long id) {
        return coursesRepository.findById(id).orElse(null);
    }

    public Course addCourse(CourseDto courseDto) {
        Course course = new Course(courseDto);
        coursesRepository.save(course);
        return course;
    }

    public void updateCourse(Course course, CourseDto courseDto) {

    }

    public void deleteById(Long id) {
        coursesRepository.deleteById(id);
    }

    public void save(Course course) {
        coursesRepository.save(course);
    }
}
