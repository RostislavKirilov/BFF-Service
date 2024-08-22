package com.tinqinacademy.bff.api.operations.getallcomments;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private String roomId;
    private String comment;
    private LocalDateTime publishDate;
    private LocalDateTime lastEditTime;
    private String editedBy;
    private String name;
}
