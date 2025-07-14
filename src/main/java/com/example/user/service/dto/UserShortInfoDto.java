package com.example.user.service.dto;

import com.example.user.service.dal.enums.Role;
import com.example.user.service.dal.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserShortInfoDto {

    private UUID id;

    private String name;

    private String surname;

    private String email;

    private Role role;

    private Status status;

    private Instant createdAt;

}
