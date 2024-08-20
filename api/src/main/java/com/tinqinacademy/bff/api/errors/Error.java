package com.tinqinacademy.bff.api.errors;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Error {

    private String message;
    private String field;
    private String code;

    public Error(String message) {
        this.message = message;
    }
}
