package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
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
import org.mindswap.academy.mindera_travel_agency.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    /**
     * The UserService interface provides methods to manage user-related operations.
     */
    Page<UserGetDto> getAll(Pageable page);

    /**
     * Retrieves all users with pagination.
     *
     * @param page the pagination information
     * @return a page of UserGetDto objects
     */
    Page<UserGetDto> getAllActive(Pageable page);

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user
     * @return the UserGetDto object
     * @throws UserNotFoundException if the user is not found
     */
    UserGetDto getById(Long id) throws UserNotFoundException;

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user
     * @return the UserGetDto object
     * @throws UserNotFoundException if the user is not found
     */
    UserGetDto getByEmail(String email) throws UserNotFoundException;

    /**
     * Retrieves all invoices associated with a user.
     *
     * @param id the ID of the user
     * @return a list of InvoiceGetDto objects
     * @throws UserNotFoundException if the user is not found
     */
    List<InvoiceGetDto> getAllInvoices(Long id) throws UserNotFoundException;

    /**
     * Retrieves all hotel reservations associated with a user.
     *
     * @param id the ID of the user
     * @return a list of HotelReservationGetDto objects
     * @throws UserNotFoundException if the user is not found
     */
    List<HotelReservationGetDto> getAllReservations(Long id) throws UserNotFoundException;

    /**
     * Retrieves all flight tickets associated with a user.
     *
     * @param id the ID of the user
     * @return a list of TicketGetDto objects
     * @throws UserNotFoundException if the user is not found
     */
    List<TicketGetDto> getAllTickets(Long id) throws UserNotFoundException;

    /**
     * Retrieves available hotels based on location and arrival date.
     *
     * @param location the location of the hotel
     * @param page     the pagination information
     * @return a list of ExternalHotelInfoDto objects
     * @throws UnirestException        if an error occurs during the HTTP request
     * @throws JsonProcessingException if an error occurs while processing JSON
     */
    List<ExternalHotelInfoDto> getAvailableHotels(String location, Pageable page) throws UnirestException, JsonProcessingException;

    /**
     * Retrieves available flights based on source, destination, and arrival date.
     *
     * @param source      the source location
     * @param destination the destination location
     * @param date        the arrival date
     * @param page        the pagination information
     * @return a list of ExternalFlightInfoDto objects
     * @throws UnirestException        if an error occurs during the HTTP request
     * @throws JsonProcessingException if an error occurs while processing JSON
     */
    List<ExternalFlightInfoDto> getAvailableFlights(String source, String destination, String date, Pageable page, int price) throws UnirestException, JsonProcessingException;

    /**
     * Creates a new user.
     *
     * @param user the UserCreateDto object containing user information
     * @return the created UserGetDto object
     * @throws DuplicateEmailException if the email is already associated with another user
     */
    UserGetDto add(UserCreateDto user) throws DuplicateEmailException;

    /**
     * Updates an existing user.
     *
     * @param id   the ID of the user to update
     * @param user the UserCreateDto object containing updated user information
     * @return the updated UserGetDto object
     * @throws UserNotFoundException   if the user is not found
     * @throws DuplicateEmailException if the email is already associated with another user
     */
    UserGetDto put(Long id, UserCreateDto user) throws UserNotFoundException, DuplicateEmailException;

    /**
     * Updates an existing user.
     *
     * @param id   the ID of the user to update
     * @param user the UserUpdateDto object containing updated user information
     * @return the updated UserGetDto object
     * @throws UserNotFoundException   if the user is not found
     * @throws DuplicateEmailException if the email is already associated with another user
     */
    UserGetDto update(Long id, UserUpdateDto user) throws UserNotFoundException, DuplicateEmailException;

    /**
     * Updates the password of an existing user.
     *
     * @param id   the ID of the user to update
     * @param user the UserUpdatePasswordDto object containing updated password information
     * @return the updated UserGetDto object
     * @throws UserNotFoundException         if the user is not found
     * @throws PasswordsDidNotMatchException if the new password and confirmation password do not match
     */
    UserGetDto updatePassword(Long id, UserUpdatePasswordDto user) throws UserNotFoundException, PasswordsDidNotMatchException;

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     * @throws UserNotFoundException if the user is not found
     */
    void delete(Long id) throws UserNotFoundException;

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user
     * @return the User object
     * @throws UserNotFoundException if the user is not found
     */
    User findById(Long id) throws UserNotFoundException;
}
