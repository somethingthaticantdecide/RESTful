package edu.school21.restful.services;

import edu.school21.restful.models.Course;
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

    public Course addCourse(CourseDto courseDto) {
        Course course = new Course();
        course.setStartDate(courseDto.getStartDate());
        course.setEndDate(courseDto.getEndDate());
        course.setDescription(courseDto.getDescription());
        coursesRepository.save(course);
        return course;
    }
}
