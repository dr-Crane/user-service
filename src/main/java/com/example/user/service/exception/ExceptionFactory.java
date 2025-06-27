package com.example.user.service.exception;

import lombok.experimental.UtilityClass;

import static com.example.user.service.exception.ErrorMessages.USER_NOT_FOUND_MESSAGE;

@UtilityClass
public class ExceptionFactory {

    public static UserNotFoundException createUserNotFoundException(String param) {
        return new UserNotFoundException(USER_NOT_FOUND_MESSAGE.formatted(param));
    }

}
