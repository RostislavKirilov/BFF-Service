package com.tinqinacademy.bff.api.operations.bookroom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.bff.api.base.OperationRequest;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRoomRequest implements OperationRequest {

    @JsonIgnore
    private String roomId;

    @NotNull(message = "Starting date must not be blank.")
    private LocalDate startDate;

    @NotNull(message = "End date must not be blank.")
    private LocalDate endDate;

    private String firstName;
    private String lastName;
    private String phoneNo;

    @NotNull(message = "User ID must not be blank.")
    private String userId;

}
