package com.tinqinacademy.bff.api.operations.editcomment;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.bff.api.base.OperationRequest;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EditCommentRequest implements OperationRequest {

    @Size(max = 100, message = "Content must be less than or equal to 1000 characters")
    private String content;

    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Room ID must be alphanumeric")
    @JsonIgnore
    private String roomId;
    @JsonIgnore
    private String commentId;
}
