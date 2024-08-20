package com.tinqinacademy.bff.api.operations.updateroom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.bff.api.base.OperationRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoomRequest implements OperationRequest {

    @NotNull
    private String bedSize;

    @JsonIgnore
    private String roomId;

    @NotNull
    private String bathroomType;

    @NotNull
    private Integer room_floor;

    @NotNull
    private String room_number;

    @NotNull
    @Positive
    private BigDecimal price;
}
