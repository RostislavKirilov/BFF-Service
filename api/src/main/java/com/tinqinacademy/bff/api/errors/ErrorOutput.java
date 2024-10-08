package com.tinqinacademy.bff.api.errors;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ErrorOutput {

    private List<Errors> errors;
    private HttpStatus status;

    public String getMessage() {
        if (errors == null || errors.isEmpty()) {
            return "Unknown error";
        }
        return errors.stream()
                .map(Errors::getMessage)
                .collect(Collectors.joining(", "));
    }
}
