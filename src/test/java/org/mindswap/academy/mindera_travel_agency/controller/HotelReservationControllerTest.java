package org.mindswap.academy.mindera_travel_agency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.mindswap.academy.mindera_travel_agency.aspect.AgencyError;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.rooms.RoomInfoGetDto;
import org.mindswap.academy.mindera_travel_agency.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

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
    //TODO 2 rooms with same externalId same hotel
    private static ObjectMapper objectMapper;
    private final String BASE_URL = "/api/v1/reservations/";
    private final String EXAMPLE1 = "{\"invoiceId\": 1,\"checkInDate\": \"2025-01-01T12:00:00\",\"checkOutDate\": \"2025-01-05T12:00:00\",\"hotelInfo\": {\"externalId\": 1,\"name\": \"Hotel Teste\",\"address\": \"teste adress\",\"phoneNumber\": \"120312312\",\"rooms\": [{\"externalId\":1,\"pricePerNight\":10,\"roomType\":\"TYPE\",\"roomNumber\":101,\"numberOfBeds\":3},{\"externalId\":2,\"pricePerNight\":15,\"roomType\":\"TYPE2\",\"roomNumber\":102,\"numberOfBeds\":2}]}}";
    private final String EXAMPLE2 = "{\"invoiceId\": 2,\"checkInDate\": \"2025-01-01T16:00:00\",\"checkOutDate\": \"2025-01-05T16:00:00\",\"hotelInfo\": {\"externalId\": 2,\"name\": \"Hotel Teste dois\",\"address\": \"teste adress dois\",\"phoneNumber\": \"920312312\",\"rooms\": [{\"externalId\":3,\"pricePerNight\":10,\"roomType\":\"TYPE\",\"roomNumber\":103,\"numberOfBeds\":3},{\"externalId\":4,\"pricePerNight\":15,\"roomType\":\"TYPE2\",\"roomNumber\":104,\"numberOfBeds\":2}]}}";
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

    @BeforeAll
    static void setUpMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() throws Exception {
        String userJson = "{\"email\": \"teste@example.com\",\"password\": \"C@$9gmL?\",\"userName\": \"userTeste\",\"dateOfBirth\": \"2000-01-01\",\"phoneNumber\": \"937313732\"}";
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
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Test get all with 0 reservations and expect status 200 and empty list")
    void getAllEmpty() throws Exception {
        // WHEN
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
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
        RoomInfoGetDto roomInfoGetDto = hotelReservationGetDto.rooms().stream().filter(room -> room.roomNumber() == 101).findFirst().get();
        // THEN
        assertEquals(1L, hotelReservationGetDto.id());
        assertEquals("Hotel Teste", hotelReservationGetDto.hotelName());
        assertEquals("teste adress", hotelReservationGetDto.hotelAddress());
        assertEquals(120312312, hotelReservationGetDto.hotelPhoneNumber());
        assertEquals(25, hotelReservationGetDto.pricePerNight());
        assertEquals(4, hotelReservationGetDto.durationOfStay());
        assertEquals(100, hotelReservationGetDto.totalPrice());
        assertEquals("2025-01-01T12:00", hotelReservationGetDto.checkInDate().toString());
        assertEquals("2025-01-05T12:00", hotelReservationGetDto.checkOutDate().toString());
        assertEquals(2, hotelReservationGetDto.rooms().size());
        assertEquals(10, roomInfoGetDto.pricePerNight());
        assertEquals("TYPE", roomInfoGetDto.roomType());
        assertEquals(101, roomInfoGetDto.roomNumber());
        assertEquals(3, roomInfoGetDto.numberOfBeds());
    }

    @Test
    @DisplayName("Test get by id with incorrect id and expect status 404 and error message")
    public void getByIdNotFound() throws Exception {
        // WHEN
        String response = mockMvc.perform(get(BASE_URL + "1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(ID_NOT_FOUND + 1, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test create and expect status 201 and reservation")
    void create() throws Exception {
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EXAMPLE1))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        HotelReservationGetDto hotelReservationGetDto = objectMapper.readValue(response, HotelReservationGetDto.class);
        RoomInfoGetDto roomInfoGetDto = hotelReservationGetDto.rooms().stream().filter(room -> room.roomNumber() == 101).findFirst().get();
        // THEN
        assertEquals(1L, hotelReservationGetDto.id());
        assertEquals("Hotel Teste", hotelReservationGetDto.hotelName());
        assertEquals("teste adress", hotelReservationGetDto.hotelAddress());
        assertEquals(120312312, hotelReservationGetDto.hotelPhoneNumber());
        assertEquals(25, hotelReservationGetDto.pricePerNight());
        assertEquals(4, hotelReservationGetDto.durationOfStay());
        assertEquals(100, hotelReservationGetDto.totalPrice());
        assertEquals("2025-01-01T12:00", hotelReservationGetDto.checkInDate().toString());
        assertEquals("2025-01-05T12:00", hotelReservationGetDto.checkOutDate().toString());
        assertEquals(2, hotelReservationGetDto.rooms().size());
        assertEquals(10, roomInfoGetDto.pricePerNight());
        assertEquals("TYPE", roomInfoGetDto.roomType());
        assertEquals(101, roomInfoGetDto.roomNumber());
        assertEquals(3, roomInfoGetDto.numberOfBeds());
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
        assertEquals(ID_NOT_FOUND + 3, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test create with incorrect validation and expect status 400 and error message")
    void createInvalid() throws Exception {
        // GIVEN
        String json = EXAMPLE1.replace("\"checkInDate\": \"2025-01-01T12:00:00\"", "\"checkInDate\": \"2024-01-01T12:00:00\"")
                .replace("\"checkOutDate\": \"2025-01-05T12:00:00\"", "\"checkOutDate\": \"2024-01-05T12:00:00\"")
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
        assertTrue(agencyError.getMessage().contains(INVALID_DATE));
        assertTrue(agencyError.getMessage().contains(INVALID_ID));
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
        assertTrue(agencyError.getMessage().contains(INVALID_DATE));
        assertTrue(agencyError.getMessage().contains(INVALID_HOTEL_INFO));
        assertTrue(agencyError.getMessage().contains(INVALID_ID));
    }

    @Test
    @DisplayName("Test create with invoice paid and expect status 400 and error message")
    void createInvoicePaid() throws Exception {
        //GIVEN
        mockMvc.perform(patch("/api/v1/invoices/1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"paymentStatus\": \"PAID\"}"));
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(EXAMPLE1))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(CANNOT_ALTER_HOTEL_RESERVATION, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test create with check in date after check out date and expect status 400 and error message")
    void createInvalidCheckInOutDate() throws Exception {
        // GIVEN
        String json = EXAMPLE1.replace("\"checkInDate\": \"2025-01-01T12:00:00\"", "\"checkInDate\": \"2025-01-05T12:00:00\"")
                .replace("\"checkOutDate\": \"2025-01-05T12:00:00\"", "\"checkOutDate\": \"2025-01-01T12:00:00\"");
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
        String json = "{\"checkInDate\": \"2025-01-02T12:00:00\",\"checkOutDate\": \"2025-01-08T12:00:00\"}";
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
        // THEN
        assertEquals(1L, hotelReservationGetDto.id());
        assertEquals("Hotel Teste", hotelReservationGetDto.hotelName());
        assertEquals("teste adress", hotelReservationGetDto.hotelAddress());
        assertEquals(120312312, hotelReservationGetDto.hotelPhoneNumber());
        assertEquals(25, hotelReservationGetDto.pricePerNight());
        assertEquals(6, hotelReservationGetDto.durationOfStay());
        assertEquals(150, hotelReservationGetDto.totalPrice());
        assertEquals("2025-01-02T12:00", hotelReservationGetDto.checkInDate().toString());
        assertEquals("2025-01-08T12:00", hotelReservationGetDto.checkOutDate().toString());
        assertEquals(2, hotelReservationGetDto.rooms().size());
    }

    @Test
    @DisplayName("Test update duration with incorrect id and expect status 404 and error message")
    void updateDurationNotFound() throws Exception {
        // GIVEN
        String json = "{\"checkInDate\": \"2025-01-02T12:00:00\",\"checkOutDate\": \"2025-01-08T12:00:00\"}";
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/duration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(ID_NOT_FOUND + 1, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test update duration with incorrect validation and expect status 400 and error message")
    void updateDurationInvalid() throws Exception {
        // GIVEN
        String json = "{\"checkInDate\": \"2024-01-02T12:00:00\",\"checkOutDate\": \"2024-01-08T12:00:00\"}";
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
        assertTrue(agencyError.getMessage().contains(INVALID_DATE));
    }

    @Test
    @DisplayName("Test update duration with invoice paid and expect status 400 and error message")
    public void updateDurationInvoicePaid() throws Exception {
        // GIVEN
        String json = "{\"checkInDate\": \"2025-01-02T12:00:00\",\"checkOutDate\": \"2025-01-08T12:00:00\"}";
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
        String json = "{\"checkInDate\": \"2025-01-08T12:00:00\",\"checkOutDate\": \"2025-01-02T12:00:00\"}";
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
        assertTrue(agencyError.getMessage().contains(INVALID_CHECK_IN_OUT_DATE));
    }

    @Test
    @DisplayName("Test add room and expect status 200 and updated price")
    void addRoom() throws Exception {
        // GIVEN
        String json = "{\"externalId\":3,\"pricePerNight\":5,\"roomType\":\"TYPE\",\"roomNumber\":103,\"numberOfBeds\":3}";
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
        RoomInfoGetDto roomInfoGetDto = hotelReservationGetDto.rooms().stream().filter(room -> room.roomNumber() == 103).findFirst().get();
        // THEN
        assertEquals(1L, hotelReservationGetDto.id());
        assertEquals("Hotel Teste", hotelReservationGetDto.hotelName());
        assertEquals(30, hotelReservationGetDto.pricePerNight());
        assertEquals(4, hotelReservationGetDto.durationOfStay());
        assertEquals(120, hotelReservationGetDto.totalPrice());
        assertEquals(3, hotelReservationGetDto.rooms().size());
        assertEquals(5, roomInfoGetDto.pricePerNight());
        assertEquals("TYPE", roomInfoGetDto.roomType());
        assertEquals(103, roomInfoGetDto.roomNumber());
        assertEquals(3, roomInfoGetDto.numberOfBeds());
    }

    @Test
    @DisplayName("Test add room with incorrect id and expect status 404 and error message")
    void addRoomNotFound() throws Exception {
        // GIVEN
        String json = "{\"externalId\":3,\"pricePerNight\":5,\"roomType\":\"TYPE\",\"roomNumber\":103,\"numberOfBeds\":3}";
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/rooms/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);

        // THEN
        assertEquals(ID_NOT_FOUND + 1, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test add room with invoice paid and expect status 400 and error message")
    void addRoomInvoicePaid() throws Exception {
        // GIVEN
        String json = "{\"externalId\":3,\"pricePerNight\":5,\"roomType\":\"TYPE\",\"roomNumber\":103,\"numberOfBeds\":3}";
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
        String json = "{\"externalId\":1}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/rooms/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        HotelReservationGetDto hotelReservationGetDto = objectMapper.readValue(response, HotelReservationGetDto.class);
        Optional<RoomInfoGetDto> roomInfoGetDto = hotelReservationGetDto.rooms().stream().filter(room -> room.roomNumber() == 101).findFirst();
        // THEN
        assertEquals(1L, hotelReservationGetDto.id());
        assertEquals("Hotel Teste", hotelReservationGetDto.hotelName());
        assertEquals(15, hotelReservationGetDto.pricePerNight());
        assertEquals(4, hotelReservationGetDto.durationOfStay());
        assertEquals(60, hotelReservationGetDto.totalPrice());
        assertEquals(1, hotelReservationGetDto.rooms().size());
        assertTrue(roomInfoGetDto.isEmpty());
    }

    @Test
    @DisplayName("Test remove room with incorrect hotel id and expect status 404 and error message")
    void removeRoomNotFound() throws Exception {
        // GIVEN
        String json = "{\"externalId\":1}";
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/rooms/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(ID_NOT_FOUND + 1, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test remove room with invoice paid and expect status 400 and error message")
    void removeRoomInvoicePaid() throws Exception {
        // GIVEN
        String json = "{\"externalId\":1}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));

        mockMvc.perform(patch("/api/v1/invoices/1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"paymentStatus\": \"PAID\"}"));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/rooms/remove")
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
    @DisplayName("Test remove room with incorrect room id and expect status 404 and error message")
    void removeRoomNotFoundRoom() throws Exception {
        // GIVEN
        String json = "{\"externalId\":3}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(EXAMPLE1));
        // WHEN
        String response = mockMvc.perform(patch(BASE_URL + "1/rooms/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(ROOM_NOT_FOUND, agencyError.getMessage());
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
        RoomInfoGetDto roomInfoGetDto = hotelReservationGetDto.rooms().stream().filter(room -> room.roomNumber() == 103).findFirst().get();
        // THEN
        assertEquals(1L, hotelReservationGetDto.id());
        assertEquals("Hotel Teste dois", hotelReservationGetDto.hotelName());
        assertEquals("teste adress dois", hotelReservationGetDto.hotelAddress());
        assertEquals(920312312, hotelReservationGetDto.hotelPhoneNumber());
        assertEquals(25, hotelReservationGetDto.pricePerNight());
        assertEquals(4, hotelReservationGetDto.durationOfStay());
        assertEquals(100, hotelReservationGetDto.totalPrice());
        assertEquals("2025-01-01T16:00", hotelReservationGetDto.checkInDate().toString());
        assertEquals("2025-01-05T16:00", hotelReservationGetDto.checkOutDate().toString());
        assertEquals(2, hotelReservationGetDto.rooms().size());
        assertEquals(10, roomInfoGetDto.pricePerNight());
        assertEquals("TYPE", roomInfoGetDto.roomType());
        assertEquals(103, roomInfoGetDto.roomNumber());
        assertEquals(3, roomInfoGetDto.numberOfBeds());
        assertEquals(2, roomInfoTestRepository.count());
    }

    @Test
    @DisplayName("Test put request with different invoice id and expect status 404 and error message")
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
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(INVALID_INVOICE, agencyError.getMessage());
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
        String json = EXAMPLE2.replace("\"checkInDate\": \"2025-01-01T16:00:00\"", "\"checkInDate\": \"2025-01-05T16:00:00\"")
                .replace("\"checkOutDate\": \"2025-01-05T16:00:00\"", "\"checkOutDate\": \"2025-01-01T16:00:00\"")
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
                .andExpect(status().isNoContent());
        // THEN
        assertEquals(0, hotelReservationTestRepository.count());
        assertEquals(0, roomInfoTestRepository.count());
    }
}