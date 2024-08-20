package com.tinqinacademy.bff.rest.controllers;

import com.tinqinacademy.bff.api.errors.Errors;
import com.tinqinacademy.bff.api.operations.partitialupdate.PartUpdateRequest;
import com.tinqinacademy.bff.api.operations.partitialupdate.PartUpdateResponse;
import com.tinqinacademy.bff.core.operations.PartUpdateOperationProcessor;
import com.tinqinacademy.bff.core.operations.VisitorRegistrationOperationProcessor;
import com.tinqinacademy.hotel.api.contracts.RestApiRoutes;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
//@SecurityRequirement(name = "bearerAuth")
public class BffAdminController {

    private final PartUpdateOperationProcessor partUpdateOperationProcessor;
    private final VisitorRegistrationOperationProcessor visitorRegistrationOperationProcessor;
    public BffAdminController ( PartUpdateOperationProcessor partUpdateOperationProcessor, VisitorRegistrationOperationProcessor visitorRegistrationOperationProcessor ) {
        this.partUpdateOperationProcessor = partUpdateOperationProcessor;
        this.visitorRegistrationOperationProcessor = visitorRegistrationOperationProcessor;
    }

    @PatchMapping(RestApiRoutes.PARTIAL_UPDATE_ROOM)
    //@SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> partialUpdateRoom( @PathVariable String roomId, @RequestBody @Validated PartUpdateRequest request) {
        request.setRoomId(roomId);
        log.info("Partial update room with request: {}", request);
        Either<Errors, PartUpdateResponse> result = partUpdateOperationProcessor.process(request);

        return result.fold(
                error -> {
                    log.warn("Partial update failed: {}", error.getMessage());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
                },
                response -> {
                    log.info("Partial update successful for room ID: {}", response.getRoomId());
                    return ResponseEntity.ok(response);
                }
        );
    }

}

