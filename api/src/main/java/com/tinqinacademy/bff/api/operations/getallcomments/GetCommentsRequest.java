package com.tinqinacademy.bff.api.operations.getallcomments;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.bff.api.base.OperationRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCommentsRequest implements OperationRequest {

    @JsonIgnore
    private String roomId;
}
