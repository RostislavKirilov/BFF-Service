package com.tinqinacademy.bff.api.operations.admindelete;

import com.tinqinacademy.bff.api.base.OperationResponse;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminDeleteResponse implements OperationResponse {

    private String commentId;
    private String message;
}