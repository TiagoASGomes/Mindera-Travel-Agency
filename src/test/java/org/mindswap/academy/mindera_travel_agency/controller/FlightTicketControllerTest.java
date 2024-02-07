package org.mindswap.academy.mindera_travel_agency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.mindswap.academy.mindera_travel_agency.aspect.AgencyError;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.repository.FlightTicketTestRepository;
import org.mindswap.academy.mindera_travel_agency.repository.InvoiceTestRepository;
import org.mindswap.academy.mindera_travel_agency.repository.PaymentStatusTestRepository;
import org.mindswap.academy.mindera_travel_agency.repository.UserTestRepository;
import org.mindswap.academy.mindera_travel_agency.util.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(RedisConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ImportAutoConfiguration(classes = {
        CacheAutoConfiguration.class,
        RedisAutoConfiguration.class})
@EnableCaching
class FlightTicketControllerTest {
    private static ObjectMapper objectMapper;
    private final String BASE_URL = "/api/v1/flights/";
    private final String FLIGHT_TICKET_JSON = "{\"fName\": \"teste um\",\"email\": \"teste@example.com\",\"phone\": \"910410860\",\"fareClass\": \"FIRST\",\"price\": 100,\"maxLuggageWeight\": 22,\"carryOnLuggage\": true,\"duration\": 2.5,\"invoiceId\": 1,\"flightId\": 1,\"priceId\": 1}";
    private final String FLIGHT_TICKET_JSON_2 = "{\"fName\": \"teste dois\",\"email\": \"teste2@example.com\",\"phone\": \"912345678\",\"fareClass\": \"ECONOMY\",\"price\": 75,\"maxLuggageWeight\": 15,\"carryOnLuggage\": false,\"duration\": 3,\"invoiceId\": 1,\"flightId\": 1,\"priceId\": 1}";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserTestRepository userTestRepository;
    @Autowired
    private InvoiceTestRepository invoiceTestRepository;
    @Autowired
    private PaymentStatusTestRepository paymentStatusTestRepository;
    @Autowired
    private FlightTicketTestRepository flightTicketTestRepository;

    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() throws Exception {
        String userJson = "{\"email\": \"teste@example.com\",\"password\": \"zxlmn!!23K?\",\"userName\": \"userTeste\",\"dateOfBirth\": \"2000-01-01\",\"phoneNumber\": \"937313732\",\"vat\": \"123456782\"}";
        String invoiceJson = "{\"userId\":1}";
        List<String> paymentStatusJson = List.of("{\"statusName\": \"PENDING\"}", "{\"statusName\": \"PAID\"}", "{\"statusName\": \"NOT_REQUESTED\"}");
        mockMvc.perform(post("/api/v1/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));
        for (String json : paymentStatusJson) {
            mockMvc.perform(post("/api/v1/payment_status/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json));
        }
        mockMvc.perform(post("/api/v1/invoices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invoiceJson));
    }

    @AfterEach
    void tearDown() {
        flightTicketTestRepository.deleteAll();
        flightTicketTestRepository.resetAutoIncrement();
        invoiceTestRepository.deleteAll();
        invoiceTestRepository.resetAutoIncrement();
        paymentStatusTestRepository.deleteAll();
        paymentStatusTestRepository.resetAutoIncrement();
        userTestRepository.deleteAll();
        userTestRepository.resetAutoIncrement();
    }

    @Test
    @DisplayName("Get all tickets with 2 tickets in db and expect status 200 and a list of 2 tickets")
    void getAllWithTicketsPresent() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON_2));

        // WHEN
        // THEN
        mockMvc.perform(get(BASE_URL + "?page=0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    @DisplayName("Get all with 30 tickets in db and expect status 200 and a list of 20 tickets")
    void getAllWith30TicketsPresent() throws Exception {
        // GIVEN
        for (int i = 0; i < 30; i++) {
            mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(FLIGHT_TICKET_JSON));
        }
        // WHEN
        // THEN
        mockMvc.perform(get(BASE_URL + "?page=0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(20)));
        mockMvc.perform(get(BASE_URL + "?page=1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(10)));
    }

    @Test
    @DisplayName("Get all tickets with no tickets in db and expect status 200 and an empty list")
    void getAllWithNoTicketsPresent() throws Exception {
        // GIVEN

        // WHEN
        // THEN
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    @DisplayName("Get all tickets by invoice with 2 tickets in db and expect status 200 and a list of 2 tickets")
    void getAllByInvoice() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON_2));
        // WHEN
        // THEN
        mockMvc.perform(get(BASE_URL + "invoice/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Get all tickets by invoice with no tickets in db and expect status 200 and an empty list")
    void getAllByInvoiceWithNoTicketsPresent() throws Exception {
        // GIVEN
        // WHEN
        // THEN
        mockMvc.perform(get(BASE_URL + "invoice/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Get all tickets by invoice with no invoice in db and expect status 404 and error message")
    void getAllByInvoiceWithIncorrectInvoiceId() throws Exception {
        // GIVEN
        // WHEN
        mockMvc.perform(get(BASE_URL + "invoice/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(INVOICE_ID_NOT_FOUND + 2));
    }

    @Test
    @DisplayName("Get ticket by id with ticket in db and expect status 200 and the ticket")
    void getByIdWithTicketPresent() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));

        // WHEN
        String response = mockMvc.perform(get(BASE_URL + "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        TicketGetDto ticket = objectMapper.readValue(response, TicketGetDto.class);

        // THEN
        assertEquals(1, ticket.id());
        assertEquals("teste um", ticket.fName());
        assertEquals("teste@example.com", ticket.email());
        assertEquals("910410860", ticket.phone());
        assertNull(ticket.ticketNumber());
        assertNull(ticket.seatNumber());
        assertEquals(100, ticket.price());
        assertEquals("FIRST", ticket.fareClass());
        assertEquals(22, ticket.maxLuggageWeight());
        assertTrue(ticket.carryOnLuggage());
        assertEquals(2.5, ticket.duration());
    }

    @Test
    @DisplayName("Get ticket by id with no ticket in db and expect status 404")
    void getByIdWithNoTicketPresent() throws Exception {
        // GIVEN
        // WHEN
        String response = mockMvc.perform(get(BASE_URL + "1"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(FLIGHT_TICKET_ID_NOT_FOUND + 1, error.getMessage());
    }

    @Test
    @DisplayName("Create ticket with correct data and expect status 201, the ticket and the invoice updated price")
    void createTicket() throws Exception {
        // GIVEN
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(FLIGHT_TICKET_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        String response2 = mockMvc.perform(get("/api/v1/invoices/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        TicketGetDto ticket = objectMapper.readValue(response, TicketGetDto.class);
        InvoiceGetDto invoice = objectMapper.readValue(response2, InvoiceGetDto.class);
        // THEN
        assertEquals(1, ticket.id());
        assertEquals("teste um", ticket.fName());
        assertEquals("teste@example.com", ticket.email());
        assertEquals("910410860", ticket.phone());
        assertNull(ticket.ticketNumber());
        assertNull(ticket.seatNumber());
        assertEquals(100, ticket.price());
        assertEquals("FIRST", ticket.fareClass());
        assertEquals(22, ticket.maxLuggageWeight());
        assertTrue(ticket.carryOnLuggage());
        assertEquals(2.5, ticket.duration());
        assertEquals(100, invoice.totalPrice());
    }

    @Test
    @DisplayName("Create ticket over ticket limit and expect status 400 and error message")
    void createTicketOverTicketLimit() throws Exception {
        // GIVEN
        for (int i = 0; i < 50; i++) {
            mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(FLIGHT_TICKET_JSON));
        }
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(FLIGHT_TICKET_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(INVOICE_FLIGHT_LIMIT_REACHED, error.getMessage());
    }

    @Test
    @DisplayName("Create 2 tickets and expect invoice price updated")
    void createTwoTickets() throws Exception {
        // GIVEN
        // WHEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON_2));
        String response = mockMvc.perform(get("/api/v1/invoices/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        InvoiceGetDto invoice = objectMapper.readValue(response, InvoiceGetDto.class);
        // THEN
        assertEquals(175, invoice.totalPrice());
    }

    @Test
    @DisplayName("Create ticket with incorrect data and expect status 400")
    void createTicketWithIncorrectData() throws Exception {
        // GIVEN
        String json = "{\"fName\": \"t\",\"email\": \"testeexample.com\",\"phone\": \"912345da678\",\"fareClass\": \"a\",\"price\": -1,\"maxLuggageWeight\": 101,\"duration\": 25,\"invoiceId\": 0,\"flightId\": 0,\"priceId\": 0}";
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertTrue(error.getMessage().contains(INVALID_NAME));
        assertTrue(error.getMessage().contains(INVALID_EMAIL));
        assertTrue(error.getMessage().contains(INVALID_PHONE_NUMBER));
        assertTrue(error.getMessage().contains(INVALID_FARE_CLASS));
        assertTrue(error.getMessage().contains(INVALID_PRICE));
        assertTrue(error.getMessage().contains(INVALID_LUGGAGE_WEIGHT));
        assertTrue(error.getMessage().contains(INVALID_DURATION));
        assertTrue(error.getMessage().contains(INVALID_INVOICE_ID));
        assertTrue(error.getMessage().contains(INVALID_FLIGHT_ID));
        assertTrue(error.getMessage().contains(INVALID_PRICE_ID));
        assertTrue(error.getMessage().contains(INVALID_CARRY_ON_LUGGAGE));
    }

    @Test
    @DisplayName("Test create ticket with empty fields and expect status 400")
    void createTicketWithEmptyBody() throws Exception {
        // GIVEN
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertTrue(error.getMessage().contains(INVALID_NAME));
        assertTrue(error.getMessage().contains(INVALID_EMAIL));
        assertTrue(error.getMessage().contains(INVALID_PHONE_NUMBER));
        assertTrue(error.getMessage().contains(INVALID_FARE_CLASS));
        assertTrue(error.getMessage().contains(INVALID_PRICE));
        assertTrue(error.getMessage().contains(INVALID_LUGGAGE_WEIGHT));
        assertTrue(error.getMessage().contains(INVALID_DURATION));
        assertTrue(error.getMessage().contains(INVALID_INVOICE_ID));
        assertTrue(error.getMessage().contains(INVALID_FLIGHT_ID));
        assertTrue(error.getMessage().contains(INVALID_PRICE_ID));
        assertTrue(error.getMessage().contains(INVALID_CARRY_ON_LUGGAGE));
    }

    @Test
    @DisplayName("Test create ticket with invoice id that does not exist and expect status 404")
    void createTicketWithInvoiceIdThatDoesNotExist() throws Exception {
        // GIVEN
        String json = FLIGHT_TICKET_JSON.replace("\"invoiceId\": 1", "\"invoiceId\": 2");
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(INVOICE_ID_NOT_FOUND + 2, error.getMessage());
    }


    @Test
    @DisplayName("Test put request with correct data and expect status 200 and the ticket")
    void putRequest() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));
        // WHEN
        String response = mockMvc.perform(put(BASE_URL + "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(FLIGHT_TICKET_JSON_2))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        TicketGetDto ticket = objectMapper.readValue(response, TicketGetDto.class);
        // THEN
        assertEquals(1, ticket.id());
        assertEquals("teste dois", ticket.fName());
        assertEquals("teste2@example.com", ticket.email());
        assertEquals("912345678", ticket.phone());
        assertNull(ticket.ticketNumber());
        assertNull(ticket.seatNumber());
        assertEquals(75, ticket.price());
        assertEquals("ECONOMY", ticket.fareClass());
        assertEquals(15, ticket.maxLuggageWeight());
        assertFalse(ticket.carryOnLuggage());
        assertEquals(3, ticket.duration());
    }

    @Test
    @DisplayName("Test put request with no ticket in db and expect status 404")
    void putRequestWithNoTicketInDb() throws Exception {
        // GIVEN
        // WHEN
        String response = mockMvc.perform(put(BASE_URL + "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(FLIGHT_TICKET_JSON_2))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(FLIGHT_TICKET_ID_NOT_FOUND + 1, error.getMessage());
    }

    @Test
    @DisplayName("Test put request with no invoice in db and expect status 404")
    void putRequestWithNoInvoiceInDb() throws Exception {
        // GIVEN
        String json = FLIGHT_TICKET_JSON_2.replace("\"invoiceId\": 1", "\"invoiceId\": 2");
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));
        // WHEN
        String response = mockMvc.perform(put(BASE_URL + "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(INVOICE_ID_NOT_FOUND + 2, error.getMessage());
    }

    @Test
    @DisplayName("Test put request with invoice paid and expect status 400 and error message")
    void putRequestWithInvoicePaid() throws Exception {
        // GIVEN
        String json = FLIGHT_TICKET_JSON.replace("\"fName\": \"teste um\"", "\"fName\": \"teste dois\"");
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));
        mockMvc.perform(patch("/api/v1/invoices/1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"paymentStatus\": \"PAID\"}"));
        // WHEN
        String response = mockMvc.perform(put(BASE_URL + "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(CANNOT_ALTER_PLANE_TICKET, error.getMessage());
    }

    @Test
    @DisplayName("Test put request with invoice pending and expect status 400 and error message")
    void putRequestWithInvoicePending() throws Exception {
        // GIVEN
        String json = FLIGHT_TICKET_JSON.replace("\"fName\": \"teste um\"", "\"fName\": \"teste dois\"");
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));
        mockMvc.perform(patch("/api/v1/invoices/1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"paymentStatus\": \"PENDING\"}"));
        // WHEN
        String response = mockMvc.perform(put(BASE_URL + "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(CANNOT_ALTER_PLANE_TICKET, error.getMessage());
    }

    @Test
    @DisplayName("Test patch personalInfo request with correct data and expect status 200 and the ticket")
    void updatePersonalInfo() throws Exception {
        // GIVEN
        String json = "{\"fName\": \"teste updated\",\"email\": \"updated@example.com\",\"phone\": \"912345678\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/personal_info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        TicketGetDto ticket = objectMapper.readValue(response, TicketGetDto.class);
        // THEN
        assertEquals(1, ticket.id());
        assertEquals("teste updated", ticket.fName());
        assertEquals("updated@example.com", ticket.email());
        assertEquals("912345678", ticket.phone());
        assertNull(ticket.ticketNumber());
        assertNull(ticket.seatNumber());
        assertEquals(100, ticket.price());
        assertEquals("FIRST", ticket.fareClass());
        assertEquals(22, ticket.maxLuggageWeight());
        assertTrue(ticket.carryOnLuggage());
        assertEquals(2.5, ticket.duration());
    }

    @Test
    @DisplayName("Test patch personalInfo request with incorrect data and expect status 400")
    void updatePersonalInfoWithIncorrectData() throws Exception {
        // GIVEN
        String json = "{\"fName\": \"teste upd2ated\",\"email\": \"updatedexample.com\",\"phone\": \"912345da678\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/personal_info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertTrue(error.getMessage().contains(INVALID_NAME));
        assertTrue(error.getMessage().contains(INVALID_EMAIL));
        assertTrue(error.getMessage().contains(INVALID_PHONE_NUMBER));
    }

    @Test
    @DisplayName("Test patch personalInfo request with empty body and expect status 400")
    void updatePersonalInfoWithEmptyBody() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/personal_info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertTrue(error.getMessage().contains(INVALID_NAME));
        assertTrue(error.getMessage().contains(INVALID_EMAIL));
        assertTrue(error.getMessage().contains(INVALID_PHONE_NUMBER));
    }

    @Test
    @DisplayName("Test patch personalInfo request with no ticket in db and expect status 404")
    void updatePersonalInfoWithNoTicketInDb() throws Exception {
        // GIVEN
        String json = "{\"fName\": \"teste updated\",\"email\": \"updated@example.com\",\"phone\": \"912345678\"}";
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/personal_info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(FLIGHT_TICKET_ID_NOT_FOUND + 1, error.getMessage());
    }

    @Test
    @DisplayName("Test patch personalInfo request with invoice paid and expect status 400 and error message")
    void updatePersonalInfoWithInvoicePaid() throws Exception {
        // GIVEN
        String json = FLIGHT_TICKET_JSON.replace("\"fName\": \"teste um\"", "\"fName\": \"teste dois\"");
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));
        mockMvc.perform(patch("/api/v1/invoices/1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"paymentStatus\": \"PAID\"}"));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/personal_info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(CANNOT_ALTER_PLANE_TICKET, error.getMessage());
    }


    @Test
    @DisplayName("Test patch request to update ticket info and expect status 200 and the ticket, and invoice price updated")
    void updateTicketInfo() throws Exception {
        // GIVEN
        String json = "{\"price\": 15,\"fareClass\": \"ECONOMY\",\"maxLuggageWeight\": 10,\"carryOnLuggage\": false}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/ticket_info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String response2 = mockMvc.perform(get("/api/v1/invoices/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        TicketGetDto ticket = objectMapper.readValue(response, TicketGetDto.class);
        InvoiceGetDto invoice = objectMapper.readValue(response2, InvoiceGetDto.class);
        // THEN
        assertEquals(1, ticket.id());
        assertEquals("teste um", ticket.fName());
        assertEquals("teste@example.com", ticket.email());
        assertEquals("910410860", ticket.phone());
        assertNull(ticket.ticketNumber());
        assertNull(ticket.seatNumber());
        assertEquals(15, ticket.price());
        assertEquals("ECONOMY", ticket.fareClass());
        assertEquals(10, ticket.maxLuggageWeight());
        assertFalse(ticket.carryOnLuggage());
        assertEquals(2.5, ticket.duration());
        assertEquals(15, invoice.totalPrice());
    }

    @Test
    @DisplayName("Test patch request to update 1 ticket info with 2 tickets in db and expect invoice price updated")
    void updateTicketInfoWithTwoTickets() throws Exception {
        // GIVEN
        String json = "{\"price\": 15,\"fareClass\": \"ECONOMY\",\"maxLuggageWeight\": 10,\"carryOnLuggage\": false}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON_2));
        // WHEN
        mockMvc.perform(patch(BASE_URL + "1/ticket_info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        String response = mockMvc.perform(get("/api/v1/invoices/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        InvoiceGetDto invoice = objectMapper.readValue(response, InvoiceGetDto.class);
        // THEN
        assertEquals(90, invoice.totalPrice());
    }

    @Test
    @DisplayName("Test patch request to update ticket info with no ticket in db and expect status 404")
    void updateTicketInfoWithNoTicketInDb() throws Exception {
        // GIVEN
        String json = "{\"price\": 15,\"fareClass\": \"ECONOMY\",\"maxLuggageWeight\": 10,\"carryOnLuggage\": false}";
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/ticket_info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(FLIGHT_TICKET_ID_NOT_FOUND + 1, error.getMessage());
    }

    @Test
    @DisplayName("Test patch request to update ticket info with invoice paid and expect status 400 and error message")
    void updateTicketInfoWithInvoicePaid() throws Exception {
        // GIVEN
        String json = "{\"price\": 15,\"fareClass\": \"ECONOMY\",\"maxLuggageWeight\": 10,\"carryOnLuggage\": false}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));
        mockMvc.perform(patch("/api/v1/invoices/1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"paymentStatus\": \"PAID\"}"));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/ticket_info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(CANNOT_ALTER_PLANE_TICKET, error.getMessage());
    }

    @Test
    @DisplayName("Test delete request with ticket in db and expect status 204")
    void deleteWithTicketInDb() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));
        // WHEN
        mockMvc.perform(delete(BASE_URL + "1"))
                .andExpect(status().isOk());
        // THEN
        assertEquals(0, flightTicketTestRepository.count());
    }

    @Test
    @DisplayName("Test delete request with no ticket in db and expect status 404")
    void deleteWithNoTicketInDb() throws Exception {
        mockMvc.perform(delete(BASE_URL + "1"))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("Test delete request with invoice paid and expect status 400 and error message")
    void deleteWithInvoicePaid() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));
        mockMvc.perform(patch("/api/v1/invoices/1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"paymentStatus\": \"PAID\"}"));
        // WHEN
        String response = mockMvc.perform(delete(BASE_URL + "1"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(CANNOT_ALTER_PLANE_TICKET, error.getMessage());
    }
}