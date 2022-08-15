package edu.school21.restful.controllers;

import edu.school21.restful.models.Course;
import edu.school21.restful.models.Lesson;
import edu.school21.restful.models.User;
import edu.school21.restful.models.dto.CourseDto;
import edu.school21.restful.models.dto.LessonDto;
import edu.school21.restful.models.dto.UserDto;
import edu.school21.restful.services.CoursesService;
import edu.school21.restful.services.LessonService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CoursesController {

    private final CoursesService coursesService;
    private final LessonService lessonService;

    public CoursesController(CoursesService coursesService, LessonService lessonService) {
        this.coursesService = coursesService;
        this.lessonService = lessonService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Collection<Course> getAllCourses() {
        return coursesService.findAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public Course addCourse(@RequestBody CourseDto courseDto) {
        return coursesService.addCourse(courseDto);
    }

    @GetMapping("/{course-id}")
    @ResponseStatus(HttpStatus.OK)
    public Course getCourse(@PathVariable("course-id") String id) {
        return coursesService.findById(Long.valueOf(id));
    }

    @PutMapping("/{course-id}")
    @ResponseStatus(HttpStatus.OK)
    public Course updateCourse(@RequestBody CourseDto courseDto, @PathVariable("course-id") String id) {
        Course course = coursesService.findById(Long.valueOf(id));
        coursesService.updateCourse(course, courseDto);
        return course;
    }

    @DeleteMapping("/{course-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable("course-id") String id) {
        coursesService.deleteById(Long.valueOf(id));
    }

    @GetMapping("/{course-id}/lessons")
    @ResponseStatus(HttpStatus.OK)
    public List<Lesson> getLessonsByCourse(@PathVariable("course-id") String id) {
        return coursesService.findById(Long.valueOf(id)).getLessons();
    }

    @PostMapping("/{course-id}/lessons")
    @ResponseStatus(HttpStatus.OK)
    public Lesson addLessonsByCourse(@RequestBody LessonDto lessonDto, @PathVariable("course-id") String id) {
        Course course = coursesService.findById(Long.valueOf(id));
        Lesson lesson = new Lesson();
        course.getLessons().add(lesson);
        coursesService.save(course);
        return lesson;
    }

    @PutMapping("/{course-id}/lessons/{lesson-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLessonsInCourse(@RequestBody LessonDto lessonDto, @PathVariable("course-id") String course, @PathVariable("lesson-id") String lesson) {
    }

    @DeleteMapping("/{course-id}/lessons/{lesson-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLessonFromCourse(@PathVariable("course-id") String course, @PathVariable("lesson-id") String lesson) {
    }

    @GetMapping("/{course-id}/students")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getStudentsByCourse(@PathVariable("course-id") String id) {
        return coursesService.findById(Long.valueOf(id)).getStudents();
    }

    @PostMapping("/{course-id}/students")
    @ResponseStatus(HttpStatus.OK)
    public User addStudentsByCourse(@RequestBody UserDto userDto, @PathVariable("course-id") String id) {
        Course course = coursesService.findById(Long.valueOf(id));
        User user = new User();
        course.getStudents().add(user);
        coursesService.save(course);
        return user;
    }

    @DeleteMapping("/{course-id}/students/{student-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudentFromCourse(@PathVariable("course-id") String id, @PathVariable("student-id") String parameter) {
        coursesService.deleteById(Long.valueOf(id));
        Course course = coursesService.findById(Long.valueOf(id));
        User user = new User();
        course.getStudents().add(user);
        coursesService.save(course);
    }

}
