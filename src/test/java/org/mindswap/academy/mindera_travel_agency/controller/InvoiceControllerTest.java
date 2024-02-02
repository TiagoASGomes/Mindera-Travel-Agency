package org.mindswap.academy.mindera_travel_agency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.mindswap.academy.mindera_travel_agency.aspect.AgencyError;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class InvoiceControllerTest {
    private static ObjectMapper objectMapper;
    private final String BASE_URL = "/api/v1/invoices/";
    private final String EXAMPLE1 = "{\"userId\": 1}";
    private final String HOTEL_EXAMPLE = "{\"invoiceId\": 1,\"checkInDate\": \"2025-01-01T12:00:00\",\"checkOutDate\": \"2025-01-05T12:00:00\",\"hotelInfo\": {\"externalId\": 1,\"name\": \"Hotel Teste\",\"address\": \"teste adress\",\"phoneNumber\": \"120312312\",\"rooms\": [{\"externalId\":1,\"pricePerNight\":10,\"roomType\":\"TYPE\",\"roomNumber\":101,\"numberOfBeds\":3},{\"externalId\":2,\"pricePerNight\":15,\"roomType\":\"TYPE2\",\"roomNumber\":102,\"numberOfBeds\":2}]}}";
    private final String FLIGHT_EXAMPLE = "{\"carryOnLuggage\": true,\"email\": \"teste@example.com\",\"fName\": \"teste um\",\"fareClass\": \"first\",\"invoiceId\": 1,\"maxLuggageWeight\": \"22\",\"phone\": \"910410860\",\"price\": 100,\"seatNumber\": \"2B\"}";
    private final String UPDATE_EXAMPLE1 = "{\"paymentStatus\": \"PENDING\", \"paymentDate\": \"2025-01-01T00:00:00\"}";
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
    @Autowired
    private FareClassTestRepository fareClassTestRepository;
    @Autowired
    private HotelReservationTestRepository hotelReservationTestRepository;

    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() throws Exception {
        String userJson = "{\"email\": \"teste@example.com\",\"password\": \"C@$9gmL?\",\"userName\": \"userTeste\",\"dateOfBirth\": \"2000-01-01\",\"phoneNumber\": \"937313732\"}";
        List<String> paymentStatusJson = List.of("{\"statusName\": \"PENDING\"}", "{\"statusName\": \"PAID\"}", "{\"statusName\": \"NOT_REQUESTED\"}");
        String fareClassJson = "{\"className\": \"first\"}";
        mockMvc.perform(post("/api/v1/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));
        for (String json : paymentStatusJson) {
            mockMvc.perform(post("/api/v1/payment_status/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json));
        }
        mockMvc.perform(post("/api/v1/fare_classes/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(fareClassJson));
    }

    @AfterEach
    void tearDown() {
        hotelReservationTestRepository.deleteAll();
        hotelReservationTestRepository.resetAutoIncrement();
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
    @DisplayName("Test get all and expect status 200 and a list of invoices")
    void getAll() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        // WHEN
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Test get all with empty db and expect status 200 and an empty list")
    void getAllEmpty() throws Exception {
        // WHEN
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Test get by id and expect status 200 and an invoice")
    void getById() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        // WHEN
        String response = mockMvc.perform(get(BASE_URL + "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        InvoiceGetDto invoice = objectMapper.readValue(response, InvoiceGetDto.class);
        // THEN
        assertEquals(1L, invoice.id());
        assertNull(invoice.hotelReservation());
        assertEquals(0, invoice.flightTickets().size());
        assertEquals(0, invoice.totalPrice());
        assertNull(invoice.paymentDate());
        assertEquals("NOT_REQUESTED", invoice.paymentStatus());
    }

    @Test
    @DisplayName("Test get by id with invalid id and expect status 404")
    void getByIdInvalidId() throws Exception {
        // WHEN
        String response = mockMvc.perform(get(BASE_URL + "1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(ID_NOT_FOUND + 1, error.getMessage());
    }

    @Test
    @DisplayName("Test get with reservation and flight tickets and expect status 200 update prices")
    void getByIdWithReservationAndFlightTickets() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        mockMvc.perform(post("/api/v1/reservations/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(HOTEL_EXAMPLE));
        mockMvc.perform(post("/api/v1/flights/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_EXAMPLE));
        // WHEN
        String response = mockMvc.perform(get(BASE_URL + "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        InvoiceGetDto invoice = objectMapper.readValue(response, InvoiceGetDto.class);
        // THEN
        assertEquals(1L, invoice.id());
        assertEquals(1, invoice.flightTickets().size());
        assertEquals(200, invoice.totalPrice());
        assertEquals("NOT_REQUESTED", invoice.paymentStatus());
        assertNull(invoice.paymentDate());
        assertEquals(1L, invoice.hotelReservation().id());
    }

    @Test
    @DisplayName("Test create and expect status 201 and an invoice")
    void create() throws Exception {
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EXAMPLE1))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        InvoiceGetDto invoice = objectMapper.readValue(response, InvoiceGetDto.class);
        // THEN
        assertEquals(1L, invoice.id());
        assertEquals(0, invoice.flightTickets().size());
        assertEquals(0, invoice.totalPrice());
        assertEquals("NOT_REQUESTED", invoice.paymentStatus());
        assertNull(invoice.paymentDate());
        assertNull(invoice.hotelReservation());
    }

    @Test
    @DisplayName("Test create with invalid user id and expect status 404")
    void createInvalidUserId() throws Exception {
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 2}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(ID_NOT_FOUND + 2, error.getMessage());
    }

    @Test
    @DisplayName("Test update payment and expect status 200 and an invoice")
    void updatePayment() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UPDATE_EXAMPLE1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        InvoiceGetDto invoice = objectMapper.readValue(response, InvoiceGetDto.class);
        // THEN
        assertEquals(1L, invoice.id());
        assertEquals("PENDING", invoice.paymentStatus());
        assertEquals("2025-01-01T00:00", invoice.paymentDate().toString());
        assertEquals(0, invoice.flightTickets().size());
        assertEquals(0, invoice.totalPrice());
        assertNull(invoice.hotelReservation());
    }

    @Test
    @DisplayName("Test update payment with paid invoice and expect status 400")
    void updatePaymentPaidInvoice() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        mockMvc.perform(patch(BASE_URL + "1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(UPDATE_EXAMPLE1.replace("PENDING", "PAID")));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UPDATE_EXAMPLE1))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(CANNOT_UPDATE_INVOICE, error.getMessage());
    }

    @Test
    @DisplayName("Test delete and expect status 204")
    void deleteInvoice() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        // WHEN
        mockMvc.perform(delete(BASE_URL + 1))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Test delete with reservation and flight tickets and expect status 204 and empty db")
    void deleteInvoiceWithReservationAndFlightTickets() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        mockMvc.perform(post("/api/v1/reservations/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(HOTEL_EXAMPLE));
        mockMvc.perform(post("/api/v1/flights/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_EXAMPLE));

        // WHEN
        mockMvc.perform(delete(BASE_URL + 1))
                .andExpect(status().isNoContent());
        // THEN
        assertEquals(0, invoiceTestRepository.count());
        assertEquals(0, hotelReservationTestRepository.count());
        assertEquals(0, flightTicketTestRepository.count());
    }

    @Test
    @DisplayName("Test delete with paid or pending invoice and expect status 400")
    void deleteInvoicePaidInvoice() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        mockMvc.perform(patch(BASE_URL + "1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(UPDATE_EXAMPLE1));
        // WHEN
        String response1 = mockMvc.perform(delete(BASE_URL + "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError error1 = objectMapper.readValue(response1, AgencyError.class);
        mockMvc.perform(patch(BASE_URL + "1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(UPDATE_EXAMPLE1.replace("PENDING", "PAID")));
        String response2 = mockMvc.perform(delete(BASE_URL + "1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError error2 = objectMapper.readValue(response2, AgencyError.class);
        // THEN
        assertEquals(CANNOT_DELETE_INVOICE, error1.getMessage());
        assertEquals(CANNOT_DELETE_INVOICE, error2.getMessage());
    }
}