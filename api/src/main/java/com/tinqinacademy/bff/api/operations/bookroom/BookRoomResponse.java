package com.tinqinacademy.bff.api.operations.bookroom;

import com.tinqinacademy.bff.api.base.OperationResponse;
import lombok.Builder;

import java.util.UUID;

@Builder
public class BookRoomResponse implements OperationResponse {

    private String bookingId;
    private String message;

}
