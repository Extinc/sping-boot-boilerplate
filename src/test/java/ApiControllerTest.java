
import com.fasterxml.jackson.databind.ObjectMapper;
import design.boilerplate.springboot.controller.ApiController;
import design.boilerplate.springboot.model.User;
import design.boilerplate.springboot.model.UserRole;

import design.boilerplate.springboot.repository.UserRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.hamcrest.Matchers;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

// Setup Mockito
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApiControllerTest {

    // Declare MockMvc Obj
    private MockMvc mockMvc;

    // INject the ApiController
    @InjectMocks
    private ApiController apiController;

    // Annotation to create mock object
    @Mock
    private UserRepository userRepository;

    // Set up Test Environment
    @BeforeAll
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
                .andExpect(status().isOk());
    }

    @Test
    public void testGetListOfUser() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("keiyiang@testcode.com")
                .username("keiyiang1")
                .password("keiyiangpassword")
                .userRole(UserRole.USER)
                .build();

        User userTwo = User.builder()
                .id(1L)
                .email("keiyiang2@testcode.com")
                .username("keiyiang21")
                .password("keiyiangpassword")
                .userRole(UserRole.USER)
                .build();

        List<User> userList = new ArrayList<User>();
        userList.add(user);
        userList.add(userTwo);

        given(userRepository.findAll()).willReturn(userList);

        mockMvc.perform(get("/api/user/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)));
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

        given(userRepository.findByUsername("keiyiang1")).willReturn(customUser);

        mockMvc.perform(get("/api/user/keiyiang1/retrieve")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.username", is(customUser.getUsername())));
    }

    @Test
    public void testUpdateSpecificUser() throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();

        User beforeUpdateUser = User.builder()
                .id(1L)
                .email("keiyiang@testcode.com")
                .username("keiyiang1")
                .password("keiyiangpassword")
                .userRole(UserRole.USER)
                .build();

        User afterUpdateUser = User.builder()
                .id(1L)
                .email("keiyiang@testcode.com")
                .username("keiyiang2")
                .password("keiyiangpassword")
                .userRole(UserRole.USER)
                .build();
        // This is used to mock object
        given(userRepository.existsByUsername("keiyiang1")).willReturn(true);
        given(userRepository.existsByEmail("keiyiang@testcode.com")).willReturn(true);

        given(userRepository.save(beforeUpdateUser)).willReturn(beforeUpdateUser);
        given(userRepository.save(afterUpdateUser)).willReturn(afterUpdateUser);

        String json = objectMapper.writeValueAsString(afterUpdateUser);
        System.out.println();

        mockMvc.perform(put("/api/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteUser() throws Exception {

        User user = User.builder()
                .id(1L)
                .email("keiyiang@testcode.com")
                .username("keiyiang1")
                .password("keiyiangpassword")
                .userRole(UserRole.USER)
                .build();
        // given - precondition or setup
        given(userRepository.existsByUsername(user.getUsername())).willReturn(true);
        given(userRepository.findByUsername(user.getUsername())).willReturn(user);
        long userId = 1L;
        willDoNothing().given(userRepository).deleteById(userId);

        mockMvc.perform(delete("/api/user/keiyiang1/delete"))
                .andExpect(status().isOk());

    }
}
