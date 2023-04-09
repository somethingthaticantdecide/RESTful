package edu.school21.restful;

import edu.school21.restful.models.JwtRequest;
import edu.school21.restful.models.dto.UserDto;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs("target/generated-snippets")
public class ResTfulApplicationTests {
    @Autowired
    private MockMvc mockMvc;
//    @MockBean
//    private UserRepository userRepository;
    private final ObjectMapper mapper = new ObjectMapper();
    private String token;
    private final String username = "firstname";
    private final String password = "lastname";

    @BeforeEach
    public void setUp() {
//        when(userRepository.findUserByFirstname(username)).thenCallRealMethod();
//        doNothing().when(userRepository).deleteById(1L);
//        userRepository.save(new User(new UserDto(username, "lastname", "ROLE_ADMIN", "login", password)));
    }

    @Before
    public void init() throws Exception {
//        authenticate(username, password, "/authenticate");
        signUp(new UserDto(username, "lastname", "ROLE_ADMIN", "login", password));
    }

    private void authenticate(String username, String password, String url) throws Exception {
        String content = mapper.writeValueAsString(new JwtRequest(username, password));
        String json = mockMvc.perform(post(url)
                .content(content)
                .contentType(APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        token = JsonPath.read(json, "$.token");
    }

    public void signUp(UserDto userDto) throws Exception {
        String content = mapper.writeValueAsString(userDto);
        String json = mockMvc.perform(post("/signUp")
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .accept(APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        token = JsonPath.read(json, "$.token");
    }

    @Test
    public void userUnauthorizedTest() throws Exception {
        UserDto userDto = new UserDto("firstname", "lastname", "ROLE_STUDENT", "login", "password");
        String content = mapper.writeValueAsString(userDto);
        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .accept(APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getUsersTest() throws Exception {
        mockMvc.perform(get("/users")).andExpect(status().isOk());
    }

    @Test
    public void addUserTest() throws Exception {
        UserDto userDto = new UserDto("firstname", "lastname", "ROLE_STUDENT", "login", "password");
        String content = mapper.writeValueAsString(userDto);
        mockMvc.perform(post("/users")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname").value("firstname"))
                .andExpect(jsonPath("$.lastname").value("lastname"))
                .andExpect(jsonPath("$.login").value("login"));
    }

    @Test
    public void updateUserTest() throws Exception {
        UserDto userDto = new UserDto("updated_firstname", "lastname", "ROLE_STUDENT", "login", "password");
        String content = mapper.writeValueAsString(userDto);
        mockMvc.perform(put("/users/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("updated_firstname"));
    }

    @Test
    public void deleteCourseTest() throws Exception {
        mockMvc.perform(delete("/users/1").header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}