package com.tinqinacademy.bff.rest.controllers;

import com.tinqinacademy.authentication.restexport.RestExportValidateToken;
import com.tinqinacademy.bff.api.errors.Errors;
import com.tinqinacademy.bff.api.operations.admindelete.AdminDeleteOperation;
import com.tinqinacademy.bff.api.operations.admindelete.AdminDeleteRequest;
import com.tinqinacademy.bff.api.operations.admindelete.AdminDeleteResponse;
import com.tinqinacademy.bff.api.operations.admineditcomment.AdminEditRequest;
import com.tinqinacademy.bff.api.operations.admineditcomment.AdminEditResponse;
import com.tinqinacademy.bff.api.operations.editcomment.EditCommentRequest;
import com.tinqinacademy.bff.api.operations.editcomment.EditCommentResponse;
import com.tinqinacademy.bff.api.operations.partitialupdate.PartUpdateRequest;
import com.tinqinacademy.bff.api.operations.partitialupdate.PartUpdateResponse;
import com.tinqinacademy.bff.core.operations.*;
import com.tinqinacademy.comments.api.contracts.RestApiRoutesComments;
import com.tinqinacademy.hotel.api.contracts.RestApiRoutes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class BffAdminController {

    private final PartUpdateOperationProcessor partUpdateOperationProcessor;
    private final VisitorRegistrationOperationProcessor visitorRegistrationOperationProcessor;
    private final EditCommentOperationProcessor editCommentOperationProcessor;
    private final AdminEditOperationProcessor adminEditOperationProcessor;
    private final AdminDeleteOperationProcessor adminDeleteOperationProcessor;
    private final RestExportValidateToken restExportValidateToken;


    public BffAdminController ( PartUpdateOperationProcessor partUpdateOperationProcessor, VisitorRegistrationOperationProcessor visitorRegistrationOperationProcessor, AdminDeleteOperationProcessor adminDeleteOperationProcessor, EditCommentOperationProcessor editCommentOperationProcessor, AdminEditOperationProcessor adminEditOperationProcessor, AdminDeleteOperation adminDeleteOperation, AdminDeleteOperationProcessor adminDeleteOperationProcessor1, RestExportValidateToken restExportValidateToken ) {
        this.partUpdateOperationProcessor = partUpdateOperationProcessor;
        this.visitorRegistrationOperationProcessor = visitorRegistrationOperationProcessor;
        this.editCommentOperationProcessor = editCommentOperationProcessor;
        this.adminEditOperationProcessor = adminEditOperationProcessor;
        this.adminDeleteOperationProcessor = adminDeleteOperationProcessor1;
        this.restExportValidateToken = restExportValidateToken;
    }

    @PatchMapping(RestApiRoutes.PARTIAL_UPDATE_ROOM)
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> partialUpdateRoom ( @PathVariable String roomId, @RequestBody @Validated PartUpdateRequest request ) {
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

    @DeleteMapping(RestApiRoutesComments.ADMIN_DELETE)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Admin can delete any comment.")
    public ResponseEntity<AdminDeleteResponse> deleteComment ( @PathVariable String commentId ) {
        log.info("Received request to delete comment with ID: {}", commentId);

        AdminDeleteRequest request = AdminDeleteRequest.builder()
                .commentId(commentId)
                .build();

        Either<Errors, AdminDeleteResponse> result = adminDeleteOperationProcessor.process(request);

        return result.fold(
                error -> {
                    log.error("Error during delete operation: {}", error.getMessage());
                    HttpStatus status = error.getHttpStatusCode() == HttpStatus.NOT_FOUND.value() ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
                    return ResponseEntity.status(status)
                            .body(new AdminDeleteResponse(commentId, error.getMessage()));
                },
                response -> {
                    log.info("Successfully deleted comment with ID: {}", commentId);
                    return ResponseEntity.ok(response);
                }
        );
    }

    @PatchMapping("/api/v1/hotel/comment/{commentId}")
    public ResponseEntity<?> editComment (
            @PathVariable String commentId,
            @RequestBody @Validated EditCommentRequest editCommentRequest ) {

        log.info("Received request to edit comment with ID: {}", commentId);

        editCommentRequest.setCommentId(commentId);

        Either<Errors, EditCommentResponse> result = editCommentOperationProcessor.process(editCommentRequest);

        return result.fold(
                error -> {
                    log.warn("Edit comment failed: {}", error.getMessage());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                },
                response -> {
                    log.info("Edit comment successful for comment ID: {}", response.getCommentId());
                    return ResponseEntity.ok(response);
                }
        );
    }

    @PatchMapping("/api/v1/system/comment/{commentId}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> editComment ( @PathVariable("commentId") String commentId, @RequestBody AdminEditRequest request ) {
        request.setCommentId(commentId);

        Either<Errors, AdminEditResponse> result = adminEditOperationProcessor.process(request);

        return result.fold(
                error -> {
                    log.warn("Failed to edit comment: {}", error.getMessage());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                },
                response -> {
                    log.info("Successfully edited comment with ID: {}", commentId);
                    return ResponseEntity.ok(response);
                }
        );
    }
}


