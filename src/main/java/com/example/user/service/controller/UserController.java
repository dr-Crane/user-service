package com.example.user.service.controller;

import com.example.user.service.dto.CreateUserDto;
import com.example.user.service.dto.UserShortInfoDto;
import com.example.user.service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID create(@Valid @RequestBody CreateUserDto dto) {
        return service.create(dto);
    }

    @GetMapping("/{id}")
    public UserShortInfoDto read(@PathVariable UUID id) {
        return service.read(id);
    }

    @GetMapping("/by-email")
    public UserShortInfoDto read(@RequestParam String email) {
        return service.read(email);
    }

    @GetMapping
    public Set<UserShortInfoDto> read() {
        return service.read();
    }

}
