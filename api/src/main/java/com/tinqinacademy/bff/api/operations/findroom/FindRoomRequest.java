package com.tinqinacademy.bff.api.operations.findroom;

import com.tinqinacademy.bff.api.base.OperationRequest;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class FindRoomRequest implements OperationRequest {

    private String roomId;

}
