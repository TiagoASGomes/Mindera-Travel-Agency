package org.mindswap.academy.mindera_travel_agency.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindswap.academy.mindera_travel_agency.aspect.AgencyError;
import org.mindswap.academy.mindera_travel_agency.dto.payment_status.PaymentStatusGetDto;
import org.mindswap.academy.mindera_travel_agency.repository.InvoiceTestRepository;
import org.mindswap.academy.mindera_travel_agency.repository.PaymentStatusTestRepository;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(RedisConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ImportAutoConfiguration(classes = {
        CacheAutoConfiguration.class,
        RedisAutoConfiguration.class})
@EnableCaching
class PaymentStatusControllerTest {
    private final String BASE_URL = "/api/v1/payment_status/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PaymentStatusTestRepository paymentStatusTestRepository;
    @Autowired
    private InvoiceTestRepository invoiceTestRepository;

    @AfterEach
    void tearDown() {
        paymentStatusTestRepository.deleteAll();
        paymentStatusTestRepository.resetAutoIncrement();
    }

    @Test
    @DisplayName("Test get all with 2 payment status and expect status 200 and 2 payment status")
    void getAll() throws Exception {
        // GIVEN
        String json1 = "{\"statusName\":\"PAID\"}";
        String json2 = "{\"statusName\":\"PENDING\"}";
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
    @DisplayName("Test get all with no payment status and expect status 200 and no payment status")
    void getAllWithNoPaymentStatus() throws Exception {
        // WHEN
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Test get by id expect status 200 and payment status")
    void getById() throws Exception {
        // GIVEN
        String json = "{\"statusName\":\"PAID\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // WHEN
        String result = mockMvc.perform(get(BASE_URL + "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        PaymentStatusGetDto paymentStatusGetDto = objectMapper.readValue(result, PaymentStatusGetDto.class);
        // THEN
        assertEquals(1L, paymentStatusGetDto.id());
        assertEquals("PAID", paymentStatusGetDto.statusName());
    }

    @Test
    @DisplayName("Test get by id with invalid id expect status 404")
    void getByIdWithInvalidId() throws Exception {
        // WHEN
        String response = mockMvc.perform(get(BASE_URL + "1"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(STATUS_ID_NOT_FOUND + 1, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test get by name expect status 200 and payment status")
    void getByName() throws Exception {
        // GIVEN
        String json = "{\"statusName\":\"PAID\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // WHEN
        String response = mockMvc.perform(get(BASE_URL + "name/PAID"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        PaymentStatusGetDto paymentStatusGetDto = objectMapper.readValue(response, PaymentStatusGetDto.class);
        // THEN
        assertEquals(1L, paymentStatusGetDto.id());
        assertEquals("PAID", paymentStatusGetDto.statusName());
    }

    @Test
    @DisplayName("Test get by name with invalid name expect status 404")
    void getByNameWithInvalidName() throws Exception {
        // WHEN
        String response = mockMvc.perform(get(BASE_URL + "name/TESTE"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(STATUS_NAME_NOT_FOUND + "TESTE", agencyError.getMessage());
    }

    @Test
    @DisplayName("Test create payment status expect status 201 and payment status")
    void create() throws Exception {
        // GIVEN
        String json = "{\"statusName\":\"PAID\"}";
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        PaymentStatusGetDto paymentStatusGetDto = objectMapper.readValue(response, PaymentStatusGetDto.class);
        // THEN
        assertEquals(1L, paymentStatusGetDto.id());
        assertEquals("PAID", paymentStatusGetDto.statusName());
    }

    @Test
    @DisplayName("Test create payment status with invalid name expect status 400")
    void createWithInvalidName() throws Exception {
        // GIVEN
        String json = "{\"statusName\":\"\"}";
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertTrue(agencyError.getMessage().contains(INVALID_PAYMENT_STATUS));
    }

    @Test
    @DisplayName("Test create payment status with duplicate name expect status 400")
    void createWithDuplicateName() throws Exception {
        // GIVEN
        String json = "{\"statusName\":\"PAID\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // WHEN
        String response = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(PAYMENT_STATUS_DUPLICATE, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test create list of payment status expect status 201 and payment status")
    void createList() throws Exception {
        // GIVEN
        String json = "[{\"statusName\":\"PAID\"},{\"statusName\":\"PENDING\"}]";
        // WHEN
        String response = mockMvc.perform(post(BASE_URL + "List")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        PaymentStatusGetDto[] paymentStatusGetDtos = objectMapper.readValue(response, PaymentStatusGetDto[].class);
        // THEN
        assertEquals(1L, paymentStatusGetDtos[0].id());
        assertEquals("PAID", paymentStatusGetDtos[0].statusName());
        assertEquals(2L, paymentStatusGetDtos[1].id());
        assertEquals("PENDING", paymentStatusGetDtos[1].statusName());
    }

    @Test
    @DisplayName("Test create list of payment status with duplicate name expect status 400")
    void createListWithDuplicateName() throws Exception {
        // GIVEN
        String json = "[{\"statusName\":\"PAID\"},{\"statusName\":\"PAID\"}]";
        // WHEN
        String response = mockMvc.perform(post(BASE_URL + "List")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(PAYMENT_STATUS_DUPLICATE, agencyError.getMessage());
        assertEquals(1, paymentStatusTestRepository.count());
    }

    @Test
    @DisplayName("Test update payment status expect status 200 and payment status")
    void update() throws Exception {
        // GIVEN
        String json = "{\"statusName\":\"PAID\"}";
        String json2 = "{\"statusName\":\"PAID_UPDATED\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // WHEN
        String response = mockMvc.perform(put(BASE_URL + "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        PaymentStatusGetDto paymentStatusGetDto = objectMapper.readValue(response, PaymentStatusGetDto.class);
        // THEN
        assertEquals(1L, paymentStatusGetDto.id());
        assertEquals("PAID_UPDATED", paymentStatusGetDto.statusName());
    }

    @Test
    @DisplayName("Test update payment status with duplicate name expect status 400")
    void updateWithDuplicateName() throws Exception {
        // GIVEN
        String json = "{\"statusName\":\"PAID\"}";
        String json2 = "{\"statusName\":\"PAID_UPDATED\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2));
        // WHEN
        String response = mockMvc.perform(put(BASE_URL + "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json2))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(PAYMENT_STATUS_DUPLICATE, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test delete payment status expect status 204")
    void deleteStatus() throws Exception {
        // GIVEN
        String json = "{\"statusName\":\"PAID\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
        // WHEN
        mockMvc.perform(delete(BASE_URL + "1"))
                .andExpect(status().isOk());

        assertEquals(0, paymentStatusTestRepository.count());
    }

    @Test
    @DisplayName("Test delete payment status with invalid id expect status 404")
    void deleteStatusWithInvalidId() throws Exception {
        // WHEN
        String response = mockMvc.perform(delete(BASE_URL + "1"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(STATUS_ID_NOT_FOUND + 1, agencyError.getMessage());
    }

    @Test
    @DisplayName("Test delete payment status in use expect status 400")
    void deleteStatusInUse() throws Exception {
        // GIVEN
        String statusJson = "{\"statusName\":\"NOT_REQUESTED\"}";
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(statusJson));
        String userJson = "{\"email\": \"teste@example.com\",\"password\": \"zxlmn!!23K?\",\"userName\": \"userTeste\",\"dateOfBirth\": \"2000-01-01\",\"phoneNumber\": \"937313732\",\"vat\": \"123456782\"}";
        mockMvc.perform(post("/api/v1/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));
        String invoiceJson = "{\"userId\":1}";
        mockMvc.perform(post("/api/v1/invoices/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invoiceJson));
        // WHEN
        String response = mockMvc.perform(delete(BASE_URL + "1"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        AgencyError agencyError = objectMapper.readValue(response, AgencyError.class);
        // THEN
        assertEquals(PAYMENT_STATUS_IN_USE, agencyError.getMessage());
        assertEquals(1, paymentStatusTestRepository.count());
        invoiceTestRepository.deleteAll();
    }

}