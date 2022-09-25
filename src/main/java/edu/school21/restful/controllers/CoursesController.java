package edu.school21.restful.controllers;

import edu.school21.restful.models.Course;
import edu.school21.restful.models.Lesson;
import edu.school21.restful.models.User;
import edu.school21.restful.models.dto.CourseDto;
import edu.school21.restful.models.dto.LessonDto;
import edu.school21.restful.models.dto.UserDto;
import edu.school21.restful.models.enums.State;
import edu.school21.restful.services.CoursesService;
import edu.school21.restful.services.LessonService;
import edu.school21.restful.services.UsersService;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CoursesController {

    private final CoursesService coursesService;
    private final LessonService lessonService;
    private final UsersService usersService;

    @GetMapping(produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<Course> getAllCourses() {
        List<Course> allCourses = coursesService.findAll();
        for (Course course : allCourses) {
            course.add(linkTo(CoursesController.class).slash(course.getId()).withSelfRel());
            course.add(linkTo(methodOn(CoursesController.class).getCourse(course.getId())).withRel("course"));
            if (course.getLessons().size() > 0) {
                course.add(linkTo(methodOn(CoursesController.class).getLessonsByCourse(course.getId())).withRel("lessons"));
            }
            if (course.getStudents().size() > 0) {
                course.add(linkTo(methodOn(CoursesController.class).getStudentsByCourse(course.getId())).withRel("students"));
            }
        }
        Link link = linkTo(CoursesController.class).withSelfRel();
        return CollectionModel.of(allCourses, link);
    }

    @PostMapping(produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.OK)
    public RepresentationModel<?> addCourse(@RequestBody CourseDto courseDto) {
        return RepresentationModel.of(coursesService.addCourse(courseDto));
    }

    @GetMapping("/{course-id}")
    public RepresentationModel<?> getCourse(@PathVariable("course-id") Long id) {
        return RepresentationModel.of(coursesService.findById(id));
    }

    @PutMapping(value = "{courseId}/publish")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RepresentationModel<?> publish(@PathVariable("courseId") Long courseId) {
        Course course = coursesService.findById(courseId);
        course.setState(State.PUBLISHED);
        return RepresentationModel.of(coursesService.save(course));
    }

    //////////////////////////////////////////////////////////////

    @PutMapping("/{course-id}")
    @ResponseStatus(HttpStatus.OK)
    public RepresentationModel<?> updateCourse(@RequestBody CourseDto courseDto, @PathVariable("course-id") String courseId) {
        return RepresentationModel.of(coursesService.updateCourse(courseDto, courseId));
    }

    @DeleteMapping("/{course-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable("course-id") String id) {
        coursesService.deleteById(Long.valueOf(id));
    }

    @GetMapping("/{course-id}/lessons")
    @ResponseStatus(HttpStatus.OK)
    public List<Lesson> getLessonsByCourse(@PathVariable("course-id") Long id) {
        return coursesService.findById(id).getLessons();
    }

    @PostMapping("/{course-id}/lessons")
    @ResponseStatus(HttpStatus.OK)
    public Lesson addLessonsByCourse(@RequestBody LessonDto lessonDto, @PathVariable("course-id") Long id) {
        Course course = coursesService.findById(id);
        Lesson lesson = new Lesson(lessonDto);
        lesson.setTeacher(usersService.findById(Long.valueOf(lessonDto.getTeacher())));
        course.getLessons().add(lesson);
        coursesService.save(course);
        return lesson;
    }

    @PutMapping("/{course-id}/lessons/{lesson-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLessonsInCourse(@RequestBody LessonDto lessonDto, @PathVariable("course-id") String courseId, @PathVariable("lesson-id") String lessonId) {
        Course course = coursesService.findById(Long.valueOf(courseId));
        List<Lesson> lessons = course.getLessons();
        for (Lesson lesson : lessons) {
            if (lesson.getId().equals(Long.valueOf(lessonId))) {
                lesson.setStartTime(lesson.getStartTime());
                lesson.setEndTime(lessonDto.getEndTime());
                lesson.setDayOfWeek(lessonDto.getDayOfWeek());
                lesson.setTeacher(usersService.findById(Long.valueOf(lessonDto.getTeacher())));
                lessonService.save(lesson);
            }
        }
        coursesService.save(course);
    }

    @DeleteMapping("/{course-id}/lessons/{lesson-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLessonFromCourse(@PathVariable("course-id") String courseId, @PathVariable("lesson-id") String lessonId) {
        Course course = coursesService.findById(Long.valueOf(courseId));
        List<Lesson> lessons = course.getLessons();
        for (Lesson lesson : lessons) {
            if (lesson.getId().equals(Long.valueOf(lessonId))) {
                lessons.remove(lesson);
                break;
            }
        }
    }

    @GetMapping("/{course-id}/students")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getStudentsByCourse(@PathVariable("course-id") Long id) {
        return coursesService.findById(id).getStudents();
    }

    @PostMapping("/{course-id}/students")
    @ResponseStatus(HttpStatus.OK)
    public User addStudentsByCourse(@RequestBody UserDto userDto, @PathVariable("course-id") Long id) {
        Course course = coursesService.findById(id);
        User user = new User(userDto);
        course.getStudents().add(user);
        coursesService.save(course);
        return user;
    }

    @DeleteMapping("/{course-id}/students/{student-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudentFromCourse(@PathVariable("course-id") String courseId, @PathVariable("student-id") String studentId) {
        Course course = coursesService.findById(Long.valueOf(courseId));
        course.getStudents().remove(usersService.findById(Long.valueOf(studentId)));
        coursesService.save(course);
    }

    @GetMapping("/{course-id}/teachers")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getTeachersByCourse(@PathVariable("course-id") String id) {
        return coursesService.findById(Long.valueOf(id)).getTeachers();
    }

    @PostMapping("/{course-id}/teachers")
    @ResponseStatus(HttpStatus.OK)
    public User addTeachersByCourse(@RequestBody UserDto userDto, @PathVariable("course-id") String id) {
        Course course = coursesService.findById(Long.valueOf(id));
        User user = new User(userDto);
        course.getTeachers().add(user);
        coursesService.save(course);
        return user;
    }

    @DeleteMapping("/{course-id}/teachers/{teacher-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeacherFromCourse(@PathVariable("course-id") String courseId, @PathVariable("teacher-id") String teacherId) {
        Course course = coursesService.findById(Long.valueOf(courseId));
        course.getTeachers().remove(usersService.findById(Long.valueOf(teacherId)));
        coursesService.save(course);
    }
}
