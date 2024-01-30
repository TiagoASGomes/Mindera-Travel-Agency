package org.mindswap.academy.mindera_travel_agency.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindswap.academy.mindera_travel_agency.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Profile("test")
class FlightTicketControllerTest {
    private final String BASE_URL = "/api/v1/flight_tickets";
    private final String FLIGHT_TICKET_JSON = "{\"carryOnLuggage\": true,\"email\": \"teste@example.com\",\"fName\": \"teste um\",\"fareClass\": \"first\",\"invoiceId\": 1,\"maxLuggageWeight\": \"22\",\"phone\": \"910410860\",\"price\": 100,\"seatNumber\": \"2B\",\"ticketNumber\": 1}";
    private final String FLIGHT_TICKET_JSON_2 = "{\"carryOnLuggage\": false,\"email\": \"teste2@example.com\",\"fName\": \"teste dois\",\"fareClass\": \"business\",\"invoiceId\": 1,\"maxLuggageWeight\": \"15\",\"phone\": \"912345678\",\"price\": 75,\"seatNumber\": \"4D\",\"ticketNumber\": 2}";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserTestRepository userTestRepository;
    @Autowired
    private InvoiceTestRepository invoiceTestRepository;
    @Autowired
    private FareClassTestRepository fareClassTestRepository;
    @Autowired
    private PaymentStatusTestRepository paymentStatusTestRepository;
    @Autowired
    private FlightTicketTestRepository flightTicketTestRepository;

    @BeforeEach
    void setUp() throws Exception {
        String userJson = "{\"email\": \"teste@example.com\",\"password\": \"C@$9gmL?\",\"userName\": \"userTeste\",\"dateOfBirth\": \"2000-01-01\",\"phoneNumber\": \"937313732\"}";
        String invoiceJson = "{\"userId\":1}";
        List<String> fareClassJson = List.of("{\"className\": \"first\"}", "{\"className\": \"business\"}", "{\"className\": \"economy\"}");
        List<String> paymentStatusJson = List.of("{\"statusName\": \"PENDING\"}", "{\"statusName\": \"PAID\"}", "{\"statusName\": \"NOT_REQUESTED\"}");
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));
        for (String json : fareClassJson) {
            mockMvc.perform(post("/api/v1/fare_classes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json));
        }
        for (String json : paymentStatusJson) {
            mockMvc.perform(post("/api/v1/payment_status")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json));
        }
        mockMvc.perform(post("/api/v1/invoices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invoiceJson));
    }

    @AfterEach
    void tearDown() {
        flightTicketTestRepository.deleteAll();
        flightTicketTestRepository.resetAutoIncrement();
        fareClassTestRepository.deleteAll();
        fareClassTestRepository.resetAutoIncrement();
        invoiceTestRepository.deleteAll();
        invoiceTestRepository.resetAutoIncrement();
        paymentStatusTestRepository.deleteAll();
        paymentStatusTestRepository.resetAutoIncrement();
        userTestRepository.deleteAll();
        userTestRepository.resetAutoIncrement();
    }

    @Test
    @DisplayName("Get all tickets with 2 tickets in db and expect status 200 and a list of 2 tickets")
    void getAll() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON_2));

        // WHEN
        // THEN
        mockMvc.perform(get(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));


    }

    @Test
    void getById() {
    }

    @Test
    void getAllByInvoice() {
    }

    @Test
    void create() {
    }

    @Test
    void updateTotal() {
    }

    @Test
    void updatePartial() {
    }

    @Test
    void updateTicketNumber() {
    }

    @Test
    void delete() {
    }
}