package com.example.user.service.exception;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ExceptionResponseDto {

    private Integer status;

    private Instant timestamp;

    private String message;

    private String path;

}
