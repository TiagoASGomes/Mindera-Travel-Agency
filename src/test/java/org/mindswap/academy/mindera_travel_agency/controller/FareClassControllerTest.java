package org.mindswap.academy.mindera_travel_agency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindswap.academy.mindera_travel_agency.aspect.AgencyError;
import org.mindswap.academy.mindera_travel_agency.dto.fare_class.FareClassGetDto;
import org.mindswap.academy.mindera_travel_agency.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FareClassControllerTest {

    private final String BASE_URL = "/api/v1/fare_classes/";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FareClassTestRepository fareClassTestRepository;
    @Autowired
    private FlightTicketTestRepository flightTicketTestRepository;
    @Autowired
    private PaymentStatusTestRepository paymentStatusTestRepository;
    @Autowired
    private InvoiceTestRepository invoiceTestRepository;
    @Autowired
    private UserTestRepository userTestRepository;

    @AfterEach
    void tearDown() {
        fareClassTestRepository.deleteAll();
        fareClassTestRepository.resetAutoIncrement();
    }

    @Test
    @DisplayName("Test get all with 2 fare classes and expect status 200 and 2 fare classes")
    void getAll() throws Exception {
        // GIVEN
        String json1 = "{\"className\": \"first\"}";
        String json2 = "{\"className\": \"economy\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json1));
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2));
        // WHEN
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Test get all with no fare classes and expect status 200 and no fare classes")
    void getAllWithNoFareClasses() throws Exception {
        // WHEN
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Test get by name and expect status 200 and fare class")
    void getByName() throws Exception {
        // GIVEN
        String json = "{\"className\": \"first\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // WHEN
        String respone = mockMvc.perform(get(BASE_URL + "/name/first"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        FareClassGetDto fareClassGetDto = objectMapper.readValue(respone, FareClassGetDto.class);
        // THEN
        assertEquals(1L, fareClassGetDto.id());
        assertEquals("first", fareClassGetDto.className());
    }

    @Test
    @DisplayName("Test get by name with no fare class and expect status 404")
    void getByNameWithNoFareClass() throws Exception {
        // WHEN
        String response = mockMvc.perform(get(BASE_URL + "/name/first"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(NAME_NOT_FOUND + "first", agencyError.getMessage());
    }

    @Test
    @DisplayName("Test create and expect status 201 and fare class")
    void create() throws Exception {
        // GIVEN
        String json = "{\"className\": \"first\"}";
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        FareClassGetDto fareClassGetDto = objectMapper.readValue(response, FareClassGetDto.class);
        // THEN
        assertEquals(1L, fareClassGetDto.id());
        assertEquals("first", fareClassGetDto.className());
    }

    @Test
    @DisplayName("Test create with duplicate name and expect status 400")
    void createWithDuplicateName() throws Exception {
        // GIVEN
        String json1 = "{\"className\": \"first\"}";
        String json2 = "{\"className\": \"first\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json1));
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(NAME_TAKEN, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test create with invalid name and expect status 400")
    void createWithInvalidName() throws Exception {
        // GIVEN
        String json = "{\"className\": \"first1\"}";
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertTrue(agencyError.getMessage().contains(INVALID_FARE_CLASS));
    }

    @Test
    @DisplayName("Test create list and expect status 201 and fare classes")
    void createList() throws Exception {
        // GIVEN
        String json = "[{\"className\": \"first\"},{\"className\": \"economy\"}]";
        // WHEN
        String response = mockMvc.perform(post(BASE_URL + "list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        FareClassGetDto[] fareClassGetDtos = objectMapper.readValue(response, FareClassGetDto[].class);
        // THEN
        assertEquals(1L, fareClassGetDtos[0].id());
        assertEquals("first", fareClassGetDtos[0].className());
        assertEquals(2L, fareClassGetDtos[1].id());
        assertEquals("economy", fareClassGetDtos[1].className());
        assertEquals(2, fareClassTestRepository.count());
    }

    @Test
    @DisplayName("Test create list with duplicate name and expect status 400")
    void createListWithDuplicateName() throws Exception {
        // GIVEN
        String json1 = "[{\"className\": \"first\"},{\"className\": \"economy\"}]";
        String json2 = "[{\"className\": \"first\"},{\"className\": \"economy\"},{\"className\": \"first\"}]";
        mockMvc.perform(post(BASE_URL + "list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json1));
        // WHEN
        String response = mockMvc.perform(post(BASE_URL + "list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(NAME_TAKEN, agencyError.getMessage());
        assertEquals(2, fareClassTestRepository.count());
    }

    @Test
    @DisplayName("Test create list with second name invalid and expect status 400")
    void createListWithSecondNameInvalid() throws Exception {
        // GIVEN
        String json = "[{\"className\": \"first\"},{\"className\": \"first\"}]";
        // WHEN
        String response = mockMvc.perform(post(BASE_URL + "list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(NAME_TAKEN, agencyError.getMessage());
        assertEquals(1, fareClassTestRepository.count());
    }

    @Test
    @DisplayName("Test update and expect status 200 and fare class")
    void update() throws Exception {
        // GIVEN
        String json = "{\"className\": \"first\"}";
        String jsonUpdate = "{\"className\": \"economy\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // WHEN
        String response = mockMvc.perform(put(BASE_URL + "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUpdate))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        FareClassGetDto fareClassGetDto = objectMapper.readValue(response, FareClassGetDto.class);
        // THEN
        assertEquals(1L, fareClassGetDto.id());
        assertEquals("economy", fareClassGetDto.className());
    }

    @Test
    @DisplayName("Test update with duplicate name and expect status 400")
    void updateWithDuplicateName() throws Exception {
        // GIVEN
        String json1 = "{\"className\": \"first\"}";
        String json2 = "{\"className\": \"economy\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json1));
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2));
        // WHEN
        String response = mockMvc.perform(put(BASE_URL + "2")
                        .contentType("application/json")
                        .content(json1))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(NAME_TAKEN, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test update with invalid id and expect status 404")
    void updateWithInvalidId() throws Exception {
        // GIVEN
        String json = "{\"className\": \"first\"}";
        // WHEN
        String response = mockMvc.perform(put(BASE_URL + "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(ID_NOT_FOUND + "1", agencyError.getMessage());
    }

    @Test
    @DisplayName("Test delete and expect status 204")
    void deleteFareClass() throws Exception {
        // GIVEN
        String json = "{\"className\": \"first\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // WHEN
        mockMvc.perform(delete(BASE_URL + "1"))
                .andExpect(status().isNoContent());
        // THEN
        assertEquals(0, fareClassTestRepository.count());
    }

    @Test
    @DisplayName("Test delete with invalid id and expect status 404")
    void deleteWithInvalidId() throws Exception {
        // WHEN
        String response = mockMvc.perform(delete(BASE_URL + "1"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(ID_NOT_FOUND + "1", agencyError.getMessage());
    }

    @Test
    @DisplayName("Test delete with fare class in use and expect status 400")
    void deleteWithFareClassInUse() throws Exception {
        // GIVEN
        String json = "{\"className\": \"first\"}";
        String userJson = "{\"email\": \"teste@example.com\",\"password\": \"C@$9gmL?\",\"userName\": \"userTeste\",\"dateOfBirth\": \"2000-01-01\",\"phoneNumber\": \"937313732\"}";
        String invoiceJson = "{\"userId\":1}";
        String paymentStatusJson = "{\"statusName\":\"NOT_REQUESTED\"}";
        String flightJson = "{\"carryOnLuggage\": true,\"email\": \"teste@example.com\",\"fName\": \"teste um\",\"fareClass\": \"first\",\"invoiceId\": 1,\"maxLuggageWeight\": \"22\",\"phone\": \"910410860\",\"price\": 100,\"seatNumber\": \"2B\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        mockMvc.perform(post("/api/v1/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));
        mockMvc.perform(post("/api/v1/payment_status/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(paymentStatusJson));
        mockMvc.perform(post("/api/v1/invoices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invoiceJson));
        mockMvc.perform(post("/api/v1/flights/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(flightJson));
        // WHEN
        String response = mockMvc.perform(delete(BASE_URL + "1"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(FARE_CLASS_IN_USE, agencyError.getMessage());
        assertEquals(1, fareClassTestRepository.count());
        flightTicketTestRepository.deleteAll();
        flightTicketTestRepository.resetAutoIncrement();
        invoiceTestRepository.deleteAll();
        invoiceTestRepository.resetAutoIncrement();
        paymentStatusTestRepository.deleteAll();
        paymentStatusTestRepository.resetAutoIncrement();
        userTestRepository.deleteAll();
        userTestRepository.resetAutoIncrement();

    }
}