package edu.school21.restful.services;

import edu.school21.restful.exceptions.NotFoundException;
import edu.school21.restful.models.Course;
import edu.school21.restful.models.dto.CourseDto;
import edu.school21.restful.repository.CoursesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoursesService {
    private final CoursesRepository courseRepository;

    public CoursesService(CoursesRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Course findById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    public Course addCourse(CourseDto courseDto) {
        return courseRepository.save(new Course(courseDto.getStartDate(), courseDto.getEndDate(), courseDto.getName(), courseDto.getDescription()));
    }

    public Course updateCourse(CourseDto courseDto, String id) {
        Course course = courseRepository.findById(Long.parseLong(id)).orElseThrow(NotFoundException::new);
        course.setStartDate(courseDto.getStartDate());
        course.setEndDate(courseDto.getEndDate());
        course.setName(courseDto.getName());
        course.setDescription(courseDto.getDescription());
        return courseRepository.saveAndFlush(course);
    }

    public void deleteById(Long id) {
        courseRepository.deleteById(id);
    }

    public void save(Course course) {
        courseRepository.save(course);
    }
}
