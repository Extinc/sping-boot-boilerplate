
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import design.boilerplate.springboot.controller.ApiController;
import design.boilerplate.springboot.model.User;
import design.boilerplate.springboot.model.UserRole;

import design.boilerplate.springboot.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.nio.charset.Charset;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@RunWith(MockitoJUnitRunner.class)
@AutoConfigureMockMvc
public class ApiControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ApiController apiController;

    @Mock
    private UserRepository userRepository;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(apiController).build();
    }


    @Test
    public void testCreateCustomUser() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();

        User customUser = new User();
        customUser.setId(1L);
        customUser.setEmail("keiyiang@testcode.com");
        customUser.setUsername("keiyiang1");
        customUser.setName("keiyiang");
        customUser.setPassword("mypass");
        customUser.setUserRole(UserRole.USER);
        String json = objectMapper.writeValueAsString(customUser);
        System.out.println();
        mockMvc.perform(post("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.email", Matchers.is(customUser.getEmail())));
    }


    @Test
    public void testGetListOfUser() throws Exception {

        mockMvc.perform(get("/api/user/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetSpecificUser() throws Exception {

        User customUser = new User();
        customUser.setId(1L);
        customUser.setEmail("keiyiang@testcode.com");
        customUser.setUsername("keiyiang1");
        customUser.setName("keiyiang");
        customUser.setPassword("mypass");
        customUser.setUserRole(UserRole.USER);

        System.out.println();

        mockMvc.perform(get("/api/user/keiyiang1/retrieve")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateSpecificUser() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();

        User customUser = new User();
        customUser.setId(1L);
        customUser.setEmail("keiyiang@testcode.com");
        customUser.setUsername("keiyiang1");
        customUser.setName("keiyiang");
        customUser.setPassword("mypass");
        customUser.setUserRole(UserRole.USER);
        String json = objectMapper.writeValueAsString(customUser);
        System.out.println();

        mockMvc.perform(put("/api/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteUser() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();

        User customUser = new User();
        customUser.setId(1L);
        customUser.setEmail("keiyiang@testcode.com");
        customUser.setUsername("keiyiang1");
        customUser.setName("keiyiang");
        customUser.setPassword("mypass");
        customUser.setUserRole(UserRole.USER);
        String json = objectMapper.writeValueAsString(customUser);
        System.out.println();

        mockMvc.perform(delete("/api/user/keiyiang1/delete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
