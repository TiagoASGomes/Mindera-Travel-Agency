package org.mindswap.academy.mindera_travel_agency.service.implementations;

import org.mindswap.academy.mindera_travel_agency.converter.FlightTicketConverter;
import org.mindswap.academy.mindera_travel_agency.converter.HotelReservationConverter;
import org.mindswap.academy.mindera_travel_agency.converter.InvoiceConverter;
import org.mindswap.academy.mindera_travel_agency.converter.UserConverter;
import org.mindswap.academy.mindera_travel_agency.dto.external.ExternalHotelInfoDto;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.User.DuplicateEmailException;
import org.mindswap.academy.mindera_travel_agency.exception.User.UserNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.FlightTicket;
import org.mindswap.academy.mindera_travel_agency.model.HotelReservation;
import org.mindswap.academy.mindera_travel_agency.model.Invoice;
import org.mindswap.academy.mindera_travel_agency.model.User;
import org.mindswap.academy.mindera_travel_agency.repository.UserRepository;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.ExternalService;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UserGetDto add(UserCreateDto user) throws DuplicateEmailException {
        if (userRepository.findByEmail(user.email()).isPresent()) {
            throw new DuplicateEmailException(DUPLICATE_EMAIL);
        }
        User newUser = userConverter.fromUserCreateDtoToModel(user);
        return userConverter.fromUserModelToGetDto(userRepository.save(newUser));
    }

    @Override
    public List<UserGetDto> getAll() {
        return userConverter.fromUserModelListToGetDto(userRepository.findAll());
    }

//    @Override
//    public UserGetDto update(long id, UserCreateDto user) throws UserNotFoundException, DuplicateEmailException {
//        User newUser = findById(id);
//        if (userRepository.findByEmail(user.email()).isPresent() && !newUser.getEmail().equals(user.email())) {
//            throw new DuplicateEmailException(DUPLICATE_EMAIL);
//        }
//        newUser.setEmail(user.email());
//        newUser.setUserName(user.userName());
//        newUser.setDateOfBirth(user.dateOfBirth());
//        newUser.setPhoneNumber(user.phoneNumber());
//        return userConverter.fromUserModelToGetDto(userRepository.save(newUser));
//    }


    @Override
    public UserGetDto put(long id, UserCreateDto user) throws UserNotFoundException, DuplicateEmailException {
        findById(id);
        Optional<User> userOptional = userRepository.findByEmail(user.email());
        if (userOptional.isPresent() && userOptional.get().getId() != id) {
            throw new DuplicateEmailException(DUPLICATE_EMAIL);
        }
        User newUser = userConverter.fromUserCreateDtoToModel(user);
        newUser.setId(id);
        return userConverter.fromUserModelToGetDto(userRepository.save(newUser));
    }

    @Override
    public User findById(long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(ID_NOT_FOUND + id));
    }


    @Override
    public void delete(long id) throws UserNotFoundException {
        findById(id);
        userRepository.deleteById(id);
    }

    @Override
    public UserGetDto getById(long id) throws UserNotFoundException {
        return userConverter.fromUserModelToGetDto(findById(id));
    }

    @Override
    public List<InvoiceGetDto> getAllInvoices(Long id) throws UserNotFoundException {
        User user = findById(id);
        return invoiceConverter.fromEntityListToGetDtoList(user.getInvoices());
    }

    @Override
    public List<HotelReservationGetDto> getAllReservations(Long id) throws UserNotFoundException {
        User user = findById(id);
        List<HotelReservation> userReservations = user.getInvoices().stream()
                .map(Invoice::getHotelReservation)
                .filter(Objects::nonNull)
                .toList();
        return hotelReservationConverter.fromEntityListToGetDtoList(userReservations);
    }

    @Override
    public List<TicketGetDto> getAllTickets(Long id) throws UserNotFoundException {
        User user = findById(id);
        List<FlightTicket> userTickets = new ArrayList<>();
        user.getInvoices()
                .forEach(invoice -> userTickets.addAll(invoice.getFlightTickets()));
        return flightTicketConverter.fromEntityListToGetDtoList(userTickets);
    }

    @Override
    public UserGetDto getByEmail(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(EMAIL_NOT_FOUND + email));
        return userConverter.fromUserModelToGetDto(user);
    }

    @Override
    public List<ExternalHotelInfoDto> getAvailableHotels() {
        return null;
    }

    @Override
    public List<ExternalHotelInfoDto> getAvailableFlights() {
        return null;
    }


}
