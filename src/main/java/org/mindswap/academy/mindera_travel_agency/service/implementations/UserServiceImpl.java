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

    /**
     * Implementation of the UserService interface.
     * This class provides methods to interact with the user repository and perform user-related operations.
     * It is responsible for managing user data, converting data between different formats, and interacting with external services.
     */

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter, InvoiceConverter invoiceConverter, FlightTicketConverter flightTicketConverter, HotelReservationConverter hotelReservationConverter, ExternalService externalService) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.invoiceConverter = invoiceConverter;
        this.flightTicketConverter = flightTicketConverter;
        this.hotelReservationConverter = hotelReservationConverter;
        this.externalService = externalService;
    }

    /**
     * Retrieves all users with pagination.
     *
     * @param page the pagination information
     * @return a page of UserGetDto objects
     */
    @Override
    public Page<UserGetDto> getAll(Pageable page) {
        Page<User> users = userRepository.findAll(page);
        return users.map(userConverter::fromUserModelToGetDto);
    }

    /**
     * Retrieves all active users.
     *
     * @param page the pageable object for pagination
     * @return a page of UserGetDto objects representing the active users
     */
    @Override
    public Page<UserGetDto> getAllActive(Pageable page) {
        Page<User> users = userRepository.findAllActive(page);
        return users.map(userConverter::fromUserModelToGetDto);
    }

    /**
     * Retrieves a UserGetDto object by its ID.
     *
     * @param id the ID of the user
     * @return the UserGetDto object representing the user
     * @throws UserNotFoundException if the user with the given ID is not found
     */
    @Override
    public UserGetDto getById(Long id) throws UserNotFoundException {
        return userConverter.fromUserModelToGetDto(findById(id));
    }

    /**
     * Retrieves a user by email and returns the corresponding UserGetDto.
     *
     * @param email the email of the user to retrieve
     * @return the UserGetDto object representing the retrieved user
     * @throws UserNotFoundException if the user with the specified email is not found
     */
    @Override
    public UserGetDto getByEmail(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(EMAIL_NOT_FOUND + email));
        return userConverter.fromUserModelToGetDto(user);
    }

    /**
     * Retrieves all invoices for a given user.
     *
     * @param id the ID of the user
     * @return a list of InvoiceGetDto objects representing the invoices
     * @throws UserNotFoundException if the user with the given ID is not found
     */
    @Override
    public List<InvoiceGetDto> getAllInvoices(Long id) throws UserNotFoundException {
        User user = findById(id);
        return invoiceConverter.fromEntityListToGetDtoList(user.getInvoices());
    }

    /**
     * Retrieves all hotel reservations for a given user.
     *
     * @param id the ID of the user
     * @return a list of HotelReservationGetDto objects representing the user's reservations
     * @throws UserNotFoundException if the user with the given ID is not found
     */
    @Override
    public List<HotelReservationGetDto> getAllReservations(Long id) throws UserNotFoundException {
        User user = findById(id);
        List<HotelReservation> userReservations = user.getInvoices().stream()
                .map(Invoice::getHotelReservation)
                .filter(Objects::nonNull)
                .toList();
        return hotelReservationConverter.fromEntityListToGetDtoList(userReservations);
    }

    /**
     * Retrieves all tickets associated with a user.
     *
     * @param id the ID of the user
     * @return a list of TicketGetDto objects representing the user's tickets
     * @throws UserNotFoundException if the user with the given ID is not found
     */
    @Override
    public List<TicketGetDto> getAllTickets(Long id) throws UserNotFoundException {
        User user = findById(id);
        List<FlightTicket> userTickets = new ArrayList<>();
        user.getInvoices()
                .forEach(invoice -> userTickets.addAll(invoice.getFlightTickets()));
        return flightTicketConverter.fromEntityListToGetDtoList(userTickets);
    }

    /**
     * Retrieves a list of available hotels based on the specified location, arrival date, and page information.
     *
     * @param location the location of the hotels
     * @param page     the page information for pagination
     * @return a list of ExternalHotelInfoDto objects representing the available hotels
     * @throws UnirestException        if an error occurs during the HTTP request
     * @throws JsonProcessingException if an error occurs while processing the JSON response
     */
    @Override
    public List<ExternalHotelInfoDto> getAvailableHotels(String location, Pageable page) throws UnirestException, JsonProcessingException {
        return externalService.getAvailableHotels(location, page.getPageNumber());
    }

    /**
     * Retrieves a list of available flights based on the given source, destination, arrival date, and page information.
     *
     * @param source      The source location of the flights.
     * @param destination The destination location of the flights.
     * @param date        The arrival date of the flights.
     * @param page        The page information for pagination.
     * @return A list of ExternalFlightInfoDto objects representing the available flights.
     * @throws UnirestException        If an error occurs while making the API request.
     * @throws JsonProcessingException If an error occurs while processing the JSON response.
     */
    @Override
    public List<ExternalFlightInfoDto> getAvailableFlights(String source, String destination, String date, Pageable page, int price) throws UnirestException, JsonProcessingException {
        return externalService.getFlights(source, destination, date, page.getPageNumber(), price);
    }

    /**
     * Adds a new user to the system.
     *
     * @param user the user to be added
     * @return the created user as a UserGetDto object
     * @throws DuplicateEmailException if the email of the user already exists in the system
     */
    @Override
    public UserGetDto add(UserCreateDto user) throws DuplicateEmailException {
        checkDuplicateEmail(0L, user.email());
        User newUser = userConverter.fromUserCreateDtoToModel(user);
        return userConverter.fromUserModelToGetDto(userRepository.save(newUser));
    }

    /**
     * Updates an existing user with the provided ID and returns the updated user information.
     *
     * @param id   The ID of the user to be updated.
     * @param user The updated user information.
     * @return The updated user information in the form of a UserGetDto object.
     * @throws UserNotFoundException   If the user with the provided ID does not exist.
     * @throws DuplicateEmailException If the updated user's email is already associated with another user.
     */
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
        if (userDto.vat() != null) {
            dbUser.setVat(userDto.vat());
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

    /**
     * Deletes a user with the specified ID.
     * Marks the user as deleted by setting the 'deleted' flag to true.
     * Saves the updated user in the repository.
     *
     * @param id the ID of the user to be deleted
     * @throws UserNotFoundException if the user with the specified ID is not found
     */
    @Override
    public void delete(Long id) throws UserNotFoundException {
        User user = findById(id);
        user.setDeleted(true);
        userRepository.save(user);
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to find
     * @return the user with the specified ID
     * @throws UserNotFoundException if no user is found with the given ID
     */
    @Override
    public User findById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(USER_ID_NOT_FOUND + id));
    }

    /**
     * Checks if the given email is already associated with another user in the system.
     * Throws a DuplicateEmailException if a duplicate email is found, except when the email belongs to the user with the given id.
     *
     * @param id    the id of the user to exclude from the duplicate email check
     * @param email the email to check for duplicates
     * @throws DuplicateEmailException if a duplicate email is found
     */
    private void checkDuplicateEmail(Long id, String email) throws DuplicateEmailException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent() && !userOptional.get().getId().equals(id)) {
            throw new DuplicateEmailException(DUPLICATE_EMAIL);
        }
    }
}
