package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindswap.academy.mindera_travel_agency.converter.FlightTicketConverter;
import org.mindswap.academy.mindera_travel_agency.converter.HotelReservationConverter;
import org.mindswap.academy.mindera_travel_agency.converter.InvoiceConverter;
import org.mindswap.academy.mindera_travel_agency.converter.UserConverter;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.User.DuplicateEmailException;
import org.mindswap.academy.mindera_travel_agency.model.User;
import org.mindswap.academy.mindera_travel_agency.repository.UserRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.ExternalService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.UserService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mindswap.academy.mindera_travel_agency.util.Messages.DUPLICATE_EMAIL;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class UserServiceImplTest {

    @MockBean
    private UserRepository userRepositoryMock;
    @MockBean
    private UserConverter userConverterMock;
    @MockBean
    private InvoiceConverter invoiceConverterMock;
    @MockBean
    private HotelReservationConverter hotelReservationConverterMock;
    @MockBean
    private FlightTicketConverter flightTicketConverterMock;
    @MockBean
    private ExternalService externalServiceMock;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepositoryMock, userConverterMock, invoiceConverterMock, flightTicketConverterMock, hotelReservationConverterMock, externalServiceMock);
    }

    @Test
    void createUserSuccessfully() throws Exception {

        UserCreateDto userCreateDto = new UserCreateDto("joe@coldmail.com", "1423Xyz!", "Joe", LocalDate.of(1990, 1, 1), "912345678");
        User user = User.builder().email("joe@coldmail.com").password("1423Xyz!").userName("Joe").dateOfBirth(LocalDate.of(1990, 1, 1)).phoneNumber("912345678").id(1L).build();
        UserGetDto userGetDto = new UserGetDto(1L, "Joe", "joe@coldmail.com", LocalDate.of(1990, 1, 1), "912345678");

        when(userConverterMock.fromUserCreateDtoToModel(userCreateDto)).thenReturn(user);
        when(userConverterMock.fromUserModelToGetDto(user)).thenReturn(userGetDto);
        when(userRepositoryMock.findByEmail(userCreateDto.email())).thenReturn(Optional.empty());
        when(userRepositoryMock.save(user)).thenReturn(user);

        UserGetDto savedUser = userService.add(userCreateDto);

        assertNotNull(savedUser);
        assertEquals(userGetDto, savedUser);

        verify(userConverterMock, times(1)).fromUserCreateDtoToModel(userCreateDto);
        verify(userConverterMock, times(1)).fromUserModelToGetDto(user);
        verify(userRepositoryMock, times(1)).findByEmail(userCreateDto.email());
        verify(userRepositoryMock, times(1)).save(user);
    }


    @Test
    void createUserWithDuplicatedEmailThrowsException() {

        UserCreateDto userCreateDto = new UserCreateDto("joe@coldmail.com", "1423Xyz!", "Joe", LocalDate.of(1990, 1, 1), "912345678");
        User user = User.builder().email("joe@coldmail.com").password("1423Xyz!").userName("Joe").dateOfBirth(LocalDate.of(1990, 1, 1)).phoneNumber("912345678").build();

        when(userRepositoryMock.findByEmail(userCreateDto.email())).thenReturn(Optional.of(new User()));


        DuplicateEmailException exception = assertThrows(DuplicateEmailException.class, () -> userService.add(userCreateDto));
        assertEquals(DUPLICATE_EMAIL, exception.getMessage());

        verify(userConverterMock, never()).fromUserCreateDtoToModel(any(UserCreateDto.class));
        verify(userRepositoryMock, times(1)).findByEmail(userCreateDto.email());
        verify(userRepositoryMock, never()).save(any(User.class));
    }

}




