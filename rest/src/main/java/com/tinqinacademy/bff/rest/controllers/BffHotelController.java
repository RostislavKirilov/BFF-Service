package com.tinqinacademy.bff.rest.controllers;

import com.tinqinacademy.authentication.restexport.RestExportValidateToken;
import com.tinqinacademy.bff.api.errors.Errors;
import com.tinqinacademy.bff.api.operations.addroom.AddRoomRequest;
import com.tinqinacademy.bff.api.operations.addroom.AddRoomResponse;
import com.tinqinacademy.bff.api.operations.bookroom.BookRoomRequest;
import com.tinqinacademy.bff.api.operations.bookroom.BookRoomResponse;
import com.tinqinacademy.bff.api.operations.deleteroom.DeleteRoomRequest;
import com.tinqinacademy.bff.api.operations.deleteroom.DeleteRoomResponse;
import com.tinqinacademy.bff.api.operations.findroom.FindRoomRequest;
import com.tinqinacademy.bff.api.operations.findroom.FindRoomResponse;
import com.tinqinacademy.bff.api.operations.getallcomments.GetCommentsRequest;
import com.tinqinacademy.bff.api.operations.getallcomments.GetCommentsResponse;
import com.tinqinacademy.bff.api.operations.leavecomment.LeaveCommentRequest;
import com.tinqinacademy.bff.api.operations.leavecomment.LeaveCommentResponse;
import com.tinqinacademy.bff.api.operations.registervisitor.RegisterVisitorRequest;
import com.tinqinacademy.bff.api.operations.registervisitor.RegisterVisitorResponse;
import com.tinqinacademy.bff.api.operations.removebooking.RemoveBookingRequest;
import com.tinqinacademy.bff.api.operations.removebooking.RemoveBookingResponse;
import com.tinqinacademy.bff.api.operations.updateroom.UpdateRoomRequest;
import com.tinqinacademy.bff.api.operations.updateroom.UpdateRoomResponse;
import com.tinqinacademy.bff.core.operations.*;
import com.tinqinacademy.comments.api.contracts.RestApiRoutesComments;
import com.tinqinacademy.comments.api.contracts.operations.getallcomments.GetCommentsInput;
import com.tinqinacademy.comments.api.contracts.operations.getallcomments.GetCommentsOutput;
import com.tinqinacademy.hotel.api.contracts.RestApiRoutes;
import com.tinqinacademy.hotel.restexport.RestExportInterface;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@Validated
@RestController
@Slf4j
public class BffHotelController {

    private final RestExportInterface restExportInterface;
    private final FindRoomOperationProcessor findRoomOperationProcessor;
    private final DeleteRoomOperationProcessor deleteRoomOperationProcessor;
    private final BookRoomOperationProcessor bookRoomOperationProcessor;
    private final RemoveBookingOperationProcessor removeBookingOperationProcessor;
    private final AddRoomOperationProcessor addRoomOperationProcessor;
    private final UpdateRoomOperationProcessor updateRoomOperationProcessor;
    private final RestExportValidateToken restExportValidateToken;
    private final VisitorRegistrationOperationProcessor visitorRegistrationOperationProcessor;
    private final LeaveCommentOperationProcessor leaveCommentOperationProcessor;
    private final GetCommentsOperationProcessor getCommentsOperationProcessor;

    @PostMapping(RestApiRoutes.BOOK_ROOM)
   // @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    //@SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<BookRoomResponse> bookRoom ( @PathVariable String roomId, @RequestBody BookRoomRequest request ) {
        log.info("Booking room with request: {}", request);
        request.setRoomId(roomId);
        Either<Errors, BookRoomResponse> result = bookRoomOperationProcessor.process(request);

        return result.fold(
                error -> {
                    log.warn("Booking failed: {}", error.getMessage());
                    return new ResponseEntity<>(BookRoomResponse.builder()
                            .message("Booking failed: " + error.getMessage())
                            .build(), HttpStatus.BAD_REQUEST);
                },
                response -> new ResponseEntity<>(response, HttpStatus.CREATED)
        );
    }

    @GetMapping(RestApiRoutes.FIND_ROOM)
    public ResponseEntity<?> getRoomById ( @PathVariable UUID roomId ) {
        log.info("Attempting to find room with ID: {}", roomId);

        FindRoomRequest request = FindRoomRequest.builder()
                .roomId(roomId.toString())
                .build();

        Either<Errors, FindRoomResponse> result = findRoomOperationProcessor.process(request);
        return result.fold(
                error -> {
                    log.warn("Failed to find room: {}", error.getMessage());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
                },
                response -> {
                    log.info("Room with ID {} found successfully", roomId);
                    return ResponseEntity.ok(response);
                }
        );
    }

    @DeleteMapping(RestApiRoutes.DELETE_ROOM)
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> deleteRoom ( @PathVariable String id ) {
        DeleteRoomRequest request = DeleteRoomRequest
                .builder()
                .roomId(id)
                .build();
        Either<Errors, DeleteRoomResponse> result = deleteRoomOperationProcessor.process(request);
        return result.fold(
                errorOutput -> new ResponseEntity<>(errorOutput, HttpStatus.BAD_REQUEST),
                output -> new ResponseEntity<>(output, HttpStatus.OK)
        );

    }


    @DeleteMapping(RestApiRoutes.REMOVE_BOOKING)
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Remove booking")
    public ResponseEntity<?> removeBooking ( @PathVariable String bookId ) {
        log.info("Removing booking with ID: {}", bookId);
        RemoveBookingRequest request = RemoveBookingRequest.builder()
                .bookingId(bookId)
                .build();

        Either<Errors, RemoveBookingResponse> result = removeBookingOperationProcessor.process(request);
        return result.fold(
                error -> {
                    log.warn("Failed to remove booking: {}", error.getMessage());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
                },
                response -> {
                    log.info("Booking with ID {} removed successfully", bookId);
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
                }
        );
    }

    @PostMapping(RestApiRoutes.ADD_ROOM)
    @Operation(summary = "Add a room")
    public ResponseEntity<?> addRoom ( @RequestBody @Validated AddRoomRequest addRoomRequest ) {
        Either<Errors, AddRoomResponse> result = addRoomOperationProcessor.process(addRoomRequest);

        if (result.isRight()) {
            AddRoomResponse addRoomResponse = result.get();
            return ResponseEntity.status(HttpStatus.CREATED).body(addRoomResponse);
        } else {
            Errors errors = result.getLeft();
            return ResponseEntity.badRequest().body(errors);
        }
    }

    @PutMapping(RestApiRoutes.UPDATE_ROOM)
    @Operation(summary = "Update room information")
    public ResponseEntity<?> editInfo (
            @PathVariable("roomId") String roomId,
            @RequestBody @Validated UpdateRoomRequest input ) {

        log.info("Attempting to update room with ID: {}", roomId);

        input.setRoomId((roomId));

        Either<Errors, UpdateRoomResponse> result = updateRoomOperationProcessor.process(input);

        return result.fold(
                error -> {
                    log.warn("Failed to update room: {}", error.getMessage());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
                },
                response -> {
                    log.info("Room with ID {} updated successfully", roomId);
                    return ResponseEntity.ok(response);
                }
        );
    }

    @PostMapping(RestApiRoutes.REGISTER_VISITOR)
    @Operation(summary = "Register a visitor")
    public ResponseEntity<?> registerVisitor ( @RequestBody RegisterVisitorRequest request ) {
        log.info("Registering visitor with request: {}", request);
        Either<Errors, RegisterVisitorResponse> result = visitorRegistrationOperationProcessor.process(request);

        return result.fold(
                error -> {
                    log.warn("Visitor registration failed: {}", error.getMessage());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
                },
                response -> {
                    log.info("Visitor registered successfully");
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                }
        );
    }


    @PostMapping(RestApiRoutesComments.LEAVE_COMMENT)
    @Operation(summary = "Leave a comment for a certain room.")
    public ResponseEntity<?> leaveComment ( @RequestBody LeaveCommentRequest request ) {
        log.info("Leaving comment with request: {}", request);

        Either<Errors, LeaveCommentResponse> result = leaveCommentOperationProcessor.process(request);

        return result.fold(
                error -> {
                    log.warn("Failed to leave comment: {}", error.getMessage());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error.getMessage());
                },
                response -> {
                    log.info("Comment left successfully");
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                }
        );
    }

    @GetMapping(RestApiRoutesComments.GET_COMMENTS)
    public ResponseEntity<?> getComments(@PathVariable String roomId) {
        GetCommentsRequest request = GetCommentsRequest.builder()
                .roomId(roomId)
                .build();

        log.info("Received request to get comments for room ID: {}", roomId);

        Either<Errors, GetCommentsResponse> result = getCommentsOperationProcessor.process(request);

        return result.fold(
                error -> {
                    log.warn("Failed to get comments: {}", error.getMessage());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
                },
                response -> {
                    log.info("Successfully retrieved comments for room ID: {}", roomId);
                    return ResponseEntity.ok(response);
                }
        );
    }


    private HttpStatus determineHttpStatus(Errors errors) {
        if (errors.getMessage().contains("No comments found")) {
            return HttpStatus.NOT_FOUND;
        }
        return HttpStatus.BAD_REQUEST;
    }
}



