package com.tinqinacademy.bff.core.operations;

import com.tinqinacademy.bff.api.base.BaseOperation;
import com.tinqinacademy.bff.api.errors.ErrorMapper;
import com.tinqinacademy.bff.api.errors.Errors;
import com.tinqinacademy.bff.api.operations.updateroom.UpdateRoomOperation;
import com.tinqinacademy.bff.api.operations.updateroom.UpdateRoomRequest;
import com.tinqinacademy.bff.api.operations.updateroom.UpdateRoomResponse;
import com.tinqinacademy.hotel.api.operations.updateroom.UpdateRoomInput;
import com.tinqinacademy.hotel.api.operations.updateroom.UpdateRoomOutput;
import com.tinqinacademy.hotel.restexport.RestExportInterface;
import io.vavr.API;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UpdateRoomOperationProcessor extends BaseOperation implements UpdateRoomOperation {
    private final RestExportInterface restExportInterface;

    protected UpdateRoomOperationProcessor ( Validator validator, ConversionService conversionService, ErrorMapper errorMapper, RestExportInterface restExportInterface ) {
        super(validator, conversionService, errorMapper);
        this.restExportInterface = restExportInterface;
    }

    @Override
    public Either<Errors, UpdateRoomResponse> process(UpdateRoomRequest input) {
        return Try.of(() -> {
                    log.info("Start updating room with request: {}", input);
                    validate(input);

                    UpdateRoomInput updateRoomInput = UpdateRoomInput.builder()
                            .roomId(input.getRoomId())
                            .room_floor(input.getRoom_floor())
                            .room_number(input.getRoom_number())
                            .bathroomType(input.getBathroomType())
                            .price(input.getPrice())
                            .bedSize(input.getBedSize())
                            .build();

                    UpdateRoomOutput updateRoomOutput = restExportInterface.updateRoom(updateRoomInput.getRoomId(), updateRoomInput);


                    UpdateRoomResponse result = UpdateRoomResponse.builder()
                            .roomId(updateRoomOutput.getRoomId())
                            .build();

                    log.info("Room updated successfully with ID: {}", updateRoomOutput.getRoomId());
                    return result;

                }) .toEither()
                .mapLeft(throwable -> API.Match(throwable).of());
    }
}
