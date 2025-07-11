package com.example.user.service.controller;

import com.example.user.service.dto.AuthRequestDto;
import com.example.user.service.dto.AuthResponseDto;
import com.example.user.service.dto.TokenRefreshRequestDto;
import com.example.user.service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody AuthRequestDto request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public AuthResponseDto refresh(@RequestBody TokenRefreshRequestDto request) {
        return authService.refresh(request);
    }

    @PostMapping("/logout")
    public void logout(@RequestBody TokenRefreshRequestDto request) {
        authService.logout(request);
    }

}
