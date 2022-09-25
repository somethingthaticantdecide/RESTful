package edu.school21.restful.configs;

import edu.school21.restful.controllers.CoursesController;
import edu.school21.restful.models.Course;
import edu.school21.restful.models.enums.State;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CourseProcessor implements RepresentationModelProcessor<EntityModel<Course>> {

    @Override
    public EntityModel<Course> process(EntityModel<Course> model) {
        Course course = model.getContent();
        assert course != null;

        model.add(linkTo(CoursesController.class).withRel("courses"));
        model.add(linkTo(methodOn(CoursesController.class).getCourse(course.getId())).withSelfRel());
        if (course.getStudents().size() > 0)
            course.add(linkTo(methodOn(CoursesController.class).getStudentsByCourse(course.getId())).withRel("students"));
        if (course.getLessons().size() > 0)
            course.add(linkTo(methodOn(CoursesController.class).getLessonsByCourse(course.getId())).withRel("lessons"));
        if (course.getState().equals(State.DRAFT))
            model.add(linkTo(methodOn(CoursesController.class).publish(course.getId())).withRel("publish"));
        return model;
    }
}
