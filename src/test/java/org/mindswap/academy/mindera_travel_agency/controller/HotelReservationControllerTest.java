package org.mindswap.academy.mindera_travel_agency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.mindswap.academy.mindera_travel_agency.aspect.AgencyError;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.rooms.RoomInfoGetDto;
import org.mindswap.academy.mindera_travel_agency.repository.*;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.ExternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HotelReservationControllerTest {
    private static ObjectMapper objectMapper;
    private final String BASE_URL = "/api/v1/hotel_reservations/";
    private final String EXAMPLE1 = "{\"arrivalDate\": \"2030-01-01\",\"leaveDate\": \"2030-01-05\",\"invoiceId\": 1,\"hotelInfo\": {\"hotelN\": \"Hotel Teste\",\"location\": \"teste adress\",\"phoneNumber\": 120312312,\"rooms\": [{\"numberOfBeds\": 3,\"roomType\": \"TYPE\",\"roomPrice\": 10},{\"numberOfBeds\": 2,\"roomType\": \"TYPE2\",\"roomPrice\": 15}]}}";
    private final String EXAMPLE2 = "{\"arrivalDate\": \"2030-01-02\",\"leaveDate\": \"2030-01-06\",\"invoiceId\": 2,\"hotelInfo\": {\"hotelN\": \"Hotel Teste dois\",\"location\": \"teste adress dois\",\"phoneNumber\": 920312312,\"rooms\": [{\"numberOfBeds\": 3,\"roomType\": \"TYPE3\",\"roomPrice\": 12},{\"numberOfBeds\": 2,\"roomType\": \"TYPE4\",\"roomPrice\": 13},{\"numberOfBeds\": 3,\"roomType\": \"TYPE\",\"roomPrice\": 10}]}}";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserTestRepository userTestRepository;
    @Autowired
    private InvoiceTestRepository invoiceTestRepository;
    @Autowired
    private PaymentStatusTestRepository paymentStatusTestRepository;
    @Autowired
    private HotelReservationTestRepository hotelReservationTestRepository;
    @Autowired
    private RoomInfoTestRepository roomInfoTestRepository;
    @MockBean
    private ExternalService externalService;

    @BeforeAll
    static void setUpMapper() {
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
        mockMvc.perform(post("/api/v1/invoices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invoiceJson));
    }

    @AfterEach
    void tearDown() {
        roomInfoTestRepository.deleteAll();
        roomInfoTestRepository.resetAutoIncrement();
        hotelReservationTestRepository.deleteAll();
        hotelReservationTestRepository.resetAutoIncrement();
        invoiceTestRepository.deleteAll();
        invoiceTestRepository.resetAutoIncrement();
        paymentStatusTestRepository.deleteAll();
        paymentStatusTestRepository.resetAutoIncrement();
        userTestRepository.deleteAll();
        userTestRepository.resetAutoIncrement();
    }

    @Test
    @DisplayName("Test get all with 2 reservations and expect status 200 and list of 2 reservations")
    void getAll() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE2));
        // WHEN
        mockMvc.perform(get(BASE_URL + "?page=0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    @DisplayName("Test get all with 0 reservations and expect status 200 and empty list")
    void getAllEmpty() throws Exception {
        // WHEN
        mockMvc.perform(get(BASE_URL + "?page=0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    @DisplayName("Test get all multiple pages")
    void getAllMultiplePages() throws Exception {
        // GIVEN
        for (int i = 0; i < 4; i++) {
            mockMvc.perform(post("/api/v1/invoices/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"userId\":1}"));
        }
        for (int i = 0; i < 6; i++) {
            mockMvc.perform(post(BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(EXAMPLE1.replace("\"invoiceId\": 1", "\"invoiceId\": " + (i + 1))));
        }
        // WHEN
        mockMvc.perform(get(BASE_URL + "?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(5)));
        mockMvc.perform(get(BASE_URL + "?page=1&size=5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("Test get by id and expect status 200 and reservation")
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
        HotelReservationGetDto hotelReservationGetDto = objectMapper.readValue(response, HotelReservationGetDto.class);
        RoomInfoGetDto roomInfoGetDto = hotelReservationGetDto.rooms().stream().filter(room -> room.pricePerNight() == 10).findFirst().get();
        // THEN
        assertEquals(1L, hotelReservationGetDto.id());
        assertEquals("Hotel Teste", hotelReservationGetDto.hotelName());
        assertEquals("teste adress", hotelReservationGetDto.hotelAddress());
        assertEquals("120312312", hotelReservationGetDto.hotelPhoneNumber());
        assertEquals(25, hotelReservationGetDto.pricePerNight());
        assertEquals(4, hotelReservationGetDto.durationOfStay());
        assertEquals(100, hotelReservationGetDto.totalPrice());
        assertEquals("2030-01-01", hotelReservationGetDto.arrivalDate().toString());
        assertEquals("2030-01-05", hotelReservationGetDto.leaveDate().toString());
        assertEquals(2, hotelReservationGetDto.rooms().size());
        assertEquals(10, roomInfoGetDto.pricePerNight());
        assertEquals("TYPE", roomInfoGetDto.roomType());
        assertEquals(0, roomInfoGetDto.roomNumber());
        assertEquals(3, roomInfoGetDto.numberOfBeds());
    }

    @Test
    @DisplayName("Test get by id with incorrect id and expect status 404 and error message")
    void getByIdNotFound() throws Exception {
        // WHEN
        String response = mockMvc.perform(get(BASE_URL + "1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(HOTEL_RESERVATION_ID_NOT_FOUND + 1, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test create and expect status 201 and reservation and invoice price")
    void create() throws Exception {
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EXAMPLE1))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        HotelReservationGetDto hotelReservationGetDto = objectMapper.readValue(response, HotelReservationGetDto.class);
        RoomInfoGetDto roomInfoGetDto = hotelReservationGetDto.rooms().stream().filter(room -> room.pricePerNight() == 10).findFirst().get();
        String response2 = mockMvc.perform(get("/api/v1/invoices/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        InvoiceGetDto invoiceGetDto = objectMapper.readValue(response2, InvoiceGetDto.class);
        // THEN
        assertEquals(1L, hotelReservationGetDto.id());
        assertEquals("Hotel Teste", hotelReservationGetDto.hotelName());
        assertEquals("teste adress", hotelReservationGetDto.hotelAddress());
        assertEquals("120312312", hotelReservationGetDto.hotelPhoneNumber());
        assertEquals(25, hotelReservationGetDto.pricePerNight());
        assertEquals(4, hotelReservationGetDto.durationOfStay());
        assertEquals(100, hotelReservationGetDto.totalPrice());
        assertEquals("2030-01-01", hotelReservationGetDto.arrivalDate().toString());
        assertEquals("2030-01-05", hotelReservationGetDto.leaveDate().toString());
        assertEquals(2, hotelReservationGetDto.rooms().size());
        assertEquals(10, roomInfoGetDto.pricePerNight());
        assertEquals("TYPE", roomInfoGetDto.roomType());
        assertEquals(0, roomInfoGetDto.roomNumber());
        assertEquals(3, roomInfoGetDto.numberOfBeds());
        assertEquals(100, invoiceGetDto.totalPrice());
    }

    @Test
    @DisplayName("Test create with incorrect invoice id and expect status 404 and error message")
    void createInvoiceNotFound() throws Exception {
        // GIVEN
        String json = EXAMPLE1.replace("\"invoiceId\": 1", "\"invoiceId\": 3");
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(INVOICE_ID_NOT_FOUND + 3, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test create with incorrect validation and expect status 400 and error message")
    void createInvalid() throws Exception {
        // GIVEN
        String json = EXAMPLE1.replace("\"arrivalDate\": \"2030-01-01\"", "\"arrivalDate\": \"2024-01-01\"")
                .replace("\"leaveDate\": \"2030-01-05\"", "\"leaveDate\": \"2024-01-06\"")
                .replace("\"invoiceId\": 1", "\"invoiceId\": 0");
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertTrue(agencyError.getMessage().contains(INVALID_ARRIVAL_DATE));
        assertTrue(agencyError.getMessage().contains(INVALID_LEAVE_DATE));
        assertTrue(agencyError.getMessage().contains(INVALID_INVOICE_ID));
    }

    @Test
    @DisplayName("Test create with empty json and expect status 400 and error message")
    void createEmpty() throws Exception {
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertTrue(agencyError.getMessage().contains(INVALID_ARRIVAL_DATE));
        assertTrue(agencyError.getMessage().contains(INVALID_LEAVE_DATE));
        assertTrue(agencyError.getMessage().contains(INVALID_INVOICE_ID));
        assertTrue(agencyError.getMessage().contains(INVALID_HOTEL_INFO));
    }

    @Test
    @DisplayName("Test create with reservation already exists and expect status 400 and error message")
    void createInvoicePaid() throws Exception {
        //GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EXAMPLE1))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(HOTEL_RESERVATION_ALREADY_EXISTS, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test create with check in date after check out date and expect status 400 and error message")
    void createInvalidCheckInOutDate() throws Exception {
        // GIVEN
        String json = EXAMPLE1.replace("\"arrivalDate\": \"2030-01-01\"", "\"arrivalDate\": \"2030-01-05\"")
                .replace("\"leaveDate\": \"2030-01-05\"", "\"leaveDate\": \"2030-01-01\"");
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertTrue(agencyError.getMessage().contains(INVALID_CHECK_IN_OUT_DATE));
    }

    @Test
    @DisplayName("Test update duration and expect status 200 and updated duration and price")
    void updateDuration() throws Exception {
        // GIVEN
        String json = "{\"arrivalDate\": \"2025-01-02\",\"leaveDate\": \"2025-01-08\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/duration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        HotelReservationGetDto hotelReservationGetDto = objectMapper.readValue(response, HotelReservationGetDto.class);
        String response2 = mockMvc.perform(get("/api/v1/invoices/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        InvoiceGetDto invoiceGetDto = objectMapper.readValue(response2, InvoiceGetDto.class);
        // THEN
        assertEquals(1L, hotelReservationGetDto.id());
        assertEquals("Hotel Teste", hotelReservationGetDto.hotelName());
        assertEquals(25, hotelReservationGetDto.pricePerNight());
        assertEquals(6, hotelReservationGetDto.durationOfStay());
        assertEquals(150, hotelReservationGetDto.totalPrice());
        assertEquals("2025-01-02", hotelReservationGetDto.arrivalDate().toString());
        assertEquals("2025-01-08", hotelReservationGetDto.leaveDate().toString());
        assertEquals(2, hotelReservationGetDto.rooms().size());
        assertEquals(150, invoiceGetDto.totalPrice());
    }

    @Test
    @DisplayName("Test update duration with incorrect id and expect status 404 and error message")
    void updateDurationNotFound() throws Exception {
        // GIVEN
        String json = "{\"arrivalDate\": \"2025-01-02\",\"leaveDate\": \"2025-01-08\"}";
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/duration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(HOTEL_RESERVATION_ID_NOT_FOUND + 1, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test update duration with incorrect validation and expect status 400 and error message")
    void updateDurationInvalid() throws Exception {
        // GIVEN
        String json = "{\"arrivalDate\": \"2024-01-02\",\"leaveDate\": \"2024-01-08\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/duration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertTrue(agencyError.getMessage().contains(INVALID_LEAVE_DATE));
        assertTrue(agencyError.getMessage().contains(INVALID_ARRIVAL_DATE));
    }

    @Test
    @DisplayName("Test update duration with invoice paid and expect status 400 and error message")
    void updateDurationInvoicePaid() throws Exception {
        // GIVEN
        String json = "{\"arrivalDate\": \"2025-01-02\",\"leaveDate\": \"2025-01-08\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));

        mockMvc.perform(patch("/api/v1/invoices/1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"paymentStatus\": \"PAID\"}"));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/duration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(CANNOT_ALTER_HOTEL_RESERVATION, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test update duration with check in date after check out date and expect status 400 and error message")
    void updateDurationInvalidCheckInOutDate() throws Exception {

        // GIVEN
        String json = "{\"arrivalDate\": \"2030-01-08\",\"leaveDate\": \"2030-01-02\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/duration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);

        // THEN
        assertEquals(INVALID_CHECK_IN_OUT_DATE, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test add room and expect status 200 and updated price")
    void addRoom() throws Exception {
        // GIVEN
        String json = "{\"numberOfBeds\":1,\"roomType\":\"TYPE\",\"roomPrice\":5}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));

        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/rooms/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        HotelReservationGetDto hotelReservationGetDto = objectMapper.readValue(response, HotelReservationGetDto.class);
        RoomInfoGetDto roomInfoGetDto = hotelReservationGetDto.rooms().stream().filter(room -> room.pricePerNight() == 5).findFirst().get();
        String response2 = mockMvc.perform(get("/api/v1/invoices/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        InvoiceGetDto invoiceGetDto = objectMapper.readValue(response2, InvoiceGetDto.class);
        // THEN
        assertEquals(1L, hotelReservationGetDto.id());
        assertEquals("Hotel Teste", hotelReservationGetDto.hotelName());
        assertEquals(30, hotelReservationGetDto.pricePerNight());
        assertEquals(4, hotelReservationGetDto.durationOfStay());
        assertEquals(120, hotelReservationGetDto.totalPrice());
        assertEquals(3, hotelReservationGetDto.rooms().size());
        assertEquals(5, roomInfoGetDto.pricePerNight());
        assertEquals("TYPE", roomInfoGetDto.roomType());
        assertEquals(0, roomInfoGetDto.roomNumber());
        assertEquals(1, roomInfoGetDto.numberOfBeds());
        assertEquals(120, invoiceGetDto.totalPrice());
    }

    @Test
    @DisplayName("Test add room with incorrect id and expect status 404 and error message")
    void addRoomNotFound() throws Exception {
        // GIVEN
        String json = "{\"numberOfBeds\":1,\"roomType\":\"TYPE\",\"roomPrice\":5}";
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/rooms/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);

        // THEN
        assertEquals(HOTEL_RESERVATION_ID_NOT_FOUND + 1, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test add room with invoice paid and expect status 400 and error message")
    void addRoomInvoicePaid() throws Exception {
        // GIVEN
        String json = "{\"numberOfBeds\":1,\"roomType\":\"TYPE\",\"roomPrice\":5}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));

        mockMvc.perform(patch("/api/v1/invoices/1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"paymentStatus\": \"PAID\"}"));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/rooms/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(CANNOT_ALTER_HOTEL_RESERVATION, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test remove room and expect status 200 and updated price")
    void removeRoom() throws Exception {
        // GIVEN
        String json = "{\"arrivalDate\": \"2030-01-01\",\"leaveDate\": \"2030-01-05\",\"invoiceId\": 1,\"hotelInfo\": {\"hotelN\": \"Hotel Teste\",\"location\": \"teste adress\",\"phoneNumber\": 120312312,\"rooms\": [{\"numberOfBeds\": 3,\"roomType\": \"TYPE\",\"roomPrice\": 10},{\"numberOfBeds\": 2,\"roomType\": \"TYPE2\",\"roomPrice\": 10}]}}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/rooms/1/remove"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        HotelReservationGetDto hotelReservationGetDto = objectMapper.readValue(response, HotelReservationGetDto.class);
        String response2 = mockMvc.perform(get("/api/v1/invoices/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        InvoiceGetDto invoiceGetDto = objectMapper.readValue(response2, InvoiceGetDto.class);
        // THEN
        assertEquals(1L, hotelReservationGetDto.id());
        assertEquals("Hotel Teste", hotelReservationGetDto.hotelName());
        assertEquals(10, hotelReservationGetDto.pricePerNight());
        assertEquals(4, hotelReservationGetDto.durationOfStay());
        assertEquals(40, hotelReservationGetDto.totalPrice());
        assertEquals(1, hotelReservationGetDto.rooms().size());
        assertEquals(40, invoiceGetDto.totalPrice());
        assertEquals(1, roomInfoTestRepository.count());
    }

    @Test
    @DisplayName("Test remove room with incorrect hotel id and expect status 404 and error message")
    void removeRoomNotFound() throws Exception {
        // GIVEN
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/rooms/1/remove"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(HOTEL_RESERVATION_ID_NOT_FOUND + 1, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test remove room with invoice paid and expect status 400 and error message")
    void removeRoomInvoicePaid() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        mockMvc.perform(patch("/api/v1/invoices/1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"paymentStatus\": \"PAID\"}"));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/rooms/1/remove"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(CANNOT_ALTER_HOTEL_RESERVATION, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test remove room with incorrect room id and expect status 404 and error message")
    void removeRoomNotFoundRoom() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/rooms/3/remove"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(ROOM_NOT_FOUND + 3, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test put request and expect status 200 and updated reservation")
    void updateReservation() throws Exception {
        // GIVEN
        String json = EXAMPLE2.replace("\"invoiceId\": 2", "\"invoiceId\": 1");
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        // WHEN
        String response = mockMvc.perform(put(BASE_URL + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        HotelReservationGetDto hotelReservationGetDto = objectMapper.readValue(response, HotelReservationGetDto.class);
        RoomInfoGetDto roomInfoGetDto = hotelReservationGetDto.rooms().stream().filter(room -> room.pricePerNight() == 10).findFirst().get();
        String response2 = mockMvc.perform(get("/api/v1/invoices/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        InvoiceGetDto invoiceGetDto = objectMapper.readValue(response2, InvoiceGetDto.class);
        // THEN
        assertEquals(1L, hotelReservationGetDto.id());
        assertEquals("Hotel Teste dois", hotelReservationGetDto.hotelName());
        assertEquals("teste adress dois", hotelReservationGetDto.hotelAddress());
        assertEquals("920312312", hotelReservationGetDto.hotelPhoneNumber());
        assertEquals(35, hotelReservationGetDto.pricePerNight());
        assertEquals(4, hotelReservationGetDto.durationOfStay());
        assertEquals(140, hotelReservationGetDto.totalPrice());
        assertEquals("2030-01-02", hotelReservationGetDto.arrivalDate().toString());
        assertEquals("2030-01-06", hotelReservationGetDto.leaveDate().toString());
        assertEquals(3, hotelReservationGetDto.rooms().size());
        assertEquals(10, roomInfoGetDto.pricePerNight());
        assertEquals("TYPE", roomInfoGetDto.roomType());
        assertEquals(3, roomInfoGetDto.numberOfBeds());
        assertEquals(3, roomInfoTestRepository.count());
        assertEquals(140, invoiceGetDto.totalPrice());
    }

    @Test
    @DisplayName("Test put request with invalid invoice id and expect status 404 and error message")
    void updateReservationNotFound() throws Exception {
        // GIVEN
        String json = EXAMPLE2.replace("\"invoiceId\": 2", "\"invoiceId\": 3");
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        // WHEN
        String response = mockMvc.perform(put(BASE_URL + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(INVOICE_ID_NOT_FOUND + 3, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test put request with different invoice id and expect status 404 and error message")
    void updateReservationDifferentInvoice() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        // WHEN
        String response = mockMvc.perform(put(BASE_URL + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EXAMPLE2))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(CANNOT_CHANGE_INVOICE, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test put request with paid invoice and expect status 400 and error message")
    void updateReservationInvoicePaid() throws Exception {
        // GIVEN
        String json = EXAMPLE2.replace("\"invoiceId\": 2", "\"invoiceId\": 1");
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));

        mockMvc.perform(patch("/api/v1/invoices/1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"paymentStatus\": \"PAID\"}"));
        // WHEN
        String response = mockMvc.perform(put(BASE_URL + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(CANNOT_ALTER_HOTEL_RESERVATION, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test put request with check in date after check out date and expect status 400 and error message")
    void updateReservationInvalidCheckInOutDate() throws Exception {
        // GIVEN
        String json = EXAMPLE2.replace("\"arrivalDate\": \"2030-01-02\"", "\"arrivalDate\": \"2030-01-06\"")
                .replace("\"leaveDate\": \"2030-01-06\"", "\"leaveDate\": \"2030-01-01\"")
                .replace("\"invoiceId\": 2", "\"invoiceId\": 1");
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        // WHEN
        String response = mockMvc.perform(put(BASE_URL + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertTrue(agencyError.getMessage().contains(INVALID_CHECK_IN_OUT_DATE));
    }

    @Test
    @DisplayName("Test delete request and expect status 200 and deleted room")
    void deleteAndExpectEmptyDb() throws Exception {
        // GIVEN
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        // WHEN
        mockMvc.perform(delete(BASE_URL + 1))
                .andExpect(status().isOk());
        // THEN
        assertEquals(0, hotelReservationTestRepository.count());
        assertEquals(0, roomInfoTestRepository.count());
    }

    @Test
    @DisplayName("Test delete request with incorrect id and expect status 404 and error message")
    void deleteNotFound() throws Exception {
        // WHEN
        String response = mockMvc.perform(delete(BASE_URL + 1))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(HOTEL_RESERVATION_ID_NOT_FOUND + 1, agencyError.getMessage());
    }
}