package org.mindswap.academy.mindera_travel_agency.service.interfaces;

import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.FlightTicketGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.User.EmailNotFoundException;
import org.mindswap.academy.mindera_travel_agency.exception.User.UserNotFoundException;
import org.mindswap.academy.mindera_travel_agency.model.User;

import java.util.List;

public interface UserService {

    UserGetDto add(UserCreateDto user) throws EmailNotFoundException;

    List<UserGetDto> getAll();

    UserGetDto update(long id, UserCreateDto user) throws UserNotFoundException, EmailNotFoundException;

    UserGetDto put(long id, UserCreateDto user) throws UserNotFoundException;

    User findById(long id) throws UserNotFoundException;

    void delete(long id) throws UserNotFoundException;

    UserGetDto getById(long id) throws UserNotFoundException;

    List<InvoiceGetDto> getAllInvoices(Long id) throws UserNotFoundException;

    List<HotelReservationGetDto> getAllReservations(Long id) throws UserNotFoundException;

    List<FlightTicketGetDto> getAllTickets(Long id) throws UserNotFoundException;
}
