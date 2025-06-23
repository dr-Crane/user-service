package com.example.user.service.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ErrorMessages {

    public static final String INVALID_DTO_PARAMETERS = "Не корректные параметры: [%s]";
    public static final String MISSING_REQUEST_BODY = "Не предоставлено тело запроса";

}
