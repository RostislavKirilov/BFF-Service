package com.tinqinacademy.bff.api.operations.registervisitor;

import com.tinqinacademy.bff.api.base.OperationRequest;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterVisitorRequest implements OperationRequest {

    @NotNull(message = "Room ID cannot be null")
    private String roomId;

    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;

    @NotNull(message = "First name cannot be null")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    private String lastName;

    @NotNull(message = "Phone number cannot be null")
    private String phoneNo;

    @NotNull(message = "ID card number cannot be null")
    private String idCardNo;

    @NotNull(message = "ID card validity cannot be null")
    private LocalDate idCardValidity;

    @NotNull(message = "ID card issue authority cannot be null")
    private String idCardIssueAuthority;

    @NotNull(message = "ID card issue date cannot be null")
    private LocalDate idCardIssueDate;
}
