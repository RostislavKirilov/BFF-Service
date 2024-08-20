package com.tinqinacademy.bff.api.base.exceptions;

public interface CustomExceptionHandler<T extends Exception> {
    ErrorWrapper handleException(T ex);
}
