package com.tinqinacademy.bff.api.operations.leavecomment;

import com.tinqinacademy.bff.api.base.OperationRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LeaveCommentRequest implements OperationRequest {

    @NotBlank(message = "Room ID must not be blank!")
    private String roomId;
    @NotBlank(message = "Please, enter your first name!")
    @Schema(example = "Jon")
    private String firstName;
    @NotBlank(message = "Please, enter your last name!")
    @Schema(example = "Doe")
    private String lastName;
    @NotBlank(message = "Please, enter your comment!")
    private String comment;
}
