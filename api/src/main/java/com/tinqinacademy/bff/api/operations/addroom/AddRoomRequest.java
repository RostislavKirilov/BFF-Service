package com.tinqinacademy.bff.api.operations.addroom;

import com.tinqinacademy.bff.api.base.OperationRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddRoomRequest implements OperationRequest {

    @NotNull
    private Integer roomFloor;

    @NotBlank
    private String roomNumber;

    @NotBlank
    private String bathroomType;

    @NotNull
    private BigDecimal price;

    @NotNull
    private String bedSize;

    @Getter
    private List<String> bedSizes;
}
