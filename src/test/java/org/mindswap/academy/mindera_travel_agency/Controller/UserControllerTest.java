package org.mindswap.academy.mindera_travel_agency.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindswap.academy.mindera_travel_agency.model.User;
import org.mindswap.academy.mindera_travel_agency.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void init() {
        userRepository.deleteAll();
        userRepository.resetAutoIncrement();
    }

    @Test
    @DisplayName("Test get all users when no users are in the database returns an empty list")
    void TestGetAllUsersWhenNoUsersAreInTheDatabaseReturnsAnEmptyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Test create a user when user is valid returns status code 201.")
    void TestGetUserByIdWhenUserIsInTheDatabaseReturnsAUser() throws Exception {


        //Given
        String userJson = "{\"email\":\"joe@coldmail.com\",\"username\":\"joe\",\"dateOfBirth\":\"1990-01-01\"}";

        //When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andReturn();
        //Then
        String responseContent = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(responseContent, User.class);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUserName()).isEqualTo("joe");
        assertThat(user.getEmail()).isEqualTo("joe@coldmail.com");
    }

    @Test
    @DisplayName("Test get all users when 2 users are in the database returns a list with 2 students")
    void TestGetAllUsersWhen2UsersAreInTheDatabaseReturnsListWith2Users() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"joe@coldmail.com\",\"username\":\"joe\",\"dateOfBirth\":\"1990-01-01\"}");


        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"john@coldmail.com\",\"username\":\"john\",\"dateOfBirth\":\"1991-01-01\"}");


        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/1"));
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/2"));
    }

}
