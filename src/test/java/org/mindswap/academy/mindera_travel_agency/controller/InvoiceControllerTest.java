package org.mindswap.academy.mindera_travel_agency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mindswap.academy.mindera_travel_agency.aspect.AgencyError;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.repository.*;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
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
class InvoiceControllerTest {
    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();
    private static ObjectMapper objectMapper;
    private final String BASE_URL = "/api/v1/invoices/";
    private final String EXAMPLE1 = "{\"userId\": 1}";
    private final String HOTEL_EXAMPLE = "{\"arrivalDate\": \"2030-01-01\",\"leaveDate\": \"2030-01-05\",\"invoiceId\": 1,\"hotelInfo\": {\"hotelN\": \"Hotel Teste\",\"location\": \"teste adress\",\"phoneNumber\": 120312312,\"rooms\": [{\"numberOfBeds\": 3,\"roomType\": \"TYPE\",\"roomPrice\": 10},{\"numberOfBeds\": 2,\"roomType\": \"TYPE2\",\"roomPrice\": 15}]}}";
    private final String FLIGHT_EXAMPLE = "{\"fName\": \"teste um\",\"email\": \"teste@example.com\",\"phone\": \"910410860\",\"fareClass\": \"FIRST\",\"price\": 100,\"maxLuggageWeight\": 22,\"carryOnLuggage\": true,\"duration\": 2.5,\"invoiceId\": 1,\"flightId\": 1,\"priceId\": 1}";
    private final String UPDATE_EXAMPLE1 = "{\"paymentStatus\": \"PENDING\", \"paymentDate\": \"2030-01-01T00:00:00\"}";
    private final String hotelApiResponse = "{\"arrival\": \"2030-01-01\",\"departure\": \"2030-01-05\",\"hotelN\": \"Hotel Teste\",\"fName\": \"userTeste\",\"phoneNumber\": \"120312312\",\"vat\": \"123456782\",\"roomReservations\": [{\"numberOfBeds\": 3,\"roomType\": \"TYPE\",\"roomPrice\": 10, \"roomNumber\": 1},{\"numberOfBeds\": 2,\"roomType\": \"TYPE2\",\"roomPrice\": 15, \"roomNumber\": 2}]}";
    private final String bookingsApiResponse = "[{\"id\": 1,\"fName\": \"teste um\",\"email\": \"teste@example.com\",\"phone\": \"910410860\",\"seatNumber\": \"1A\",\"flight\": {\"id\": 1,\"origin\": \"LIS\",\"destination\": \"OPO\",\"duration\": 2.5,\"dateOfFlight\": \"2030-01-01T00:00:00\",\"availableSeats\": 100,\"plane\": {\"id\": 1,\"peopleCapacity\": 100,\"luggageCapacity\": 100,\"companyOwner\": \"TAP\",\"modelName\": \"A320\"},\"price\": [{\"id\": 1,\"className\": \"FIRST\",\"price\": 100}]},\"price\": {\"id\": 1,\"price\": 100,\"className\": \"FIRST\"}}]";
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
    private HotelReservationTestRepository hotelReservationTestRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("hotel.api.base-url", wireMockServer::baseUrl);
        registry.add("flight.api.base-url", wireMockServer::baseUrl);
    }

    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() throws Exception {
        String userJson = "{\"email\": \"teste@example.com\",\"password\": \"zxlmn!!23K?\",\"userName\": \"userTeste\",\"dateOfBirth\": \"2000-01-01\",\"phoneNumber\": \"937313732\",\"vat\": \"123456782\"}";
        List<String> paymentStatusJson = List.of("{\"statusName\": \"PENDING\"}", "{\"statusName\": \"PAID\"}", "{\"statusName\": \"NOT_REQUESTED\"}");

        mockMvc.perform(post("/api/v1/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));
        for (String json : paymentStatusJson) {
            mockMvc.perform(post("/api/v1/payment_status/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json));
        }
    }

    @AfterEach
    void tearDown() {
        hotelReservationTestRepository.deleteAll();
        hotelReservationTestRepository.resetAutoIncrement();
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
    @DisplayName("Test get all and expect status 200 and a list of invoices")
    void getAllPaged() throws Exception {
        // GIVEN
        for (int i = 0; i < 30; i++) {
            mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(EXAMPLE1));
        }
        // WHEN
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
    @DisplayName("Test get all with empty db and expect status 200 and an empty list")
    void getAllEmpty() throws Exception {
        // WHEN
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(0)));

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
        assertEquals(INVOICE_ID_NOT_FOUND + 1, error.getMessage());
    }

    @Test
    @DisplayName("Test get with reservation and flight tickets and expect status 200 update prices")
    void getByIdWithReservationAndFlightTickets() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        mockMvc.perform(post("/api/v1/hotel_reservations/")
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
        assertEquals(USER_ID_NOT_FOUND + 2, error.getMessage());
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
        assertEquals("2030-01-01T00:00", invoice.paymentDate().toString());
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
    @DisplayName("Test finalize invoice and expect status 200 and an invoice")
    void finalizeInvoice() throws Exception {
        // GIVEN
        wireMockServer.stubFor(WireMock.post(WireMock.urlMatching("/api/v1/bookings"))
                .inScenario("finalize invoice")
                .whenScenarioStateIs(STARTED)
                .willReturn(WireMock.aResponse()
                        .withStatus(201)
                        .withBody(bookingsApiResponse))
                .willSetStateTo("Flight tickets created"));
        wireMockServer.stubFor(WireMock.post(WireMock.urlMatching("/api/v1/reservations/Hotel-Teste"))
                .inScenario("finalize invoice")
                .whenScenarioStateIs("Flight tickets created")
                .willReturn(WireMock.aResponse()
                        .withStatus(201)
                        .withBody(hotelApiResponse)));
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        mockMvc.perform(post("/api/v1/hotel_reservations/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(HOTEL_EXAMPLE));
        mockMvc.perform(post("/api/v1/flights/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_EXAMPLE));

        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/finalize"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        InvoiceGetDto invoice = objectMapper.readValue(response, InvoiceGetDto.class);
        // THEN
        assertEquals(1L, invoice.id());
        assertEquals("PENDING", invoice.paymentStatus());
        assertEquals(200, invoice.totalPrice());
        assertEquals(1L, invoice.hotelReservation().id());
        assertEquals(1, invoice.flightTickets().size());
        assertEquals("1A", invoice.flightTickets().get(0).seatNumber());
        assertEquals(1, invoice.flightTickets().get(0).ticketNumber());
        assertTrue(invoice.hotelReservation().rooms().stream().anyMatch(room -> room.roomNumber() == 1));
        assertTrue(invoice.hotelReservation().rooms().stream().anyMatch(room -> room.roomNumber() == 2));
    }

    @Test
    @DisplayName("Test finalize invoice with missing hotel reservation and expect status 400")
    void finalizeInvoiceMissingHotelReservation() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        mockMvc.perform(post("/api/v1/flights/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_EXAMPLE));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/finalize"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(INVOICE_NOT_COMPLETE, error.getMessage());
    }

    @Test
    @DisplayName("Test finalize invoice with missing flight tickets and expect status 400")
    void finalizeInvoiceMissingFlightTickets() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        mockMvc.perform(post("/api/v1/hotel_reservations/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(HOTEL_EXAMPLE));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/finalize"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(INVOICE_NOT_COMPLETE, error.getMessage());
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
                .andExpect(status().isOk());
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
                .andExpect(status().isOk());
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