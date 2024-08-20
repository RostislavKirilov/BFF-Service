package com.tinqinacademy.bff.api.operations.removebooking;

import com.tinqinacademy.bff.api.base.OperationRequest;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Builder
public class RemoveBookingRequest implements OperationRequest {

    @NotNull
    private String bookingId;
}
