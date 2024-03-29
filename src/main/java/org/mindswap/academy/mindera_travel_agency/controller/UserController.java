package org.mindswap.academy.mindera_travel_agency.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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
import org.mindswap.academy.mindera_travel_agency.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * Retrieves all users with pagination support.
     *
     * @param page the page information
     * @return the list of users
     */
    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "Return all users")
    @GetMapping("/")
    public ResponseEntity<Page<UserGetDto>> getAll(Pageable page) {
        return ResponseEntity.ok(userService.getAll(page));
    }

    /**
     * Retrieves all active users with pagination support.
     *
     * @param page the page information
     * @return the list of active users
     */
    @Operation(summary = "Get all active users")
    @ApiResponse(responseCode = "200", description = "Return all active users")
    @GetMapping("/active")
    public ResponseEntity<Page<UserGetDto>> getAllActive(Pageable page) {
        return ResponseEntity.ok(userService.getAllActive(page));
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the user ID
     * @return the user information
     * @throws UserNotFoundException if the user is not found
     */
    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return user by ID"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserGetDto> getById(@PathVariable Long id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getById(id));
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the user email
     * @return the user information
     * @throws UserNotFoundException if the user is not found
     */
    @Operation(summary = "Get user by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return user by email"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<UserGetDto> getByEmail(@PathVariable String email) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getByEmail(email));
    }

    /**
     * Retrieves all invoices for a user.
     *
     * @param id the user ID
     * @return the list of invoices
     * @throws UserNotFoundException if the user is not found
     */
    @Operation(summary = "Get all invoices by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return all invoices by user ID"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}/invoices")
    public ResponseEntity<List<InvoiceGetDto>> getAllInvoices(@PathVariable Long id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getAllInvoices(id));
    }

    /**
     * Retrieves all hotel reservations of a user.
     *
     * @param id the user ID
     * @return the list of hotel reservations
     * @throws UserNotFoundException if the user is not found
     */
    @Operation(summary = "Get all hotel reservations by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return all hotel reservations by user ID"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<HotelReservationGetDto>> getAllReservations(@PathVariable Long id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getAllReservations(id));
    }

    /**
     * Retrieves all flight tickets of a user.
     *
     * @param id the user ID
     * @return the list of flight tickets
     * @throws UserNotFoundException if the user is not found
     */
    @Operation(summary = "Get all flight tickets by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return all flight tickets by user ID"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}/tickets")
    public ResponseEntity<List<TicketGetDto>> getAllTickets(@PathVariable Long id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getAllTickets(id));
    }

    /**
     * Retrieves available hotels based on location and arrival date.
     *
     * @param location the hotel location
     * @param page     the page information
     * @return the list of available hotels
     * @throws UnirestException        if an error occurs during the HTTP request
     * @throws JsonProcessingException if an error occurs while processing JSON
     */
    @Operation(summary = "Get available hotels by location and arrival date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return available hotels by location and arrival date"),
            @ApiResponse(responseCode = "400", description = "Error while processing JSON, or an error occurs during the HTTP request")
    })
    @GetMapping("/hotels/{location}")
    public ResponseEntity<List<ExternalHotelInfoDto>> getAvailableHotels(@PathVariable String location, Pageable page) throws UnirestException, JsonProcessingException {
        return ResponseEntity.ok(userService.getAvailableHotels(location, page));
    }

    /**
     * Retrieves available flights based on source, destination, and arrival date.
     *
     * @param source      the flight source
     * @param destination the flight destination
     * @param date        the arrival date
     * @param page        the page information
     * @return the list of available flights
     * @throws UnirestException        if an error occurs during the HTTP request
     * @throws JsonProcessingException if an error occurs while processing JSON
     */
    @Operation(summary = "Get available flights by source, destination, and arrival date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return available flights by source, destination, and arrival date"),
            @ApiResponse(responseCode = "400", description = "Error while processing JSON, or an error occurs during the HTTP request")
    })
    @GetMapping("/flights/{source}/{destination}")
    public ResponseEntity<List<ExternalFlightInfoDto>> getAvailableFlights(@PathVariable String source, @PathVariable String destination, @RequestParam(defaultValue = "") String date, @RequestParam(defaultValue = "9999") int price, Pageable page) throws UnirestException, JsonProcessingException {
        return ResponseEntity.ok(userService.getAvailableFlights(source, destination, date, page, price));
    }

    /**
     * Creates a new user.
     *
     * @param user the user information
     * @return the created user information
     * @throws DuplicateEmailException if the email is already associated with another user
     */
    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Return the created user information"),
            @ApiResponse(responseCode = "400", description = "Email is already associated with another user")
    })
    @PostMapping("/")
    public ResponseEntity<UserGetDto> create(@Valid @RequestBody UserCreateDto user) throws DuplicateEmailException {
        return new ResponseEntity<>(userService.add(user), HttpStatus.CREATED);
    }

    /**
     * Updates an existing user.
     *
     * @param id   the user ID
     * @param user the updated user information
     * @return the updated user information
     * @throws UserNotFoundException   if the user is not found
     * @throws DuplicateEmailException if the email is already associated with another user
     */
    @Operation(summary = "Update an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the updated user information"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Email is already associated with another user")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<UserGetDto> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDto user) throws UserNotFoundException, DuplicateEmailException {
        return new ResponseEntity<>(userService.update(id, user), HttpStatus.OK);
    }

    /**
     * Updates the password of a user.
     *
     * @param id   the user ID
     * @param user the updated password information
     * @return the updated user information
     * @throws UserNotFoundException         if the user is not found
     * @throws PasswordsDidNotMatchException if the new password and confirmation password do not match
     */
    @Operation(summary = "Update the password of a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the updated user information"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "New password and confirmation password do not match")
    })
    @PatchMapping("/{id}/password")
    public ResponseEntity<UserGetDto> updatePassword(@PathVariable Long id, @Valid @RequestBody UserUpdatePasswordDto user) throws UserNotFoundException, PasswordsDidNotMatchException {
        return new ResponseEntity<>(userService.updatePassword(id, user), HttpStatus.OK);
    }

    /**
     * Replaces an existing user with a new user.
     *
     * @param id   the user ID
     * @param user the new user information
     * @return the replaced user information
     * @throws UserNotFoundException   if the user is not found
     * @throws DuplicateEmailException if the email is already associated with another user
     */
    @Operation(summary = "Replace an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return the replaced user information"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Email is already associated with another user")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserGetDto> put(@PathVariable Long id, @Valid @RequestBody UserCreateDto user) throws UserNotFoundException, DuplicateEmailException {
        return new ResponseEntity<>(userService.put(id, user), HttpStatus.OK);
    }

    /**
     * Deletes a user.
     *
     * @param id the user ID
     * @return the response entity
     * @throws UserNotFoundException if the user is not found
     */
    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws UserNotFoundException {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}
