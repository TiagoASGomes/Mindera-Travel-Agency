package org.mindswap.academy.mindera_travel_agency.controller;

import jakarta.validation.Valid;
import org.mindswap.academy.mindera_travel_agency.dto.flight_ticket.TicketGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.hotel.HotelReservationGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.invoice.InvoiceGetDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserGetDto;
import org.mindswap.academy.mindera_travel_agency.exception.User.DuplicateEmailException;
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

    @GetMapping("/{id}")
    public ResponseEntity<UserGetDto> getById(@PathVariable Long id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getById(id));
    }

    //TODO get by email

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

    @PostMapping("/")
    public ResponseEntity<UserGetDto> create(@Valid @RequestBody UserCreateDto user) throws DuplicateEmailException {
        return new ResponseEntity<>(userService.add(user), HttpStatus.CREATED);
    }

    //TODO mudar patch
//    @PatchMapping("/{id}")
//    public ResponseEntity<UserGetDto> update(@PathVariable Long id, @Valid @RequestBody UserCreateDto user) throws UserNotFoundException, DuplicateEmailException {
//        return new ResponseEntity<>(userService.update(id, user), HttpStatus.OK);
//    }

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
