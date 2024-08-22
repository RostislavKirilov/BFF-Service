package com.tinqinacademy.bff.api.operations.getallcomments;

import com.tinqinacademy.bff.api.base.OperationResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCommentsResponse implements OperationResponse {
    private List<CommentResponse> comments;
}
