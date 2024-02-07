package org.mindswap.academy.mindera_travel_agency.service.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.mindswap.academy.mindera_travel_agency.converter.FlightTicketConverter;
import org.mindswap.academy.mindera_travel_agency.converter.HotelReservationConverter;
import org.mindswap.academy.mindera_travel_agency.converter.InvoiceConverter;
import org.mindswap.academy.mindera_travel_agency.converter.UserConverter;
import org.mindswap.academy.mindera_travel_agency.dto.external.flight.ExternalFlightInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.external.hotel.ExternalHotelInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserUpdateDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserUpdatePasswordDto;
import org.mindswap.academy.mindera_travel_agency.exception.user.DuplicateEmailException;
import org.mindswap.academy.mindera_travel_agency.exception.user.PasswordsDidNotMatchException;
import org.mindswap.academy.mindera_travel_agency.exception.user.UserNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.FlightTicket;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.mindswap.academy.mindera_travel_agency.model.User;
import org.mindswap.academy.mindera_travel_agency.repository.UserRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.ExternalService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mindswap.academy.mindera_travel_agency.util.Messages.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final InvoiceConverter invoiceConverter;
    private final FlightTicketConverter flightTicketConverter;
    private final HotelReservationConverter hotelReservationConverter;
    private final ExternalService externalService;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter, InvoiceConverter invoiceConverter, FlightTicketConverter flightTicketConverter, HotelReservationConverter hotelReservationConverter, ExternalService externalService) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.invoiceConverter = invoiceConverter;
        this.flightTicketConverter = flightTicketConverter;
        this.hotelReservationConverter = hotelReservationConverter;
        this.externalService = externalService;
    }


    @Override
    public Page<UserGetDto> getAll(Pageable page) {
        Page<User> users = userRepository.findAll(page);
        return users.map(userConverter::fromUserModelToGetDto);
    }

    @Override
    public Page<UserGetDto> getAllActive(Pageable page) {
        Page<User> users = userRepository.findAllActive(page);
        return users.map(userConverter::fromUserModelToGetDto);
    }

    @Override
    @Cacheable("userCache")
    public UserGetDto getById(Long id) throws UserNotFoundException {
        return userConverter.fromUserModelToGetDto(findById(id));
    }

    @Override
    @Cacheable("userCache")
    public UserGetDto getByEmail(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(EMAIL_NOT_FOUND + email));
        return userConverter.fromUserModelToGetDto(user);
    }

    @Override
    @Cacheable("reservationsCache")
    public List<InvoiceGetDto> getAllInvoices(Long id) throws UserNotFoundException {
        User user = findById(id);
        return invoiceConverter.fromEntityListToGetDtoList(user.getInvoices());
    }

    @Override
    @Cacheable("reservationsCache")
    public List<HotelReservationGetDto> getAllReservations(Long id) throws UserNotFoundException {
        User user = findById(id);
        List<HotelReservation> userReservations = user.getInvoices().stream()
                .map(Invoice::getHotelReservation)
                .filter(Objects::nonNull)
                .toList();
        return hotelReservationConverter.fromEntityListToGetDtoList(userReservations);
    }

    @Override
    @Cacheable("reservationsCache")
    public List<TicketGetDto> getAllTickets(Long id) throws UserNotFoundException {
        User user = findById(id);
        List<FlightTicket> userTickets = new ArrayList<>();
        user.getInvoices()
                .forEach(invoice -> userTickets.addAll(invoice.getFlightTickets()));
        return flightTicketConverter.fromEntityListToGetDtoList(userTickets);
    }

    @Override
    @Cacheable("reservationsCache")
    public List<ExternalHotelInfoDto> getAvailableHotels(String location, String arrivalDate, Pageable page) throws UnirestException, JsonProcessingException {
        return externalService.getAvailableHotels(location, arrivalDate, page.getPageNumber());
    }

    @Override
    @Cacheable("reservationsCache")
    public List<ExternalFlightInfoDto> getAvailableFlights(String source, String destination, String arrivalDate, Pageable page) throws UnirestException, JsonProcessingException {
        return externalService.getFlights(source, destination, arrivalDate, page.getPageNumber());
    }

    @Override
    public UserGetDto add(UserCreateDto user) throws DuplicateEmailException {
        checkDuplicateEmail(0L, user.email());
        User newUser = userConverter.fromUserCreateDtoToModel(user);
        return userConverter.fromUserModelToGetDto(userRepository.save(newUser));
    }


    @Override
    public UserGetDto put(Long id, UserCreateDto user) throws UserNotFoundException, DuplicateEmailException {
        findById(id);
        checkDuplicateEmail(id, user.email());
        User newUser = userConverter.fromUserCreateDtoToModel(user);
        newUser.setId(id);
        return userConverter.fromUserModelToGetDto(userRepository.save(newUser));
    }


    @Override
    public UserGetDto update(Long id, UserUpdateDto userDto) throws UserNotFoundException, DuplicateEmailException {
        User dbUser = findById(id);
        if (userDto.email() != null) {
            checkDuplicateEmail(id, userDto.email());
            dbUser.setEmail(userDto.email());
        }
        if (userDto.userName() != null) {
            dbUser.setUserName(userDto.userName());
        }
        if (userDto.phoneNumber() != null) {
            dbUser.setPhoneNumber(userDto.phoneNumber());
        }
        return userConverter.fromUserModelToGetDto(userRepository.save(dbUser));
    }

    @Override
    public UserGetDto updatePassword(Long id, UserUpdatePasswordDto user) throws UserNotFoundException, PasswordsDidNotMatchException {
        User dbUser = findById(id);
        if (!user.oldPassword().equals(dbUser.getPassword())) {
            throw new PasswordsDidNotMatchException(PASSWORDS_DID_NOT_MATCH);
        }
        dbUser.setPassword(user.newPassword());
        return userConverter.fromUserModelToGetDto(userRepository.save(dbUser));
    }

    @Override
    public void delete(Long id) throws UserNotFoundException {
        User user = findById(id);
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public User findById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_ID_NOT_FOUND + id));
    }

    private void checkDuplicateEmail(Long id, String email) throws DuplicateEmailException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent() && !userOptional.get().getId().equals(id)) {
            throw new DuplicateEmailException(DUPLICATE_EMAIL);
        }
    }
}
