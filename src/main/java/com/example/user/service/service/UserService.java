package com.example.user.service.service;

import com.example.user.service.dal.entity.UserEntity;
import com.example.user.service.dal.repository.UserRepository;
import com.example.user.service.dto.CreateUserDto;
import com.example.user.service.dto.UserShortInfoDto;
import com.example.user.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Transactional
    public UUID create(CreateUserDto dto) {
        UserEntity entity = mapper.map(dto);
        entity.setPassword(encoder.encode(dto.getPassword()));
        return repository.save(entity).getId();
    }

    @Transactional(readOnly = true)
    public UserShortInfoDto read(UUID id) {
        return repository.findById(id).map(mapper::map).orElseThrow(() -> new RuntimeException());
    }

    @Transactional(readOnly = true)
    public UserShortInfoDto read(String email) {
        return null;
    }

    @Transactional(readOnly = true)
    public Set<UserShortInfoDto> read() {//TODO: рассказать про пагинацию
        return null;
    }

}
