package org.mindswap.academy.mindera_travel_agency.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mindswap.academy.mindera_travel_agency.aspect.AgencyError;
import org.mindswap.academy.mindera_travel_agency.dto.external.flight.ExternalFlightInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.external.flight.ExternalPlaneInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.external.flight.ExternalPriceInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.external.hotel.ExternalHotelInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.external.hotel.ExternalRoomInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserGetDto;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
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
class UserControllerTest {
    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();
    private static ObjectMapper objectMapper;
    private final String BASE_URL = "/api/v1/users/";
    private final String USER_JSON = "{\"email\": \"teste@example.com\",\"password\": \"zxlmn!!23K?\",\"userName\": \"userTeste\",\"dateOfBirth\": \"2000-01-01\",\"phoneNumber\": \"937313732\",\"vat\": \"123456782\"}";
    private final String INVOICE_JSON = "{\"userId\":1}";
    private final List<String> PAYMENT_JSON = List.of("{\"statusName\": \"PENDING\"}", "{\"statusName\": \"PAID\"}", "{\"statusName\": \"NOT_REQUESTED\"}");
    private final String FLIGHT_TICKET_JSON = "{\"fName\": \"teste um\",\"email\": \"teste@example.com\",\"phone\": \"910410860\",\"fareClass\": \"FIRST\",\"price\": 100,\"maxLuggageWeight\": 22,\"carryOnLuggage\": true,\"duration\": 2.5,\"invoiceId\": 1,\"flightId\": 1,\"priceId\": 1}";
    private final String HOTEL_RESERVATION_JSON = "{\"arrivalDate\": \"2030-01-01\",\"leaveDate\": \"2030-01-05\",\"invoiceId\": 1,\"hotelInfo\": {\"hotelN\": \"Hotel Teste\",\"location\": \"teste adress\",\"phoneNumber\": 120312312,\"rooms\": [{\"numberOfBeds\": 3,\"roomType\": \"TYPE\",\"roomPrice\": 10},{\"numberOfBeds\": 2,\"roomType\": \"TYPE2\",\"roomPrice\": 15}]}}";
    private final ExternalFlightInfoDto EXTERNAL_FLIGHT_INFO_DTO = new ExternalFlightInfoDto(1L, "LIS", "OPO", 2, LocalDateTime.of(2030, 1, 1, 0, 0), 100, new ExternalPlaneInfoDto(1L, 100, 100, "TAP", "A320"), List.of(new ExternalPriceInfoDto(1L, 100, "FIRST")));
    private final ExternalHotelInfoDto EXTERNAL_HOTEL_INFO_DTO = new ExternalHotelInfoDto("Hotel Teste", "teste adress", "120312312", Set.of(new ExternalRoomInfoDto(3, 2, "TYPE", 15), new ExternalRoomInfoDto(2, 1, "TYPE2", 10)));
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserTestRepository userTestRepository;
    @Autowired
    private InvoiceTestRepository invoiceTestRepository;
    @Autowired
    private HotelReservationTestRepository hotelReservationTestRepository;
    @Autowired
    private FlightTicketTestRepository flightTicketTestRepository;
    @Autowired
    private PaymentStatusTestRepository paymentStatusTestRepository;

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

    @AfterEach
    void init() {
        flightTicketTestRepository.deleteAll();
        flightTicketTestRepository.resetAutoIncrement();
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
    @DisplayName("Test get all users when no users are in the database returns an empty list")
    void TestGetAllUsersWhenNoUsersAreInTheDatabaseReturnsAnEmptyList() throws Exception {
        mockMvc.perform(get(BASE_URL + "?page=0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(0)));
    }

    @Test
    @DisplayName("Test get all users when 2 users are in the database returns a list with 2 students")
    void TestGetAllUsersWhen2UsersAreInTheDatabaseReturnsListWith2Users() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON.replace("\"email\": \"teste@example.com\"", "\"email\": \"teste2@example.com\"")));

        mockMvc.perform(get(BASE_URL + "?page=0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    @DisplayName("Test get all active users with 1 active user in the database returns a list with 1 user")
    void TestGetAllActiveUsersWith1ActiveUserInTheDatabaseReturnsListWith1User() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));

        mockMvc.perform(get(BASE_URL + "active"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("Test get all active users with 1 active and 1 inactive user in the database returns a list with 1 user")
    void TestGetAllActiveUsersWith1ActiveAnd1InactiveUserInTheDatabaseReturnsListWith1User() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON.replace("\"email\": \"teste@example.com\"", "\"email\": \"teste2@example.com\"")));
        mockMvc.perform(delete(BASE_URL + "1"));

        mockMvc.perform(get(BASE_URL + "active"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("Test get user by id when user is not in the database returns status code 404.")
    void TestGetUserByIdWhenUserIsNotInTheDatabaseReturnsStatus404() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/1"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        assertNotNull(error.getMessage());
        assertEquals(USER_ID_NOT_FOUND + 1, error.getMessage());
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
        assertEquals("userTeste", user.userName());
        assertEquals("teste@example.com", user.email());
        assertEquals("2000-01-01", user.dateOfBirth().toString());
        assertEquals("937313732", user.phoneNumber());
        assertEquals(1, userTestRepository.count());
    }

    @Test
    @DisplayName("Test get user by email when user is not in the database returns status code 404.")
    void TestGetUserByEmailWhenUserIsNotInTheDatabaseReturnsStatus404() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "email/asd@cdaa.com"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);
        assertEquals(EMAIL_NOT_FOUND + "asd@cdaa.com", error.getMessage());
    }

    @Test
    @DisplayName("Test get user by email when user is in the database returns status code 200.")
    void TestGetUserByEmailWhenUserIsInTheDatabaseReturnsStatus200() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));

        String response = mockMvc.perform(get(BASE_URL + "email/teste@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        UserGetDto user = objectMapper.readValue(response, UserGetDto.class);

        assertEquals(1, user.id());
        assertEquals("userTeste", user.userName());
        assertEquals("teste@example.com", user.email());
        assertEquals("2000-01-01", user.dateOfBirth().toString());
        assertEquals("937313732", user.phoneNumber());
    }

    @Test
    @DisplayName("Test get all invoices when no invoices are in the database returns an empty list")
    void TestGetAllInvoicesWhenNoInvoicesAreInTheDatabaseReturnsAnEmptyList() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));
        mockMvc.perform(get(BASE_URL + "1/invoices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Test get all invoices when 2 invoices are in the database returns a list with 2 invoices")
    void TestGetAllInvoicesWhen2InvoicesAreInTheDatabaseReturnsListWith2Invoices() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));
        for (String json : PAYMENT_JSON) {
            mockMvc.perform(post("/api/v1/payment_status/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json));
        }
        mockMvc.perform(post("/api/v1/invoices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(INVOICE_JSON));
        mockMvc.perform(post("/api/v1/invoices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(INVOICE_JSON));

        mockMvc.perform(get(BASE_URL + "1/invoices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Test get all reservations when no reservations are in the database returns an empty list")
    void TestGetAllReservationsWhenNoReservationsAreInTheDatabaseReturnsAnEmptyList() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));
        mockMvc.perform(get(BASE_URL + "1/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Test get all reservations when 2 reservations are in the database returns a list with 2 reservations")
    void TestGetAllReservationsWhen2ReservationsAreInTheDatabaseReturnsListWith2Reservations() throws Exception {
        //TODO verificar
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));
        for (String json : PAYMENT_JSON) {
            mockMvc.perform(post("/api/v1/payment_status/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json));
        }
        mockMvc.perform(post("/api/v1/invoices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(INVOICE_JSON));
        mockMvc.perform(post("/api/v1/invoices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(INVOICE_JSON));
        mockMvc.perform(post("/api/v1/hotel_reservations/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(HOTEL_RESERVATION_JSON));
        mockMvc.perform(post("/api/v1/hotel_reservations/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(HOTEL_RESERVATION_JSON.replace("\"invoiceId\": 1", "\"invoiceId\": 2")));

        mockMvc.perform(get(BASE_URL + "1/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Test get all flight tickets when no flight tickets are in the database returns an empty list")
    void TestGetAllFlightTicketsWhenNoFlightTicketsAreInTheDatabaseReturnsAnEmptyList() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));
        mockMvc.perform(get(BASE_URL + "1/tickets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Test get all flight tickets when 2 flight tickets are in the database returns a list with 2 flight tickets")
    void TestGetAllFlightTicketsWhen2FlightTicketsAreInTheDatabaseReturnsListWith2FlightTickets() throws Exception {
        //TODO verificar
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));
        for (String json : PAYMENT_JSON) {
            mockMvc.perform(post("/api/v1/payment_status/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json));
        }
        mockMvc.perform(post("/api/v1/invoices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(INVOICE_JSON));
        mockMvc.perform(post("/api/v1/flights/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));
        mockMvc.perform(post("/api/v1/flights/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(FLIGHT_TICKET_JSON));

        mockMvc.perform(get(BASE_URL + "1/tickets"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Test get external flight info")
    void TestGetExternalFlightInfo() throws Exception {
        List<ExternalFlightInfoDto> externalFlightInfoDtoList = List.of(EXTERNAL_FLIGHT_INFO_DTO);
        wireMockServer.stubFor(WireMock.get("/api/v1/flights/LIS/OPO?date=2020-01-01T12:00&page=0&price=100")
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(externalFlightInfoDtoList))));
        String response = mockMvc.perform(get(BASE_URL + "/flights/LIS/OPO?price=100&date=2020-01-01T12:00&page=0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        ExternalFlightInfoDto externalFlightInfoDto = objectMapper.readValue(response, new TypeReference<List<ExternalFlightInfoDto>>() {
        }).getFirst();

        assertEquals(1, externalFlightInfoDto.id());
        assertEquals("LIS", externalFlightInfoDto.origin());
        assertEquals("OPO", externalFlightInfoDto.destination());
        assertEquals(2, externalFlightInfoDto.duration());
        assertEquals(LocalDateTime.of(2030, 1, 1, 0, 0), externalFlightInfoDto.dateOfFlight());
        assertEquals(100, externalFlightInfoDto.availableSeats());
        assertEquals(1, externalFlightInfoDto.plane().id());
        assertEquals(100, externalFlightInfoDto.plane().peopleCapacity());
        assertEquals(100, externalFlightInfoDto.plane().luggageCapacity());
        assertEquals("TAP", externalFlightInfoDto.plane().companyOwner());
        assertEquals("A320", externalFlightInfoDto.plane().modelName());
        assertEquals(1, externalFlightInfoDto.price().size());
        assertEquals(1, externalFlightInfoDto.price().get(0).id());
        assertEquals(100, externalFlightInfoDto.price().get(0).price());
        assertEquals("FIRST", externalFlightInfoDto.price().get(0).className());
    }

    @Test
    @DisplayName("Test get external hotel info")
    void TestGetExternalHotelInfo() throws Exception {
        //TODO ver com o jose
        List<ExternalHotelInfoDto> externalHotelInfoDtoList = List.of(EXTERNAL_HOTEL_INFO_DTO);
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
        assertTrue(error.getMessage().contains(INVALID_VAT));
    }

    @Test
    @DisplayName("Test create a user with invalid fields and return status code 400.")
    void TestCreateUserWithInvalidEmailAndReturnStatus400() throws Exception {
        String json = "{\"email\": \"testeexample.com\",\"password\": \"a\",\"userName\": \"!asdas#$\",\"dateOfBirth\": \"2030-01-01\",\"phoneNumber\": \"93731as3732\",\"vat\": \"1234asd56782\"}";
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);

        assertTrue(error.getMessage().contains(INVALID_EMAIL));
        assertTrue(error.getMessage().contains(INVALID_USER_NAME));
        assertTrue(error.getMessage().contains(INVALID_DATE));
        assertTrue(error.getMessage().contains(INVALID_PHONE_NUMBER));
        assertTrue(error.getMessage().contains(INVALID_PASSWORD));
        assertTrue(error.getMessage().contains(INVALID_VAT));
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
        assertEquals("userTeste", user.userName());
        assertEquals("teste@example.com", user.email());
        assertEquals("2000-01-01", user.dateOfBirth().toString());
        assertEquals("937313732", user.phoneNumber());
        assertEquals("123456782", userTestRepository.findById(1L).get().getVat());
        assertEquals("zxlmn!!23K?", userTestRepository.findById(1L).get().getPassword());
    }

    @Test
    @DisplayName("Test create a user with duplicate email and expect status 400")
    void TestCreateUserWithDuplicateEmailAndExpectStatus400() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);

        assertEquals(DUPLICATE_EMAIL, error.getMessage());

    }

    @Test
    @DisplayName("Test put request with correct data and expect status 200")
    void putRequestWithCorrectDataExpectStatus200() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));

        String updatedUserJson = USER_JSON.replace("\"email\": \"teste@example.com\"", "\"email\": \"teste2@example.com\"");

        String response = mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        UserGetDto user = objectMapper.readValue(response, UserGetDto.class);

        assertEquals(1, user.id());
        assertEquals("userTeste", user.userName());
        assertEquals("teste2@example.com", user.email());
        assertEquals("2000-01-01", user.dateOfBirth().toString());
        assertEquals("937313732", user.phoneNumber());
        assertEquals("123456782", userTestRepository.findById(1L).get().getVat());
        assertEquals("zxlmn!!23K?", userTestRepository.findById(1L).get().getPassword());
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

        assertEquals(USER_ID_NOT_FOUND + 9999, error.getMessage());
    }

    @Test
    @DisplayName("Test update a user with duplicate email and expect status 400")
    void TestUpdateUserWithDuplicateEmailAndExpectStatus400() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON.replace("\"email\": \"teste@example.com\"", "\"email\": \"teste2@example.com\"")));
        String response = mockMvc.perform(put(BASE_URL + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);

        assertEquals(DUPLICATE_EMAIL, error.getMessage());
    }


    @Test
    @DisplayName("Test patch request with correct data and expect status 200")
    void patchRequestWithCorrectDataExpectStatus200() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));

        String updatedUserJson = "{\"userName\":\"updated_joe\",\"phoneNumber\":\"911234234\",\"email\":\"teste2@example.com\",\"vat\":\"123456788\"}";

        String response = mockMvc.perform(patch(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        UserGetDto updatedUser = objectMapper.readValue(response, UserGetDto.class);

        assertEquals(1, updatedUser.id());
        assertEquals("updated_joe", updatedUser.userName());
        assertEquals("teste2@example.com", updatedUser.email());
        assertEquals("2000-01-01", updatedUser.dateOfBirth().toString());
        assertEquals("911234234", updatedUser.phoneNumber());
        assertEquals("123456788", userTestRepository.findById(1L).get().getVat());
        assertEquals("zxlmn!!23K?", userTestRepository.findById(1L).get().getPassword());
    }

    @Test
    @DisplayName("Test patch a user with only username and expect status 200 and only username updated")
    void TestPatchUserWithOnlyUsernameAndExpectStatus200AndOnlyUsernameUpdated() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));

        String updatedUserJson = "{\"userName\":\"updated_joe\"}";

        String response = mockMvc.perform(patch(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        UserGetDto updatedUser = objectMapper.readValue(response, UserGetDto.class);

        assertEquals(1, updatedUser.id());
        assertEquals("updated_joe", updatedUser.userName());
        assertEquals("teste@example.com", updatedUser.email());
        assertEquals("2000-01-01", updatedUser.dateOfBirth().toString());
        assertEquals("937313732", updatedUser.phoneNumber());
    }

    @Test
    @DisplayName("Test update password with correct data and expect status 200")
    void updatePasswordWithCorrectDataExpectStatus200() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));

        String updatedUserJson = "{\"oldPassword\":\"zxlmn!!23K?\",\"newPassword\":\"new123Password\"}";

        mockMvc.perform(patch(BASE_URL + "/1/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        userTestRepository.findById(1L).ifPresent(user -> assertEquals("new123Password", user.getPassword()));
    }

    @Test
    @DisplayName("Test update password with incorrect old password and expect status 400")
    void updatePasswordWithIncorrectOldPasswordAndExpectStatus400() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));

        String updatedUserJson = "{\"oldPassword\":\"wrong123Password\",\"newPassword\":\"new123Password\"}";

        String response = mockMvc.perform(patch(BASE_URL + "/1/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);

        assertEquals(PASSWORDS_DID_NOT_MATCH, error.getMessage());
    }


    @Test
    @DisplayName("Test delete request with user in db and expect status 200")
    void deleteWithUserInDb() throws Exception {

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_JSON));

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isOk());

        mockMvc.perform(get(BASE_URL + "active?page=0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(0)));
    }


    @Test
    @DisplayName("Test delete a user with non-existing id and expect status 404")
    void TestDeleteUserWithNonExistingIdAndExpectStatus404() throws Exception {
        String response = mockMvc.perform(delete(BASE_URL + "9999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        AgencyError error = objectMapper.readValue(response, AgencyError.class);

        assertEquals(USER_ID_NOT_FOUND + 9999, error.getMessage());
    }


}
