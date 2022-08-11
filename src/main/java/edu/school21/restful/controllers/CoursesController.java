package edu.school21.restful.controllers;

import edu.school21.restful.models.Course;
import edu.school21.restful.models.dto.CourseDto;
import edu.school21.restful.services.CoursesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CoursesController {

    private final CoursesService coursesService;

    public CoursesController(CoursesService coursesService) {
        this.coursesService = coursesService;
    }

    @GetMapping()
    public ResponseEntity<?> getAllCourses() {
        try {
            List<Course> coursePage = coursesService.findAll();
            return ResponseEntity.ok(coursePage);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping()
    public ResponseEntity<Course> addCourse(@RequestBody CourseDto courseDto) {
        try {
            Course course = coursesService.addCourse(courseDto);
            return ResponseEntity.ok(course);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
