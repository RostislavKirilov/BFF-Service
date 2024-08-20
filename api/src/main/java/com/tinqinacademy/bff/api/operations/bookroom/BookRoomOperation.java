package com.tinqinacademy.bff.api.operations.bookroom;

import com.tinqinacademy.bff.api.base.OperationProcessor;
import com.tinqinacademy.bff.api.errors.Errors;
import io.vavr.control.Either;

public interface BookRoomOperation extends OperationProcessor<BookRoomRequest, BookRoomResponse> {

}
