package com.tinqinacademy.bff.api.operations.leavecomment;

import com.tinqinacademy.bff.api.base.OperationResponse;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LeaveCommentResponse implements OperationResponse {private String roomId;
    private String comment;
    private LocalDateTime publishedDate;
    private LocalDateTime lastEditBy;
    private String editedBy;

}
