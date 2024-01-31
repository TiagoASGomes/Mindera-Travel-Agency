package org.mindswap.academy.mindera_travel_agency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.rooms.RoomInfoGetDto;
import org.mindswap.academy.mindera_travel_agency.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Profile("test")
class HotelReservationControllerTest {
    private static ObjectMapper objectMapper;
    private final String BASE_URL = "/api/v1/reservations/";
    private final String EXAMPLE1 = "{\"invoiceId\": 1,\"checkInDate\": \"2025-01-01T12:00:00\",\"checkOutDate\": \"2025-01-05T12:00:00\",\"hotelInfo\": {\"externalIdId\": 1,\"name\": \"Hotel Teste\",\"address\": \"teste adress\",\"phoneNumber\": \"120312312\",\"rooms\": [{\"externalId\":1,\"pricePerNight\":10,\"roomType\":\"TYPE\",\"roomNumber\":101,\"numberOfBeds\":3}]}}";
    private final String EXAMPLE2 = "{\"checkInDate\": \"2025-01-01T16:00:00\",\"checkOutDate\": \"2025-01-05T16:00:00\",\"hotelInfo\": {\"externalIdId\": 2,\"name\": \"Hotel Teste dois\",\"address\": \"teste adress dois\",\"phoneNumber\": \"920312312\"},\"rooms\": [{\"externalId\":3,\"pricePerNight\":12,\"roomType\":\"TYPE\",\"roomNumber\":103,\"numberOfBeds\":3}]}\"invoiceId\": 2}";
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
                .andExpect(jsonPath("$", hasSize(1)));
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
        RoomInfoGetDto roomInfoGetDto = hotelReservationGetDto.rooms().get(0);
        // THEN
        assertEquals(1L, hotelReservationGetDto.id());
        assertEquals("Hotel Teste", hotelReservationGetDto.hotelName());
        assertEquals("teste adress", hotelReservationGetDto.hotelAddress());
        assertEquals(120312312, hotelReservationGetDto.hotelPhoneNumber());
        assertEquals(10, hotelReservationGetDto.pricePerNight());
        assertEquals(4, hotelReservationGetDto.durationOfStay());
        assertEquals(40, hotelReservationGetDto.totalPrice());
        assertEquals("2025-01-01T12:00", hotelReservationGetDto.checkInDate().toString());
        assertEquals("2025-01-05T12:00", hotelReservationGetDto.checkOutDate().toString());
        assertEquals(1, hotelReservationGetDto.rooms().size());
        assertEquals(1L, roomInfoGetDto.id());
        assertEquals(10, roomInfoGetDto.pricePerNight());
        assertEquals("TYPE", roomInfoGetDto.roomType());
        assertEquals(101, roomInfoGetDto.roomNumber());
        assertEquals(3, roomInfoGetDto.numberOfBeds());
    }

    @Test
    void create() {
    }

    @Test
    void updateDuration() {
    }

    @Test
    void addRoom() {
    }

    @Test
    void removeRoom() {
    }

    @Test
    void updateReservation() {
    }

    @Test
    void delete() {
    }
}