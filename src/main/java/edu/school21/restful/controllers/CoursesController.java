package edu.school21.restful.controllers;

import edu.school21.restful.models.Course;
import edu.school21.restful.models.Lesson;
import edu.school21.restful.models.User;
import edu.school21.restful.models.dto.CourseDto;
import edu.school21.restful.models.dto.LessonDto;
import edu.school21.restful.models.dto.UserDto;
import edu.school21.restful.services.CoursesService;
import edu.school21.restful.services.LessonService;
import edu.school21.restful.services.UsersService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/courses")
public class CoursesController {

    private final CoursesService coursesService;
    private final LessonService lessonService;
    private final UsersService usersService;

    public CoursesController(CoursesService coursesService, LessonService lessonService, UsersService usersService) {
        this.coursesService = coursesService;
        this.lessonService = lessonService;
        this.usersService = usersService;
    }

    @GetMapping(produces = { "application/hal+json" })
    @ResponseStatus(HttpStatus.OK)
    public Collection<Course> getAllCourses() {
        List<Course> allCourses = coursesService.findAll();

        for (Course course : allCourses) {
            String courseId = String.valueOf(course.getId());
            course.add(linkTo(CoursesController.class).slash(courseId).withSelfRel());
            course.add(linkTo(methodOn(CoursesController.class).getCourse(courseId)).withRel("course"));

            if (course.getLessons().size() > 0) {
                course.add(linkTo(methodOn(CoursesController.class)
                    .getLessonsByCourse(courseId)).withRel("lessons"));
            }
            if (course.getStudents().size() > 0) {
                course.add(linkTo(methodOn(CoursesController.class)
                    .getStudentsByCourse(courseId)).withRel("students"));
            }
        }
//        Link link = linkTo(CoursesController.class).withSelfRel();
//        CollectionModel<Course> result = CollectionModel.of(allCourses, link);
        return allCourses;
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

    //////////////////////////////////////////////////////////////

    @PutMapping("/{course-id}")
    @ResponseStatus(HttpStatus.OK)
    public Course updateCourse(@RequestBody CourseDto courseDto, @PathVariable("course-id") String courseId) {
        return coursesService.updateCourse(courseDto, courseId);
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
        User user = new User();
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
