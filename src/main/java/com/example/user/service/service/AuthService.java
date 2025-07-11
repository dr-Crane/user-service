package com.example.user.service.service;

import com.example.user.service.dal.entity.RefreshTokenEntity;
import com.example.user.service.dal.repository.RefreshTokenRepository;
import com.example.user.service.dto.AuthRequestDto;
import com.example.user.service.dto.AuthResponseDto;
import com.example.user.service.dto.TokenRefreshRequestDto;
import com.example.user.service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    private final RefreshTokenRepository refreshRepo;

    @Value("${jwt.refresh-ttl}")
    private long refreshTokenTtl;

    @Transactional
    public AuthResponseDto login(AuthRequestDto request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        RefreshTokenEntity tokenEntity = new RefreshTokenEntity();
        tokenEntity.setEmail(userDetails.getUsername());
        tokenEntity.setToken(refreshToken);
        tokenEntity.setExpiryDate(new Date(System.currentTimeMillis() + refreshTokenTtl));
        refreshRepo.deleteByEmail(userDetails.getUsername());
        refreshRepo.save(tokenEntity);

        return new AuthResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public AuthResponseDto refresh(@RequestBody TokenRefreshRequestDto request) {
        String refreshToken = request.getRefreshToken();

        RefreshTokenEntity tokenEntity = refreshRepo.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Не корректный токен"));

        if (tokenEntity.getExpiryDate().before(new Date())) {
            refreshRepo.delete(tokenEntity);
            throw new RuntimeException("Токен истек");//TODO: выбросить исключение и вернуть 401
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(tokenEntity.getEmail());
        String newAccessToken = jwtUtil.generateAccessToken(userDetails);
        String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

        tokenEntity.setToken(newRefreshToken);
        tokenEntity.setExpiryDate(new Date(System.currentTimeMillis() + refreshTokenTtl));
        refreshRepo.save(tokenEntity);

        return new AuthResponseDto(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(@RequestBody TokenRefreshRequestDto request) {
        refreshRepo.findByToken(request.getRefreshToken()).ifPresent(refreshRepo::delete);
    }

}
