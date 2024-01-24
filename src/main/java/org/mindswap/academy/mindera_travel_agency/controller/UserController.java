package org.mindswap.academy.mindera_travel_agency.controller;

import jakarta.validation.Valid;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserCreateDto;
import org.mindswap.academy.mindera_travel_agency.dto.user.UserGetDto;
import org.mindswap.academy.mindera_travel_agency.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserGetDto>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping("/")
    public ResponseEntity<UserGetDto> add(@Valid @RequestBody UserCreateDto user) {
        userService.add(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserGetDto> update(@PathVariable long id, @Valid @RequestBody UserCreateDto user) {
        userService.update(id, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserGetDto> put(@PathVariable long id, @Valid @RequestBody UserCreateDto user) {
        userService.put(id, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserGetDto> delete(@PathVariable long id, @Valid @RequestBody UserCreateDto user) {
        userService.delete(id, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
