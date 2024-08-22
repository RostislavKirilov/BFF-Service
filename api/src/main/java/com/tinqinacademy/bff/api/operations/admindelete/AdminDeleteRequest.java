package com.tinqinacademy.bff.api.operations.admindelete;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.bff.api.base.OperationRequest;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminDeleteRequest implements OperationRequest {

    @JsonIgnore
    private String roomId;
    private String commentId;
}
