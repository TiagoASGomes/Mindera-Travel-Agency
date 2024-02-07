package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class UserServiceImplTest {
//
//    @MockBean
//    private UserRepository userRepositoryMock;
//    @MockBean
//    private UserConverter userConverterMock;
//    @MockBean
//    private InvoiceConverter invoiceConverterMock;
//    @MockBean
//    private HotelReservationConverter hotelReservationConverterMock;
//    @MockBean
//    private FlightTicketConverter flightTicketConverterMock;
//    @MockBean
//    private ExternalService externalServiceMock;
//    private UserService userService;
//
//    @BeforeEach
//    void setUp() {
//        userService = new UserServiceImpl(userRepositoryMock, userConverterMock, invoiceConverterMock, flightTicketConverterMock, hotelReservationConverterMock, externalServiceMock);
//    }
//
//    @Test
//    void getAllUsersSuccessfully() {
//        User user = User.builder().email("joe@coldmail.com").password("1423Xyz!").userName("Joe").dateOfBirth(LocalDate.of(1990, 1, 1)).phoneNumber("912345678").id(1L).build();
//        UserGetDto userGetDto = new UserGetDto(1L, "Joe", "joe@coldmail.com", LocalDate.of(1990, 1, 1), "912345678");
//
//        when(userRepositoryMock.findAll()).thenReturn(Collections.singletonList(user));
//        when(userConverterMock.fromUserModelToGetDto(user)).thenReturn(userGetDto);
//
//        List<UserGetDto> users = userService.getAll();
//
//        assertNotNull(users);
//        assertEquals(1, users.size());
//        assertEquals(userGetDto, users.get(0));
//
//        verify(userRepositoryMock, times(1)).findAll();
//        verify(userConverterMock, times(1)).fromUserModelToGetDto(user);
//    }
//
//    @Test
//    void updateUserSuccessfully() throws Exception {
//        UserCreateDto userCreateDto = new UserCreateDto("joe@coldmail.com", "1423Xyz!", "Joe", LocalDate.of(1990, 1, 1), "912345678");
//        User user = User.builder().email("joe@coldmail.com").password("1423Xyz!").userName("Joe").dateOfBirth(LocalDate.of(1990, 1, 1)).phoneNumber("912345678").id(1L).build();
//        UserGetDto userGetDto = new UserGetDto(1L, "Joe", "joe@coldmail.com", LocalDate.of(1990, 1, 1), "912345678");
//
//        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(user));
//        when(userRepositoryMock.findByEmail(userCreateDto.email())).thenReturn(Optional.empty());
//        when(userConverterMock.fromUserModelToGetDto(user)).thenReturn(userGetDto);
//        when(userRepositoryMock.save(user)).thenReturn(user);
//
//        UserGetDto updatedUser = userService.update(1L, userCreateDto);
//
//        assertNotNull(updatedUser);
//        assertEquals(userGetDto, updatedUser);
//
//        verify(userRepositoryMock, times(1)).findById(1L);
//        verify(userRepositoryMock, times(1)).findByEmail(userCreateDto.email());
//        verify(userRepositoryMock, times(1)).save(user);
//        verify(userConverterMock, times(1)).fromUserModelToGetDto(user);
//    }
//
//    @Test
//    void updateUserWithNonExistingIdThrowsException() {
//        UserCreateDto userCreateDto = new UserCreateDto("joe@coldmail.com", "1423Xyz!", "Joe", LocalDate.of(1990, 1, 1), "912345678");
//
//        when(userRepositoryMock.findById(1L)).thenReturn(Optional.empty());
//
//        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.update(1L, userCreateDto));
//        assertEquals(USER_NOT_FOUND, exception.getMessage());
//
//        verify(userRepositoryMock, times(1)).findById(1L);
//        verify(userRepositoryMock, never()).findByEmail(anyString());
//        verify(userRepositoryMock, never()).save(any(User.class));
//        verify(userConverterMock, never()).fromUserModelToGetDto(any(User.class));
//    }
//
//    @Test
//    void addNewUserSuccessfully() throws DuplicateEmailException {
//        UserCreateDto userCreateDto = new UserCreateDto("joe@coldmail.com", "1423Xyz!", "Joe", LocalDate.of(1990, 1, 1), "912345678");
//        User user = User.builder().email("joe@coldmail.com").password("1423Xyz!").userName("Joe").dateOfBirth(LocalDate.of(1990, 1, 1)).phoneNumber("912345678").id(1L).build();
//        UserGetDto userGetDto = new UserGetDto(1L, "Joe", "joe@coldmail.com", LocalDate.of(1990, 1, 1), "912345678");
//
//        when(userRepositoryMock.findByEmail(userCreateDto.email())).thenReturn(Optional.empty());
//        when(userConverterMock.fromUserCreateDtoToModel(userCreateDto)).thenReturn(user);
//        when(userRepositoryMock.save(user)).thenReturn(user);
//        when(userConverterMock.fromUserModelToGetDto(user)).thenReturn(userGetDto);
//
//        UserGetDto savedUser = userService.add(userCreateDto);
//
//        assertNotNull(savedUser);
//        assertEquals(userGetDto, savedUser);
//
//        verify(userRepositoryMock, times(1)).findByEmail(userCreateDto.email());
//        verify(userConverterMock, times(1)).fromUserCreateDtoToModel(userCreateDto);
//        verify(userRepositoryMock, times(1)).save(user);
//        verify(userConverterMock, times(1)).fromUserModelToGetDto(user);
//    }
//
//    @Test
//    void addNewUserWithExistingEmailThrowsException() {
//        UserCreateDto userCreateDto = new UserCreateDto("joe@coldmail.com", "1423Xyz!", "Joe", LocalDate.of(1990, 1, 1), "912345678");
//        User user = User.builder().email("joe@coldmail.com").password("1423Xyz!").userName("Joe").dateOfBirth(LocalDate.of(1990, 1, 1)).phoneNumber("912345678").id(1L).build();
//
//        when(userRepositoryMock.findByEmail(userCreateDto.email())).thenReturn(Optional.of(user));
//
//        EmailNotFoundException exception = assertThrows(EmailNotFoundException.class, () -> userService.add(userCreateDto));
//        assertEquals(EMAIL_NOT_FOUND, exception.getMessage());
//
//        verify(userRepositoryMock, times(1)).findByEmail(userCreateDto.email());
//        verify(userConverterMock, never()).fromUserCreateDtoToModel(any(UserCreateDto.class));
//        verify(userRepositoryMock, never()).save(any(User.class));
//        verify(userConverterMock, never()).fromUserModelToGetDto(any(User.class));
//    }
//
//    @Test
//    void updateUserWithExistingEmailThrowsException() {
//        UserCreateDto userCreateDto = new UserCreateDto("joe@coldmail.com", "1423Xyz!", "Joe", LocalDate.of(1990, 1, 1), "912345678");
//        User existingUser = User.builder().email("joe@coldmail.com").password("1423Xyz!").userName("Joe").dateOfBirth(LocalDate.of(1990, 1, 1)).phoneNumber("912345678").id(1L).build();
//        User anotherExistingUser = User.builder().email("jane@coldmail.com").password("1423Xyz!").userName("Jane").dateOfBirth(LocalDate.of(1990, 1, 1)).phoneNumber("912345678").id(2L).build();
//
//        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUser));
//        when(userRepositoryMock.findByEmail(userCreateDto.email())).thenReturn(Optional.of(anotherExistingUser));
//
//        EmailNotFoundException exception = assertThrows(EmailNotFoundException.class, () -> userService.update(1L, userCreateDto));
//        assertEquals(EMAIL_ALREADY_EXISTS, exception.getMessage());
//
//        verify(userRepositoryMock, times(1)).findById(1L);
//        verify(userRepositoryMock, times(1)).findByEmail(userCreateDto.email());
//        verify(userRepositoryMock, never()).save(any(User.class));
//        verify(userConverterMock, never()).fromUserModelToGetDto(any(User.class));
//    }
//
//    @Test
//    void updateUserWithInvalidEmailThrowsException() {
//        UserCreateDto userCreateDto = new UserCreateDto("invalidEmail", "1423Xyz!", "Joe", LocalDate.of(1990, 1, 1), "912345678");
//        User existingUser = User.builder().email("joe@coldmail.com").password("1423Xyz!").userName("Joe").dateOfBirth(LocalDate.of(1990, 1, 1)).phoneNumber("912345678").id(1L).build();
//
//        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUser));
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.update(1L, userCreateDto));
//        assertEquals(INVALID_EMAIL, exception.getMessage());
//
//        verify(userRepositoryMock, times(1)).findById(1L);
//    }
//
//    @Test
//    void deleteUserSuccessfully() throws UserNotFoundException {
//        User existingUser = User.builder().email("joe@coldmail.com").password("1423Xyz!").userName("Joe").dateOfBirth(LocalDate.of(1990, 1, 1)).phoneNumber("912345678").id(1L).build();
//
//        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUser));
//
//        userService.delete(1L);
//
//        verify(userRepositoryMock, times(1)).findById(1L);
//        verify(userRepositoryMock, times(1)).delete(existingUser);
//    }
//
//    @Test
//    void deleteUserWithNonExistingIdThrowsException() {
//        when(userRepositoryMock.findById(1L)).thenReturn(Optional.empty());
//
//        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.delete(1L));
//        assertEquals(USER_NOT_FOUND, exception.getMessage());
//
//        verify(userRepositoryMock, times(1)).findById(1L);
//        verify(userRepositoryMock, never()).delete(any(User.class));
//    }
//
//    @Test
//    void addNewUserWithInvalidPasswordThrowsException() {
//        UserCreateDto userCreateDto = new UserCreateDto("joe@coldmail.com", "invalidPassword", "Joe", LocalDate.of(1990, 1, 1), "912345678");
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.add(userCreateDto));
//        assertEquals(INVALID_PASSWORD, exception.getMessage());
//    }
//
//    @Test
//    void updateUserWithInvalidPasswordThrowsException() {
//        UserCreateDto userCreateDto = new UserCreateDto("joe@coldmail.com", "invalidPassword", "Joe", LocalDate.of(1990, 1, 1), "912345678");
//        User existingUser = User.builder().email("joe@coldmail.com").password("1423Xyz!").userName("Joe").dateOfBirth(LocalDate.of(1990, 1, 1)).phoneNumber("912345678").id(1L).build();
//
//        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUser));
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.update(1L, userCreateDto));
//        assertEquals(INVALID_PASSWORD, exception.getMessage());
//
//        verify(userRepositoryMock, times(1)).findById(1L);
//    }
//
//    @Test
//    void addNewUserWithFutureDateOfBirthThrowsException() {
//        UserCreateDto userCreateDto = new UserCreateDto("joe@coldmail.com", "1423Xyz!", "Joe", LocalDate.of(3000, 1, 1), "912345678");
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.add(userCreateDto));
//        assertEquals(INVALID_DATE_OF_BIRTH, exception.getMessage());
//    }
//
//    @Test
//    void updateUserWithFutureDateOfBirthThrowsException() {
//        UserCreateDto userCreateDto = new UserCreateDto("joe@coldmail.com", "1423Xyz!", "Joe", LocalDate.of(3000, 1, 1), "912345678");
//        User existingUser = User.builder().email("joe@coldmail.com").password("1423Xyz!").userName("Joe").dateOfBirth(LocalDate.of(1990, 1, 1)).phoneNumber("912345678").id(1L).build();
//
//        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUser));
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.update(1L, userCreateDto));
//        assertEquals(INVALID_DATE_OF_BIRTH, exception.getMessage());
//
//        verify(userRepositoryMock, times(1)).findById(1L);
//    }
//
//    @Test
//    void addNewUserWithEmptyUserNameThrowsException() {
//        UserCreateDto userCreateDto = new UserCreateDto("joe@coldmail.com", "1423Xyz!", "", LocalDate.of(1990, 1, 1), "912345678");
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.add(userCreateDto));
//        assertEquals(INVALID_USER_NAME, exception.getMessage());
//    }
//
//    @Test
//    void updateUserWithEmptyUserNameThrowsException() {
//        UserCreateDto userCreateDto = new UserCreateDto("joe@coldmail.com", "1423Xyz!", "", LocalDate.of(1990, 1, 1), "912345678");
//        User existingUser = User.builder().email("joe@coldmail.com").password("1423Xyz!").userName("Joe").dateOfBirth(LocalDate.of(1990, 1, 1)).phoneNumber("912345678").id(1L).build();
//
//        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(existingUser));
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> userService.update(1L, userCreateDto));
//        assertEquals(INVALID_USER_NAME, exception.getMessage());
//
//        verify(userRepositoryMock, times(1)).findById(1L);
//    }

}



