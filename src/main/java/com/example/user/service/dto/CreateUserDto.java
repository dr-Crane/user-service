package com.example.user.service.dto;

import lombok.Data;

@Data
public class CreateUserDto {

    private String name;

    private String surname;

    private String email;

    private String password;

}
