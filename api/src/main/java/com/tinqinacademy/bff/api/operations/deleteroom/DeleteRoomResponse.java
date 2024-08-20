package com.tinqinacademy.bff.api.operations.deleteroom;

import com.tinqinacademy.bff.api.base.OperationResponse;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeleteRoomResponse implements OperationResponse {

    private String message;

}
