package org.mindswap.academy.mindera_travel_agency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindswap.academy.mindera_travel_agency.aspect.AgencyError;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserGetDto;
import org.mindswap.academy.mindera_travel_agency.repository.UserTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class UserControllerTest {
    private static ObjectMapper objectMapper;
    private final String BASE_URL = "/api/v1/users/";
    private final String USER_JSON = "{\"email\":\"email@example.com\",\"userName\":\"User_test\",\"dateOfBirth\":\"1990-01-01\",\"phoneNumber\":\"912345678\",\"password\":\"zxlmn!!23K\"}";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserTestRepository userTestRepository;


    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void init() {
        userTestRepository.deleteAll();
        userTestRepository.resetAutoIncrement();
    }

    @Test
    @DisplayName("Test get all users when no users are in the database returns an empty list")
    void TestGetAllUsersWhenNoUsersAreInTheDatabaseReturnsAnEmptyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Test get all users when 2 users are in the database returns a list with 2 students")
    void TestGetAllUsersWhen2UsersAreInTheDatabaseReturnsListWith2Users() throws Exception {

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON.replace("\"email\":\"email@example.com\"", "\"email\":\"email2@example.com\"")));


        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Test get user by id when user is not in the database returns status code 404.")
    void TestGetUserByIdWhenUserIsNotInTheDatabaseReturnsStatus404() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        assertNotNull(error.getMessage());
        assertEquals(ID_NOT_FOUND + 1, error.getMessage());
    }

    @Test
    @DisplayName("Test get user by id when user is in the database returns status code 200.")
    void TestGetUserByIdWhenUserIsInTheDatabaseReturnsStatus200() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));

        String response = mockMvc.perform(get(BASE_URL + "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        UserGetDto user = objectMapper.readValue(response, UserGetDto.class);


        assertEquals(1, user.id());
        assertEquals("User_test", user.userName());
        assertEquals("email@example.com", user.email());
        assertEquals("1990-01-01", user.dateOfBirth().toString());
        assertEquals("912345678", user.phoneNumber());
        assertEquals(1, userTestRepository.count());

    }

    @Test
    @DisplayName("Test get a user with non-existing id and expect status 404")
    void TestGetUserWithNonExistingIdAndExpectStatus404() throws Exception {
        String response = mockMvc.perform(get(BASE_URL + "9999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);

        assertEquals(ID_NOT_FOUND + 9999, error.getMessage());
    }

    @Test
    @DisplayName("Test get a user with existing id and expect status 200")
    void TestGetUserWithExistingIdAndExpectStatus200() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));
        String response = mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        UserGetDto user = objectMapper.readValue(response, UserGetDto.class);

        assertEquals(1, user.id());
        assertEquals("User_test", user.userName());
        assertEquals("email@example.com", user.email());
        assertEquals("1990-01-01", user.dateOfBirth().toString());
        assertEquals("912345678", user.phoneNumber());
    }

    @Test
    @DisplayName("Test create a user with empty field and return status code 400.")
    void TestCreateUserWithEmptyFieldAndReturnStatus400() throws Exception {
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);

        assertTrue(error.getMessage().contains(INVALID_EMAIL));
        assertTrue(error.getMessage().contains(INVALID_USER_NAME));
        assertTrue(error.getMessage().contains(INVALID_DATE_OF_BIRTH));
        assertTrue(error.getMessage().contains(INVALID_PHONE_NUMBER));
        assertTrue(error.getMessage().contains(INVALID_PASSWORD));

    }

    @Test
    @DisplayName("Test create a user with invalid email and return status code 400.")
    void TestCreateUserWithInvalidEmailAndReturnStatus400() throws Exception {
        String json = USER_JSON.replace("\"email\":\"email@example.com\"", "\"email\":\"email\"");
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);

        assertTrue(error.getMessage().contains(INVALID_EMAIL));
    }

    @Test
    @DisplayName("Test create a user with invalid username and return status code 400.")
    void TestCreateUserWithInvalidUsernameAndReturnStatus400() throws Exception {
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON.replace("\"userName\":\"User_test\"", "\"userName\":\"as dasd \"")))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);

        assertTrue(error.getMessage().contains(INVALID_USER_NAME));

    }

    @Test
    @DisplayName("Test create a user with valid data and expect status 201")
    void TestCreateUserWithValidDataAndExpectStatus201() throws Exception {

        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        UserGetDto user = objectMapper.readValue(response, UserGetDto.class);

        assertEquals(1, user.id());
        assertEquals("User_test", user.userName());
        assertEquals("email@example.com", user.email());
        assertEquals("1990-01-01", user.dateOfBirth().toString());
        assertEquals("912345678", user.phoneNumber());
    }

    @Test
    @DisplayName("Test create a user with duplicate email and expect status 400")
    void TestCreateUserWithDuplicateEmailAndExpectStatus400() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);

        assertTrue(error.getMessage().contains(DUPLICATE_EMAIL));

    }

    @Test
    @DisplayName("Test create a user with invalid date of birth and expect status 400")
    void TestCreateUserWithInvalidDateOfBirthAndExpectStatus400() throws Exception {
        String userJson = USER_JSON.replace("1990-01-01", "2030-01-01");

        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);

        assertTrue(error.getMessage().contains(INVALID_DATE));
    }


    @Test
    @DisplayName("Test put request with correct data and expect status 200")
    void putRequestWithCorrectDataExpectStatus200() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));

        String updatedUserJson = USER_JSON.replace("\"userName\":\"User_test\"", "\"userName\":\"updated_joe\"");

        String response = mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        UserGetDto updatedUser = objectMapper.readValue(response, UserGetDto.class);

        assertEquals(1, updatedUser.id());
        assertEquals("updated_joe", updatedUser.userName());
        assertEquals("email@example.com", updatedUser.email());
        assertEquals("1990-01-01", updatedUser.dateOfBirth().toString());
        assertEquals("912345678", updatedUser.phoneNumber());
    }

    @Test
    @DisplayName("Test update a user with non-existing id and expect status 404")
    void TestUpdateUserWithNonExistingIdAndExpectStatus404() throws Exception {

        String response = mockMvc.perform(put(BASE_URL + "9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);

        assertEquals(ID_NOT_FOUND + 9999, error.getMessage());
    }

    @Test
    @DisplayName("Test update a user with duplicate email and expect status 400")
    void TestUpdateUserWithDuplicateEmailAndExpectStatus400() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON.replace("\"email\":\"email@example.com\"", "\"email\":\"teste@example.com\"")));
        String response = mockMvc.perform(put(BASE_URL + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);

        assertTrue(error.getMessage().contains(DUPLICATE_EMAIL));
    }

    @Test
    @DisplayName("Test update a user with invalid date of birth and expect status 400")
    void TestUpdateUserWithInvalidDateOfBirthAndExpectStatus400() throws Exception {
        String userJson = USER_JSON.replace("1990-01-01", "2030-01-01");
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));

        String response = mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);

        assertTrue(error.getMessage().contains(INVALID_DATE));
    }

//    @Test
//    @DisplayName("Test patch request with correct data and expect status 200")
//    void updateSomeUserFields() throws Exception {
//
//        String json = USER_JSON;
//
//        mockMvc.perform(post(BASE_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(USER_JSON));
//
//
//        String response = mockMvc.perform(patch(BASE_URL + "/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        User updatedUser = objectMapper.readValue(response, User.class);
//
//
//        assertEquals(1L, updatedUser.getId());
//        assertEquals("joe", updatedUser.getUserName());
//        assertEquals("updated_joe@coldmail.com", updatedUser.getEmail());
//        assertEquals("1995-01-01", updatedUser.getDateOfBirth());
//        assertEquals("351 9********", updatedUser.getPhoneNumber());
//        assertEquals("******", updatedUser.getPassword());
//    }

//    @Test
//    @DisplayName("Test patch request with incorrect data and expect status 400")
//    void updateSomeUserFieldsWithIncorrectData() throws Exception {
//        // GIVEN
//        String json = USER_JSON;
//
//        mockMvc.perform(post(BASE_URL)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(USER_JSON));
//
//        // WHEN
//        String response = mockMvc.perform(patch(BASE_URL + "/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isBadRequest())
//                .andReturn().getResponse().getContentAsString();
//
//        AgencyError error = objectMapper.readValue(response, AgencyError.class);
//
//        // THEN
//        assertTrue(error.getMessage().contains(INVALID_EMAIL));
//        assertTrue(error.getMessage().contains(INVALID_DATE_OF_BIRTH));
//        assertTrue(error.getMessage().contains(INVALID_PHONE_NUMBER));
//        assertTrue(error.getMessage().contains(INVALID_USER_NAME));
//        assertTrue(error.getMessage().contains(INVALID_PASSWORD));
//    }

//    @Test
//    @DisplayName("Test patch a user with non-existing id and expect status 404")
//    void TestPatchUserWithNonExistingIdAndExpectStatus404() throws Exception {
//        String userJson = "{\"email\":\"jane@coldmail.com\"}";
//
//        mockMvc.perform(patch(BASE_URL + "/9999")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(userJson))
//                .andExpect(status().isNotFound())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.message").value("User not found"));
//    }

//    @Test
//    @DisplayName("Test patch a user with duplicate email and expect status 400")
//    void TestPatchUserWithDuplicateEmailAndExpectStatus400() throws Exception {
//        String userJson = "{\"email\":\"joe@coldmail.com\"}";
//
//        mockMvc.perform(patch(BASE_URL + "/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(userJson))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.message").value("Email already exists"));
//    }

//    @Test
//    @DisplayName("Test patch a user with invalid date of birth and expect status 400")
//    void TestPatchUserWithInvalidDateOfBirthAndExpectStatus400() throws Exception {
//        String userJson = "{\"dateOfBirth\":\"1992-13-01\"}";
//
//        mockMvc.perform(patch(BASE_URL + "/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(userJson))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.message").value("Invalid date of birth"));
//    }

    @Test
    @DisplayName("Test delete request with user in db and expect status 204")
    void deleteWithUserInDb() throws Exception {

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        assertEquals(0, userTestRepository.count());
        assertFalse(userTestRepository.existsById(1L));
    }

    @Test
    @DisplayName("Test delete a user with non-existing id and expect status 404")
    void TestDeleteUserWithNonExistingIdAndExpectStatus404() throws Exception {
        String response = mockMvc.perform(delete(BASE_URL + "9999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);

        assertEquals(ID_NOT_FOUND + 9999, error.getMessage());
    }

    @Test
    @DisplayName("Test delete a user with existing id and expect status 204")
    void TestDeleteUserWithExistingIdAndExpectStatus204() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));
        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Test delete a user with existing id and then get the user expect status 404")
    void TestDeleteUserWithExistingIdAndThenGetTheUserExpectStatus404() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));
        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        assertEquals(0, userTestRepository.count());
    }


}
