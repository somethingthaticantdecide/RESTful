package edu.school21.restful;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import edu.school21.restful.models.Course;
import edu.school21.restful.models.Lesson;
import edu.school21.restful.models.User;
import edu.school21.restful.models.dto.UserDto;
import edu.school21.restful.models.enums.State;
import edu.school21.restful.models.roles.Role;
import edu.school21.restful.repository.CoursesRepository;
import edu.school21.restful.repository.LessonRepository;
import edu.school21.restful.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.RestMediaTypes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs("target/generated-snippets")
class PublishCourseTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CoursesRepository coursesRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private UserRepository userRepository;

    private String token;
    private final ObjectMapper mapper = new ObjectMapper();

    public String signUp(UserDto userDto) throws Exception {
        String content = mapper.writeValueAsString(userDto);
        String json = mvc.perform(post("/signUp")
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .accept(APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(json, "$.token");
    }

    @BeforeEach
    public void init() throws Exception {
        token = signUp(new UserDto("firstname", "lastname", "ROLE_ADMIN", "login", "password"));
        Course course = coursesRepository.findById(1L).orElse(null);
        if (course == null) {
            course = new Course();
            course.setName("name course");
            course.setStartDate(Date.valueOf(LocalDate.now()));
            course.setEndDate(Date.valueOf(LocalDate.now()));
            course.setDescription("test course");
            Lesson lesson = new Lesson();
            lessonRepository.save(lesson);
            ArrayList<Lesson> lessons = new ArrayList<>();
            lessons.add(lesson);
            course.setLessons(lessons);
            User student = new User("student", "pass", Role.ROLE_STUDENT);
            userRepository.save(student);
            ArrayList<User> students = new ArrayList<>();
            students.add(student);
            course.setStudents(students);
            User teacher = new User("teacher", "pass", Role.ROLE_TEACHER);
            userRepository.save(teacher);
            ArrayList<User> teachers = new ArrayList<>();
            teachers.add(teacher);
            course.setTeachers(teachers);
        }
        course.setState(State.DRAFT);
        coursesRepository.save(course);
    }

    @Test
    public void publishTest() throws Exception {
        ResultActions resultActions = mvc.perform(put("/courses/1/publish")
                        .header("Authorization", "Bearer " + token)
                        .accept(RestMediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        resultActions.andDo(document("publishTest"));
    }
}
