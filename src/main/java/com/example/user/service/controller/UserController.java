package com.example.user.service.controller;

import com.example.user.service.dto.CreateUserDto;
import com.example.user.service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public UUID create(@RequestBody CreateUserDto dto) {
        return service.create(dto);
    }

}
