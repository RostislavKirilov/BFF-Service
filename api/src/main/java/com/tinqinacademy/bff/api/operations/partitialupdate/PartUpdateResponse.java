package com.tinqinacademy.bff.api.operations.partitialupdate;

import com.tinqinacademy.bff.api.base.OperationResponse;
import lombok.*;


@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PartUpdateResponse implements OperationResponse {
    String roomId;
}
