package com.tinqinacademy.bff.api.operations.admineditcomment;

import com.tinqinacademy.bff.api.base.OperationResponse;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminEditResponse implements OperationResponse {

    private String commentId;
    private String message;
}
