package com.tinqinacademy.bff.api.base.messages;

import com.tinqinacademy.bff.api.errors.ErrorOutput;
import lombok.Getter;

@Getter
public class InvalidInputException extends RuntimeException {
    private final ErrorOutput errorOutput;

    public InvalidInputException(ErrorOutput errorOutput) {
        super(errorOutput.getMessage());
        this.errorOutput = errorOutput;
    }
}

