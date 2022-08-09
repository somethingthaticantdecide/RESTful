package edu.school21.restful.services;

import edu.school21.restful.models.Course;
import edu.school21.restful.models.dto.CourseDto;
import edu.school21.restful.repository.CoursesRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

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
        course.setStartDate(LocalDate.parse(courseDto.getStartDate(), ISO_LOCAL_DATE));
//        course.setEndDate(courseDto.getEndDate());
        coursesRepository.save(course);
        return course;
    }
}
