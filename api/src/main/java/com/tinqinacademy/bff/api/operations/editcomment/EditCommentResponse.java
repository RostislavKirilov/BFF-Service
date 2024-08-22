package com.tinqinacademy.bff.api.operations.editcomment;

import com.tinqinacademy.bff.api.base.OperationResponse;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EditCommentResponse implements OperationResponse {

    private String commentId;
    private String content;
    private String roomId;
}
