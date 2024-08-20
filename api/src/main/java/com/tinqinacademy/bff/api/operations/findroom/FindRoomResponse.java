package com.tinqinacademy.bff.api.operations.findroom;

import com.tinqinacademy.bff.api.base.OperationResponse;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class FindRoomResponse implements OperationResponse {

    private String roomId;
    private BigDecimal price;
    private String floor;
    private String bedSize;
    private String bathroomType;
    private List<LocalDateTime> datesOccupied;
}
