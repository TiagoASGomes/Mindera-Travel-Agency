package org.mindswap.academy.mindera_travel_agency.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindswap.academy.mindera_travel_agency.converter.FlightTicketConverter;
import org.mindswap.academy.mindera_travel_agency.repository.FlightTicketRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.FareClassService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.InvoiceService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class FlightTicketServiceImplTest {

    @MockBean
    private FlightTicketRepository FTRepository;
    @MockBean
    private FlightTicketConverter FTConverter;
    @MockBean
    private FareClassService FCService;
    @MockBean
    private InvoiceService IService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAll() {
    }

    @Test
    void getAllByInvoice() {
    }

    @Test
    void getById() {
    }

    @Test
    void create() {
    }

    @Test
    void updateTotal() {
    }

    @Test
    void updatePersonalInfo() {
    }

    @Test
    void updateTicketInfo() {
    }

    @Test
    void delete() {
    }

    @Test
    void findById() {
    }
}