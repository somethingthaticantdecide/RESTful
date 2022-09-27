package edu.school21.restful.controllers;

import edu.school21.restful.exceptions.NotFoundException;
import edu.school21.restful.models.Course;
import edu.school21.restful.models.Lesson;
import edu.school21.restful.models.User;
import edu.school21.restful.models.dto.CourseDto;
import edu.school21.restful.models.dto.LessonDto;
import edu.school21.restful.models.dto.UserDto;
import edu.school21.restful.models.enums.State;
import edu.school21.restful.models.requests.UserRequest;
import edu.school21.restful.services.CoursesService;
import edu.school21.restful.services.LessonService;
import edu.school21.restful.services.UsersService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
@Tag(name="My Courses controller", description="My Courses controller description")
public class CoursesController {

    private final CoursesService coursesService;
    private final LessonService lessonService;
    private final UsersService usersService;
    private final PagedResourcesAssembler<Course> pagedCourseResourcesAssembler;
    private final PagedResourcesAssembler<Lesson> pagedLessonResourcesAssembler;
    private final PagedResourcesAssembler<User> pageUserResourcesAssembler;

    @GetMapping(produces = { "application/hal+json" })
    @Operation(description = "Возвращает все имеющиеся курсы [есть пагинация, сортировка по ID]")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<EntityModel<Course>> getAllCourses(@PageableDefault(sort = "id", size = 10) Pageable pageable) {
        Page<Course> allCourses = coursesService.findAll(pageable);
        for (Course course : allCourses) {
            course.add(linkTo(CoursesController.class).slash(course.getId()).withSelfRel());
            course.add(linkTo(methodOn(CoursesController.class).getCourse(course.getId())).withRel("course"));
            if (course.getLessons() != null && course.getLessons().size() > 0) {
                course.add(linkTo(methodOn(CoursesController.class)).slash("lessons").withRel("lessons"));
            }
            if (course.getStudents() != null && course.getStudents().size() > 0) {
                course.add(linkTo(methodOn(CoursesController.class)).slash("students").withRel("students"));
            }
        }
        return pagedCourseResourcesAssembler.toModel(allCourses);
    }

    @GetMapping("/{course-id}")
    @Operation(description = "Возвращает курс по id")
    @ResponseStatus(HttpStatus.OK)
    public RepresentationModel<?> getCourse(@PathVariable("course-id") Long id) {
        return RepresentationModel.of(coursesService.findById(id));
    }

    @PostMapping(produces = { "application/hal+json" })
    @Operation(description = "Добавление нового курса")
    @ResponseStatus(HttpStatus.OK)
    public RepresentationModel<?> addCourse(@RequestBody CourseDto courseDto) {
        return RepresentationModel.of(coursesService.addCourse(courseDto));
    }

    @PutMapping(value = "{courseId}/publish")
    @Operation(description = "Публикация курса")
    @ResponseStatus(HttpStatus.OK)
    public RepresentationModel<?> publish(@PathVariable("courseId") @Parameter(description = "Идентификатор курса") Long courseId) {
        Course course = coursesService.findById(courseId);
        course.setState(State.PUBLISHED);
        return RepresentationModel.of(coursesService.save(course));
    }

    @PutMapping("/{course-id}")
    @Operation(description = "Обновление информации о курсе")
    @ResponseStatus(HttpStatus.OK)
    public RepresentationModel<?> updateCourse(@RequestBody CourseDto courseDto, @PathVariable("course-id") String courseId) {
        return RepresentationModel.of(coursesService.updateCourse(courseDto, courseId));
    }

    @DeleteMapping("/{course-id}")
    @Operation(description = "Удаление курса")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable("course-id") String id) {
        coursesService.deleteById(Long.valueOf(id));
    }

    @GetMapping("/{course-id}/lessons")
    @Operation(description = "Возвращает уроки из курса")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<EntityModel<Lesson>> getLessonsByCourse(@PathVariable("course-id") Long id, @PageableDefault(sort = "id", size = 10) Pageable pageable) {
        Page<Lesson> lessons = lessonService.getLessonsByCourse(id, pageable);
        return pagedLessonResourcesAssembler.toModel(lessons);
    }

    @PostMapping("/{course-id}/lessons")
    @Operation(description = "Добавляет урок в курс")
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
    @Operation(description = "Изменяет урок в курсе")
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
    @Operation(description = "Добавляет урок в курс")
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
    @Operation(description = "Возвращает студентов курса")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<EntityModel<User>> getStudentsByCourse(@PathVariable("course-id") Long id, @PageableDefault(sort = "id", size = 10) Pageable pageable) {
        Page<User> students = usersService.getStudentByCourse(id, pageable);
        return pageUserResourcesAssembler.toModel(students);
    }

    @PostMapping("/{course-id}/students")
    @Operation(description = "Добавляет студента на курс")
    @ResponseStatus(HttpStatus.OK)
    public User addStudentsByCourse(@RequestBody UserRequest userRequest, @PathVariable("course-id") Long courseId) {
        Course course = coursesService.findById(courseId);
        if (userRequest.getUserId() == null) throw new NotFoundException();
        User user = usersService.findById(userRequest.getUserId());
        if (!course.getStudents().contains(user))
            course.getStudents().add(user);
        coursesService.save(course);
        return user;
    }

    @DeleteMapping("/{course-id}/students/{student-id}")
    @Operation(description = "Удаляет студента с курса")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudentFromCourse(@PathVariable("course-id") String courseId, @PathVariable("student-id") String studentId) {
        Course course = coursesService.findById(Long.valueOf(courseId));
        course.getStudents().remove(usersService.findById(Long.valueOf(studentId)));
        coursesService.save(course);
    }

    @GetMapping("/{course-id}/teachers")
    @Operation(description = "Возвращает учителей курса")
    @ResponseStatus(HttpStatus.OK)
    public PagedModel<EntityModel<User>> getTeachersByCourse(@PathVariable("course-id") Long id, @PageableDefault(sort = "id", size = 10) Pageable pageable) {
        Page<User> teachers = usersService.getTeachersByCourse(id, pageable);
        return pageUserResourcesAssembler.toModel(teachers);
    }

    @PostMapping("/{course-id}/teachers")
    @Operation(description = "Добавляет учителя на курса")
    @ResponseStatus(HttpStatus.OK)
    public User addTeachersByCourse(@RequestBody UserRequest userRequest, @PathVariable("course-id") Long courseId) {
        Course course = coursesService.findById(courseId);
        if (userRequest.getUserId() == null) throw new NotFoundException();
        User user = usersService.findById(userRequest.getUserId());
        if (!course.getTeachers().contains(user))
            course.getTeachers().add(user);
        coursesService.save(course);
        return user;
    }

    @DeleteMapping("/{course-id}/teachers/{teacher-id}")
    @Operation(description = "Удаляет учителя с курса")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeacherFromCourse(@PathVariable("course-id") String courseId, @PathVariable("teacher-id") String teacherId) {
        Course course = coursesService.findById(Long.valueOf(courseId));
        course.getTeachers().remove(usersService.findById(Long.valueOf(teacherId)));
        coursesService.save(course);
    }
}
