package org.mindswap.academy.mindera_travel_agency.controller;

import jakarta.validation.Valid;
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
import org.mindswap.academy.mindera_travel_agency.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserGetDto>> getAll() {
        //TODO paginar
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<UserGetDto>> getAllActive() {
        return ResponseEntity.ok(userService.getAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserGetDto> getById(@PathVariable Long id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserGetDto> getByEmail(@PathVariable String email) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getByEmail(email));
    }

    @GetMapping("/{id}/invoices")
    public ResponseEntity<List<InvoiceGetDto>> getAllInvoices(@PathVariable Long id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getAllInvoices(id));
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<HotelReservationGetDto>> getAllReservations(@PathVariable Long id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getAllReservations(id));
    }

    @GetMapping("/{id}/tickets")
    public ResponseEntity<List<TicketGetDto>> getAllTickets(@PathVariable Long id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getAllTickets(id));
    }

    //TODO chamar external apis adicionar filtros e sort
    @GetMapping("/hotels/{location}/{arrivalDate}/{leaveDate}")
    public ResponseEntity<List<ExternalHotelInfoDto>> getAvailableHotels(@PathVariable String location, @PathVariable String arrivalDate, @PathVariable String leaveDate) {
        return ResponseEntity.ok(userService.getAvailableHotels());
    }

    @GetMapping("/flights/{source}/{destination}/{arrivalDate}/")
    public ResponseEntity<List<ExternalHotelInfoDto>> getAvailableFlights(@PathVariable String source, @PathVariable String destination, @PathVariable String arrivalDate) {
        return ResponseEntity.ok(userService.getAvailableFlights());
    }

    @PostMapping("/")
    public ResponseEntity<UserGetDto> create(@Valid @RequestBody UserCreateDto user) throws DuplicateEmailException {
        return new ResponseEntity<>(userService.add(user), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserGetDto> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDto user) throws UserNotFoundException, DuplicateEmailException {
        return new ResponseEntity<>(userService.update(id, user), HttpStatus.OK);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<UserGetDto> updatePassword(@PathVariable Long id, @Valid @RequestBody UserUpdatePasswordDto user) throws UserNotFoundException, PasswordsDidNotMatchException {
        return new ResponseEntity<>(userService.updatePassword(id, user), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserGetDto> put(@PathVariable Long id, @Valid @RequestBody UserCreateDto user) throws UserNotFoundException, DuplicateEmailException {
        return new ResponseEntity<>(userService.put(id, user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws UserNotFoundException {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
