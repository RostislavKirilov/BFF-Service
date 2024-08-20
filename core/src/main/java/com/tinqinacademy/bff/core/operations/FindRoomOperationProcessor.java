package com.tinqinacademy.bff.core.operations;

import com.tinqinacademy.bff.api.base.BaseOperation;
import com.tinqinacademy.bff.api.errors.ErrorMapper;
import com.tinqinacademy.bff.api.errors.Errors;
import com.tinqinacademy.bff.api.operations.findroom.FindRoomOperation;
import com.tinqinacademy.bff.api.operations.findroom.FindRoomRequest;
import com.tinqinacademy.bff.api.operations.findroom.FindRoomResponse;
import com.tinqinacademy.hotel.api.operations.findroom.RoomId;
import com.tinqinacademy.hotel.restexport.RestExportInterface;
import io.vavr.API;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class FindRoomOperationProcessor extends BaseOperation implements FindRoomOperation {

    private final RestExportInterface restExportInterface;

    protected FindRoomOperationProcessor ( Validator validator, ConversionService conversionService, ErrorMapper errorMapper, RestExportInterface restExportInterface ) {
        super(validator, conversionService, errorMapper);
        this.restExportInterface = restExportInterface;
    }

    @Override
    public Either<Errors, FindRoomResponse> process(FindRoomRequest input) {
        return Try.of(() -> {
                    log.info("Start finding room with ID: {}", input.getRoomId());
                    validate(input);
                    UUID roomId;
                    try {
                        roomId = UUID.fromString(input.getRoomId());
                    } catch (IllegalArgumentException e) {
                        log.error("Invalid room ID format: {}", input.getRoomId());
                        throw new IllegalArgumentException("Invalid room ID format", e);
                    }

                    RoomId roomDetails = restExportInterface.getRoomById(String.valueOf(roomId));
                    FindRoomResponse response = FindRoomResponse.builder()
                            .roomId(roomDetails.getRoomId().toString())
                            .price(roomDetails.getPrice())
                            .floor(roomDetails.getFloor())
                            .bedSize(roomDetails.getBedSize())
                            .bathroomType(roomDetails.getBathroomType())
                            .datesOccupied(roomDetails.getDatesOccupied())
                            .build();

                    return response;

                })
                .toEither()
                .mapLeft(throwable -> {
                    // Log and map errors
                    log.error("Error during room finding operation", throwable);
                    return API.Match(throwable).of(
                            API.Case(API.$(e -> e instanceof IllegalArgumentException), e -> new Errors("Invalid input: " + e.getMessage())),
                            API.Case(API.$(), e -> new Errors("Internal server error"))
                    );
                });
    }
}

