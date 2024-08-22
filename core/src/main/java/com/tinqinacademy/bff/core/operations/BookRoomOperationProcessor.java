package com.tinqinacademy.bff.core.operations;

import com.tinqinacademy.bff.api.base.BaseOperation;
import com.tinqinacademy.bff.api.errors.ErrorMapper;
import com.tinqinacademy.bff.api.errors.Errors;
import com.tinqinacademy.bff.api.operations.bookroom.BookRoomOperation;
import com.tinqinacademy.bff.api.operations.bookroom.BookRoomRequest;
import com.tinqinacademy.bff.api.operations.bookroom.BookRoomResponse;
import com.tinqinacademy.hotel.api.operations.bookroom.BookRoomInput;
import com.tinqinacademy.hotel.api.operations.bookroom.BookRoomOutput;
import com.tinqinacademy.hotel.restexport.RestExportInterface;
import io.vavr.API;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import static io.vavr.API.$;
import static io.vavr.API.Case;

@Service
@Slf4j
public class BookRoomOperationProcessor extends BaseOperation implements BookRoomOperation {

    private final RestExportInterface restExportInterface;


    public BookRoomOperationProcessor ( Validator validator, ConversionService conversionService, ErrorMapper errorMapper, RestExportInterface restExportInterface ) {
        super(validator, conversionService, errorMapper);
        this.restExportInterface = restExportInterface;
    }

    @Override
    public Either<Errors, BookRoomResponse> process(BookRoomRequest input) {
        return Try.of(() -> {
                    log.info("Start booking room with input: {}", input);
                    validate(input);

                    BookRoomInput bookRoomInput = BookRoomInput.builder()
                            .startDate(input.getStartDate())
                            .endDate(input.getEndDate())
                            .firstName(input.getFirstName())
                            .lastName(input.getLastName())
                            .phoneNo(input.getPhoneNo())
                            .userId(input.getUserId())
                            .build();

                    String roomId = input.getRoomId();
                    BookRoomOutput output = restExportInterface.bookRoom(roomId, bookRoomInput);

                    BookRoomResponse result = BookRoomResponse.builder()
                            .bookingId(output.getBookingId())
                            .message("Room booked successfully!")
                            .build();
                    return result;
                })
                .toEither()
                .mapLeft(throwable -> {
                    log.error("Failed to book room: {}", throwable.getMessage());
                    return API.Match(throwable).of(
                            Case($(), new Errors("General error message"))
                    );
                });
    }
}
