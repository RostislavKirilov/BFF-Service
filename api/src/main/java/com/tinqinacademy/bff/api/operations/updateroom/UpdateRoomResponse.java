package com.tinqinacademy.bff.api.operations.updateroom;

import com.tinqinacademy.bff.api.base.OperationResponse;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoomResponse implements OperationResponse {

    String roomId;

}
