package com.example.user.service.service;

import com.example.user.service.dal.entity.UserEntity;
import com.example.user.service.dal.enums.Status;
import com.example.user.service.dal.repository.UserRepository;
import com.example.user.service.dto.CreateUserDto;
import com.example.user.service.dto.UpdateUserDto;
import com.example.user.service.dto.UserShortInfoDto;
import com.example.user.service.exception.ExceptionFactory;
import com.example.user.service.exception.UserNotFoundException;
import com.example.user.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Transactional
    public UUID create(CreateUserDto dto) {
        UserEntity entity = mapper.map(dto, encoder);
        return repository.save(entity).getId();
    }

    @Transactional(readOnly = true)
    public UserShortInfoDto read(UUID id) {
        return repository.findById(id).map(mapper::map).orElseThrow(
                () -> ExceptionFactory.createUserNotFoundException(id.toString())
        );
    }

    @Transactional(readOnly = true)
    public UserShortInfoDto read(String email) {
        return repository.findByEmail(email).map(mapper::map).orElseThrow(
                () -> ExceptionFactory.createUserNotFoundException(email)
        );
    }

    @Transactional(readOnly = true)
    public Set<UserShortInfoDto> read() {
        return repository.findAll().stream().map(mapper::map).collect(Collectors.toSet());
    }

    @Transactional
    public UUID update(UUID id, UpdateUserDto dto) {
        UserEntity user = getUserEntity(id);
        mapper.update(user, dto, encoder);
        repository.save(user);
        return id;
    }

    @Transactional
    public void delete(UUID id, Boolean isHardDelete) {
        if (Objects.isNull(isHardDelete) || BooleanUtils.isFalse(isHardDelete)) {
            UserEntity user = getUserEntity(id);
            user.setStatus(Status.DELETED);
            repository.save(user);
        }
        if (BooleanUtils.isTrue(isHardDelete)) {
            repository.deleteById(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = repository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException(email)
        );
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    private UserEntity getUserEntity(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> ExceptionFactory.createUserNotFoundException(id.toString())
        );
    }

}
