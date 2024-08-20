package com.tinqinacademy.bff.api.base.exceptions;

import com.tinqinacademy.bff.api.base.messages.ExceptionMessages;

public class UserNotFoundException extends RuntimeException{
    private final String message = ExceptionMessages.USER_NOT_FOUND;
}

