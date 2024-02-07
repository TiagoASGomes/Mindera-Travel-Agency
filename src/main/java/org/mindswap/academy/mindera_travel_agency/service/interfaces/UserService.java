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
import org.mindswap.academy.mindera_travel_agency.exception.User.DuplicateEmailException;
import org.mindswap.academy.mindera_travel_agency.exception.User.PasswordsDidNotMatchException;
import org.mindswap.academy.mindera_travel_agency.exception.User.UserNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    Page<UserGetDto> getAll(Pageable page);

    Page<UserGetDto> getAllActive(Pageable page);

    UserGetDto getById(Long id) throws UserNotFoundException;

    UserGetDto getByEmail(String email) throws UserNotFoundException;

    List<InvoiceGetDto> getAllInvoices(Long id) throws UserNotFoundException;

    List<HotelReservationGetDto> getAllReservations(Long id) throws UserNotFoundException;

    List<TicketGetDto> getAllTickets(Long id) throws UserNotFoundException;

    List<ExternalHotelInfoDto> getAvailableHotels(String location, String arrivalDate, Pageable page) throws UnirestException, JsonProcessingException;

    List<ExternalFlightInfoDto> getAvailableFlights(String source, String destination, String arrivalDate, Pageable page) throws UnirestException, JsonProcessingException;

    UserGetDto add(UserCreateDto user) throws DuplicateEmailException;

    UserGetDto put(Long id, UserCreateDto user) throws UserNotFoundException, DuplicateEmailException;

    UserGetDto update(Long id, UserUpdateDto user) throws UserNotFoundException, DuplicateEmailException;

    UserGetDto updatePassword(Long id, UserUpdatePasswordDto user) throws UserNotFoundException, PasswordsDidNotMatchException;

    void delete(Long id) throws UserNotFoundException;

    User findById(Long id) throws UserNotFoundException;
}
