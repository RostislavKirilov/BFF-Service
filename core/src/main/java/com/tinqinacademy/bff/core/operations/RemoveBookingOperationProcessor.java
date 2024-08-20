package com.tinqinacademy.bff.core.operations;

import com.tinqinacademy.bff.api.base.BaseOperation;
import com.tinqinacademy.bff.api.errors.ErrorMapper;
import com.tinqinacademy.bff.api.errors.Errors;
import com.tinqinacademy.bff.api.operations.removebooking.RemoveBookingOperation;
import com.tinqinacademy.bff.api.operations.removebooking.RemoveBookingRequest;
import com.tinqinacademy.bff.api.operations.removebooking.RemoveBookingResponse;
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
public class RemoveBookingOperationProcessor extends BaseOperation implements RemoveBookingOperation {

    private final RestExportInterface restExportInterface;

    protected RemoveBookingOperationProcessor(Validator validator, ConversionService conversionService, ErrorMapper errorMapper, RestExportInterface restExportInterface) {
        super(validator, conversionService, errorMapper);
        this.restExportInterface = restExportInterface;
    }

    @Override
    public Either<Errors, RemoveBookingResponse> process( RemoveBookingRequest input) {
        return Try.of(() -> {
                    log.info("Start removing booking with ID: {}", input.getBookingId());
                    validate(input);

                    UUID bookingId;
                    try {
                        bookingId = UUID.fromString(input.getBookingId());
                    } catch (IllegalArgumentException e) {
                        log.error("Invalid booking ID format: {}", input.getBookingId());
                        throw new IllegalArgumentException("Invalid booking ID format", e);
                    }

                    restExportInterface.removeBooking(UUID.fromString(String.valueOf(bookingId)));

                    RemoveBookingResponse response = RemoveBookingResponse.builder()
                            .build();

                    return response;

                })
                .toEither()
                .mapLeft(throwable -> API.Match(throwable).of());
                };
    }

