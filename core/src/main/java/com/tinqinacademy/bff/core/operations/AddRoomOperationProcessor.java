package com.tinqinacademy.bff.core.operations;

import com.tinqinacademy.bff.api.base.BaseOperation;
import com.tinqinacademy.bff.api.errors.ErrorMapper;
import com.tinqinacademy.bff.api.errors.Errors;
import com.tinqinacademy.bff.api.operations.addroom.AddRoomOperation;
import com.tinqinacademy.bff.api.operations.addroom.AddRoomRequest;
import com.tinqinacademy.bff.api.operations.addroom.AddRoomResponse;
import com.tinqinacademy.hotel.api.operations.createroom.CreateRoomInput;
import com.tinqinacademy.hotel.api.operations.createroom.CreateRoomOutput;
import com.tinqinacademy.hotel.restexport.RestExportInterface;
import io.vavr.API;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AddRoomOperationProcessor extends BaseOperation implements AddRoomOperation {

    private final RestExportInterface restExportInterface;
    protected AddRoomOperationProcessor ( Validator validator, ConversionService conversionService, ErrorMapper errorMapper, RestExportInterface restExportInterface ) {
        super(validator, conversionService, errorMapper);
        this.restExportInterface = restExportInterface;
    }

    @Override
    public Either<Errors, AddRoomResponse> process(AddRoomRequest input) {
        return Try.of(() -> {
                    log.info("Start adding room with request: {}", input);
                    validate(input);

                    CreateRoomInput createRoomInput = CreateRoomInput.builder()
                            .roomFloor(input.getRoomFloor())
                            .roomNumber(input.getRoomNumber())
                            .bathroomType(input.getBathroomType())
                            .price(input.getPrice())
                            .bedSize(input.getBedSize())
                            .build();

              CreateRoomOutput createRoomOutput = restExportInterface.addRoom(createRoomInput);



                    AddRoomResponse result = AddRoomResponse.builder()
                            .message("Room added successfully with ID: " + createRoomOutput.getMessage())
                            .build();

                    return result;

                }).toEither()
                .mapLeft(throwable -> API.Match(throwable).of());
    }
}

