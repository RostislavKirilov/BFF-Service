package com.tinqinacademy.bff.api.operations.partitialupdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.bff.api.base.OperationRequest;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PartUpdateRequest implements OperationRequest {

    @JsonIgnore
    private String roomId;

    private String bed_size;

    private String bathroomType;

    private Integer floor;

    private Integer roomNo;

    private BigDecimal price;
}
