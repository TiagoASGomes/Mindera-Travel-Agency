package org.mindswap.academy.mindera_travel_agency.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.mindswap.academy.mindera_travel_agency.aspect.AgencyError;
import org.mindswap.academy.mindera_travel_agency.model.User;
import org.mindswap.academy.mindera_travel_agency.repository.InvoiceTestRepository;
import org.mindswap.academy.mindera_travel_agency.repository.UserTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Profile("test")
public class UserControllerTest {
    private static ObjectMapper objectMapper;
    private final String BASE_URL = "/api/v1/users";
    private final String USER_JSON = "{\"email\":\"joe@coldmail.com\",\"username\":\"joe\",\"dateOfBirth\":\"1990-01-01\"}";
    private final String INVOICE_JSON = "{\"userId\":1}";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserTestRepository userTestRepository;
    @Autowired
    private InvoiceTestRepository invoiceTestRepository;


    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() throws Exception {

        String userJson = USER_JSON;
        String invoiceJson = INVOICE_JSON;

        mockMvc.perform(post("/api/v1/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));
        for (String json : List.of(invoiceJson, invoiceJson)) {
            mockMvc.perform(post("/api/v1/invoices/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json));
        }
    }


    @AfterEach
    void init() {
        userTestRepository.deleteAll();
        userTestRepository.resetAutoIncrement();
        invoiceTestRepository.deleteAll();
        invoiceTestRepository.resetAutoIncrement();
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
    @DisplayName("Test get all users when 2 users are in the database returns a list with 2 students")
    void TestGetAllUsersWhen2UsersAreInTheDatabaseReturnsListWith2Users() throws Exception {

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));


        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Test get all user by invoice with no invoice in the database and expect a status code 404")
    void TestGetAllUsersByInvoiceWithNoInvoiceInTheDatabase() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "invoices"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(ID_NOT_FOUND + 1));

    }
    @Test
    @DisplayName("Test get all user by invoice with 2 invoices in the database and expect a status code 200")
    void TestGetAllUsersByInvoiceWith2InvoicesInTheDatabase() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "invoices"));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":1}"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":2}"))

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

            mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1"))
                            .andExpect(status().isOk())
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                            .andExpect(jsonPath("$.id").value(1))
                            .andExpect(jsonPath("$.userName").value("joe"))
                            .andExpect(jsonPath("$.email").value(
                                            "{\"email\":\"john@coldmail.com\",\"username\":\"john\",\"dateOfBirth\":\"1991-01-01\"}"))
                            .andExpect(jsonPath("$.dateOfBirth").value("1990-01-01"));

            assertEquals(1, userTestRepository.count());
            assertEquals(1, userTestRepository.findById(1L).get().getId());
            assertEquals("joe", userTestRepository.findById(1L).get().getUserName());
            assertEquals("joe@coldmail.com", userTestRepository.findById(1L).get().getEmail());
            assertEquals("1990-01-01", userTestRepository.findById(1L).get().getDateOfBirth());
    }

    @Test
@DisplayName("Test get a user with non-existing id and expect status 404")
void TestGetUserWithNonExistingIdAndExpectStatus404() throws Exception {
    mockMvc.perform(get(BASE_URL + "/9999"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("User not found"));
}

@Test
@DisplayName("Test get a user with existing id and expect status 200")
void TestGetUserWithExistingIdAndExpectStatus200() throws Exception {
    mockMvc.perform(get(BASE_URL + "/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").isNotEmpty())
            .andExpect(jsonPath("$.email").isNotEmpty())
            .andExpect(jsonPath("$.dateOfBirth").isNotEmpty());
}
    

    @Test
    @DisplayName("Test create a user when user is valid returns status code 201.")
    void TestCreateUserByIdWhenUserIsInTheDatabaseReturnsAUser() throws Exception {

        String userJson =USER_JSON;

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        User user = objectMapper.readValue(responseContent, User.class);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUserName()).isEqualTo("joe");
        assertThat(user.getEmail()).isEqualTo("joe@coldmail.com");
        assertThat(user.getDateOfBirth()).isEqualTo("1990-01-01");
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
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
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
                            .content(USER_JSON))
                            .andExpect(status().isBadRequest())
                            .andReturn().getResponse().getContentAsString();
            AgencyError error = objectMapper.readValue(response, AgencyError.class);

            assertTrue(error.getMessage().contains(INVALID_USER_NAME));

    }

    

    @Test
    @DisplayName("Test create user with invoice id that does not exist and expect status 404")
    void createUserWithInvoiceIdThatDoesNotExist() throws Exception {

            String json = USER_JSON.replace("\"invoiceId\": 1", "\"invoiceId\": 2");

            String response = mockMvc.perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                            .andExpect(status().isNotFound())
                            .andReturn().getResponse().getContentAsString();

            AgencyError error = objectMapper.readValue(response, AgencyError.class);

            assertEquals(ID_NOT_FOUND + 2, error.getMessage());
    }

    @Test
@DisplayName("Test create a user with valid data and expect status 201")
void TestCreateUserWithValidDataAndExpectStatus201() throws Exception {
    String userJson = "{\"email\":\"jane@coldmail.com\",\"username\":\"jane\",\"dateOfBirth\":\"1992-01-01\"}";

    mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.username").value("jane"))
            .andExpect(jsonPath("$.email").value("jane@coldmail.com"))
            .andExpect(jsonPath("$.dateOfBirth").value("1992-01-01"));
}

@Test
@DisplayName("Test create a user with duplicate email and expect status 400")
void TestCreateUserWithDuplicateEmailAndExpectStatus400() throws Exception {
    String userJson = "{\"email\":\"joe@coldmail.com\",\"username\":\"jane\",\"dateOfBirth\":\"1992-01-01\"}";

    mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Email already exists"));
}

@Test
@DisplayName("Test create a user with invalid date of birth and expect status 400")
void TestCreateUserWithInvalidDateOfBirthAndExpectStatus400() throws Exception {
    String userJson = "{\"email\":\"jane@coldmail.com\",\"username\":\"jane\",\"dateOfBirth\":\"1992-13-01\"}";

    mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Invalid date of birth"));
}

    

    @Test
    @DisplayName("Test put request with correct data and expect status 200")
    void putRequestWithCorrectDataExpectStatus200() throws Exception {

            mockMvc.perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(USER_JSON));

            String updatedUserJson = USER_JSON;

            mockMvc.perform(put(BASE_URL + "/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updatedUserJson))
                            .andExpect(status().isOk());

            User updatedUser = userTestRepository.findById(1L).orElse(null);

            assertNotNull(updatedUser);
            assertEquals("updated_joe", updatedUser.getUserName());
            assertEquals("updated_joe@coldmail.com", updatedUser.getEmail());
            assertEquals("1995-01-01", updatedUser.getDateOfBirth());
            assertEquals("351 9********", updatedUser.getPhoneNumber());
            assertEquals("******", updatedUser.getPassword());
    }

    @Test
@DisplayName("Test update a user with non-existing id and expect status 404")
void TestUpdateUserWithNonExistingIdAndExpectStatus404() throws Exception {
    String userJson = "{\"email\":\"jane@coldmail.com\",\"username\":\"jane\",\"dateOfBirth\":\"1992-01-01\"}";

    mockMvc.perform(put(BASE_URL + "/9999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("User not found"));
}

@Test
@DisplayName("Test update a user with duplicate email and expect status 400")
void TestUpdateUserWithDuplicateEmailAndExpectStatus400() throws Exception {
    String userJson = "{\"email\":\"joe@coldmail.com\",\"username\":\"jane\",\"dateOfBirth\":\"1992-01-01\"}";

    mockMvc.perform(put(BASE_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Email already exists"));
}

@Test
@DisplayName("Test update a user with invalid date of birth and expect status 400")
void TestUpdateUserWithInvalidDateOfBirthAndExpectStatus400() throws Exception {
    String userJson = "{\"email\":\"jane@coldmail.com\",\"username\":\"jane\",\"dateOfBirth\":\"1992-13-01\"}";

    mockMvc.perform(put(BASE_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Invalid date of birth"));
}

    @Test
    @DisplayName("Test patch request with correct data and expect status 200")
    void updateSomeUserFields() throws Exception {

        String json = USER_JSON;

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));


        String response = mockMvc.perform(patch(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        User updatedUser = objectMapper.readValue(response, User.class);


        assertEquals(1L, updatedUser.getId());
        assertEquals("joe", updatedUser.getUserName());
        assertEquals("updated_joe@coldmail.com", updatedUser.getEmail());
        assertEquals("1995-01-01", updatedUser.getDateOfBirth());
        assertEquals("351 9********", updatedUser.getPhoneNumber());
        assertEquals("******", updatedUser.getPassword());
    }

    @Test
    @DisplayName("Test patch request with incorrect data and expect status 400")
    void updateSomeUserFieldsWithIncorrectData() throws Exception {
            // GIVEN
            String json = USER_JSON;

            mockMvc.perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(USER_JSON));

            // WHEN
            String response = mockMvc.perform(patch(BASE_URL + "/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                            .andExpect(status().isBadRequest())
                            .andReturn().getResponse().getContentAsString();

            AgencyError error = objectMapper.readValue(response, AgencyError.class);

            // THEN
            assertTrue(error.getMessage().contains(INVALID_EMAIL));
            assertTrue(error.getMessage().contains(INVALID_DATE_OF_BIRTH));
            assertTrue(error.getMessage().contains(INVALID_PHONE_NUMBER));
            assertTrue(error.getMessage().contains(INVALID_USER_NAME));
            assertTrue(error.getMessage().contains(INVALID_PASSWORD));
    }

    @Test
@DisplayName("Test patch a user with non-existing id and expect status 404")
void TestPatchUserWithNonExistingIdAndExpectStatus404() throws Exception {
    String userJson = "{\"email\":\"jane@coldmail.com\"}";

    mockMvc.perform(patch(BASE_URL + "/9999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("User not found"));
}

@Test
@DisplayName("Test patch a user with duplicate email and expect status 400")
void TestPatchUserWithDuplicateEmailAndExpectStatus400() throws Exception {
    String userJson = "{\"email\":\"joe@coldmail.com\"}";

    mockMvc.perform(patch(BASE_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Email already exists"));
}

@Test
@DisplayName("Test patch a user with invalid date of birth and expect status 400")
void TestPatchUserWithInvalidDateOfBirthAndExpectStatus400() throws Exception {
    String userJson = "{\"dateOfBirth\":\"1992-13-01\"}";

    mockMvc.perform(patch(BASE_URL + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Invalid date of birth"));
}

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
            assertEquals(0, invoiceTestRepository.count());

    }

    @Test
@DisplayName("Test delete a user with non-existing id and expect status 404")
void TestDeleteUserWithNonExistingIdAndExpectStatus404() throws Exception {
    mockMvc.perform(delete(BASE_URL + "/9999"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("User not found"));
}

@Test
@DisplayName("Test delete a user with existing id and expect status 204")
void TestDeleteUserWithExistingIdAndExpectStatus204() throws Exception {
    mockMvc.perform(delete(BASE_URL + "/1"))
            .andExpect(status().isNoContent());
}

@Test
@DisplayName("Test delete a user with existing id and then get the user expect status 404")
void TestDeleteUserWithExistingIdAndThenGetTheUserExpectStatus404() throws Exception {
    mockMvc.perform(delete(BASE_URL + "/1"))
            .andExpect(status().isNoContent());

    mockMvc.perform(get(BASE_URL + "/1"))
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("User not found"));
}
    

}
