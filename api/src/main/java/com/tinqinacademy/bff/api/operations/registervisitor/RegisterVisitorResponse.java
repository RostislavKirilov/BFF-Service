package com.tinqinacademy.bff.api.operations.registervisitor;

import com.tinqinacademy.bff.api.base.OperationResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RegisterVisitorResponse implements OperationResponse {

    private String guestId;

}
