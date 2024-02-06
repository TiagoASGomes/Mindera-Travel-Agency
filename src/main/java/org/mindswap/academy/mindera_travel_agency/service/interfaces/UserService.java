package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.mindswap.academy.mindera_travel_agency.dto.external.ExternalHotelInfoDto;
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

import java.util.List;

public interface UserService {

    UserGetDto add(UserCreateDto user) throws DuplicateEmailException;

    List<UserGetDto> getAll();

//    UserGetDto update(long id, UserCreateDto user) throws UserNotFoundException, DuplicateEmailException;

    UserGetDto put(long id, UserCreateDto user) throws UserNotFoundException, DuplicateEmailException;

    User findById(long id) throws UserNotFoundException;

    void delete(long id) throws UserNotFoundException;

    UserGetDto getById(long id) throws UserNotFoundException;

    List<InvoiceGetDto> getAllInvoices(Long id) throws UserNotFoundException;

    List<HotelReservationGetDto> getAllReservations(Long id) throws UserNotFoundException;

    List<TicketGetDto> getAllTickets(Long id) throws UserNotFoundException;

    UserGetDto getByEmail(String email) throws UserNotFoundException;

    String getAvailableHotels() throws UnirestException;

    List<ExternalHotelInfoDto> getAvailableFlights();

    List<UserGetDto> getAllActive();

    UserGetDto update(Long id, UserUpdateDto user) throws UserNotFoundException, DuplicateEmailException;

    UserGetDto updatePassword(Long id, UserUpdatePasswordDto user) throws UserNotFoundException, PasswordsDidNotMatchException;
}
