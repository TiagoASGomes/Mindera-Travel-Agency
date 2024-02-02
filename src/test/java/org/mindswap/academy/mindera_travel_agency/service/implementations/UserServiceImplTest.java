package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindswap.academy.mindera_travel_agency.converter.UserConverter;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.User.EmailNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.User;
import org.mindswap.academy.mindera_travel_agency.repository.UserRepository;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @MockBean
    private UserRepository userRepositoryMock;
    private UserConverter userConverterMock;

    private UserServiceImpl userService;
    static MockedStatic<UserConverter> mockedUserConverter = Mockito.mockStatic(UserConverter.class);

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
       User service  = new UserServiceImpl(userRepositoryMock, userConverterMock);
    }

    @Test
    void testCreateUserCallsRepositoryAndConverter() throws EmailNotFoundException {
        UserCreateDto userCreateDto = new UserCreateDto("joe@coldmail.com", "1423Xyz!", "Joe", "01-01-1990", "912345678");
        User user = new User("joe@coldmail.com", "1423Xyz!", "Joe", "01-01-1990", "912345678");
        when(userRepositoryMock.findUserName(userCreateDto.email())).thenReturn(Optional.empty());

        when(userRepositoryMock.save(Mockito.any())).thenReturn(user);
        mockedUserConverter.when(() -> UserConverter.fromUserCreateDtoToModel(userCreateDto)).thenReturn(user);
        when(userRepositoryMock.save(Mockito.any())).thenReturn(user);


        userService.add(userCreateDto);


        Mockito.verify(userRepositoryMock, times(1)).findByUserName(userCreateDto.email());
        Mockito.verify(userRepositoryMock, times(1)).save(user);
        verifyNoMoreInteractions(userRepositoryMock);
        mockedUserConverter.verify(() -> UserConverter.fromUserCreateDtoToModel(userCreateDto));
        mockedUserConverter.verifyNoMoreInteractions();
        assertEquals(user, userService.add(userCreateDto));
    }



    @Test
    void createUserSuccessfully() throws EmailNotFoundException {

        UserCreateDto userCreateDto = new UserCreateDto("joe@coldmail.com", "1423Xyz!", "Joe", "01-01-1990", "912345678");
        User user = new User(); // Assuming User is the entity class


        when(userConverterMock.fromUserCreateDtoToModel(userCreateDto)).thenReturn(user);
        when(userRepositoryMock.findByEmail(userCreateDto.email())).thenReturn(Optional.empty());
        when(userRepositoryMock.save(user)).thenReturn(user);


        UserGetDto savedUser = userService.add(userCreateDto);


        assertNotNull(savedUser);
        assertEquals(user, savedUser);

        verify(userConverterMock, times(1)). fromUserCreateDtoToModel(userCreateDto);
        verify(userRepositoryMock, times(1)).findByEmail(userCreateDto.email());
        verify(userRepositoryMock, times(1)).save(user);
    }


    @Test
    void createUserWithDuplicatedEmailThrowsException() {

        UserCreateDto userCreateDto = new UserCreateDto("joe@coldmail.com", "1423Xyz!", "Joe", "01-01-1990", "912345678");


        when(userRepositoryMock.findByEmail(userCreateDto.email())).thenReturn(Optional.of(new User()));


        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> userService.add(userCreateDto));
        assertEquals("Email already taken", exception.getMessage());

        verify(userConverterMock, never()).fromUserCreateDtoToModel(any(UserCreateDto.class));
        verify(userRepositoryMock, times(1)).findByEmail(userCreateDto.email());
        verify(userRepositoryMock, never()).save(any(User.class));
    }

}




