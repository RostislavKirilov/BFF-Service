package com.tinqinacademy.bff.api.operations.deleteroom;

import com.tinqinacademy.bff.api.base.OperationRequest;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeleteRoomRequest implements OperationRequest {


    @NotNull
    private String roomId;


}
