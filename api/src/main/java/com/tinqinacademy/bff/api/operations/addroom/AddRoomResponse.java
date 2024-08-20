package com.tinqinacademy.bff.api.operations.addroom;

import com.tinqinacademy.bff.api.base.OperationResponse;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddRoomResponse implements OperationResponse {
    private String message;

}
