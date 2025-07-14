package com.example.user.service.service;

import com.example.user.service.dal.entity.UserEntity;
import com.example.user.service.dal.enums.Status;
import com.example.user.service.dal.repository.UserRepository;
import com.example.user.service.dto.CreateUserDto;
import com.example.user.service.dto.UpdateUserDto;
import com.example.user.service.dto.UserShortInfoDto;
import com.example.user.service.exception.UserNotFoundException;
import com.example.user.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;
    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    private UUID userId;

    private UserEntity userEntity;

    @BeforeEach
    void setup() {
        userId = UUID.randomUUID();
        userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail("test@example.com");
    }

    @Test
    void shouldCreateUserSuccessfully() {
        UserEntity mapped = new UserEntity();
        mapped.setId(userId);

        when(mapper.map(any(CreateUserDto.class), any(PasswordEncoder.class))).thenReturn(mapped);
        when(repository.save(mapped)).thenReturn(mapped);

        CreateUserDto dto = new CreateUserDto();
        UUID result = userService.create(dto);

        assertThat(result).isEqualTo(userId);
        verify(repository).save(mapped);
    }

    @Test
    void shouldReturnUserById() {
        when(repository.findById(userId)).thenReturn(Optional.of(userEntity));

        UserShortInfoDto expected = new UserShortInfoDto();
        when(mapper.map(userEntity)).thenReturn(expected);

        UserShortInfoDto result = userService.read(userId);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldThrowIfUserNotFoundById() {
        when(repository.findById(userId)).thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> userService.read(userId)
        ).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void shouldSoftDeleteUser() {
        userEntity.setStatus(Status.ACTIVE);
        when(repository.findById(userId)).thenReturn(Optional.of(userEntity));

        userService.delete(userId, false);

        assertThat(userEntity.getStatus()).isEqualTo(Status.DELETED);
        verify(repository).save(userEntity);
    }

    @Test
    void shouldHardDeleteUser() {
        userService.delete(userId, true);
        verify(repository).deleteById(userId);
    }

    @Test
    void adminCanUpdateAnyUser() {
        mockSecurityContext(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        when(repository.findById(userId)).thenReturn(Optional.of(userEntity));

        UpdateUserDto dto = new UpdateUserDto();
        userService.update(userId, dto);
        verify(repository).save(userEntity);
    }

    private void mockSecurityContext(List<SimpleGrantedAuthority> roles) {
        SecurityContext context = mock(SecurityContext.class);
        Authentication auth = mock(Authentication.class);
        User user = new User("admin@example.com", "", roles);

        when(auth.getPrincipal()).thenReturn(user);
        when(context.getAuthentication()).thenReturn(auth);

        MockedStatic<SecurityContextHolder> securityContextHolder = mockStatic(SecurityContextHolder.class);
        securityContextHolder.when(SecurityContextHolder::getContext).thenReturn(context);
    }

}
