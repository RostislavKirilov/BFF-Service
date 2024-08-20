package com.tinqinacademy.bff.core.operations;

import com.tinqinacademy.bff.api.base.BaseOperation;
import com.tinqinacademy.bff.api.errors.ErrorMapper;
import com.tinqinacademy.bff.api.errors.Errors;
import com.tinqinacademy.bff.api.operations.deleteroom.DeleteRoomOperation;
import com.tinqinacademy.bff.api.operations.deleteroom.DeleteRoomRequest;
import com.tinqinacademy.bff.api.operations.deleteroom.DeleteRoomResponse;
import com.tinqinacademy.hotel.api.operations.deleteroom.DeleteRoomOutput;
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
public class DeleteRoomOperationProcessor extends BaseOperation implements DeleteRoomOperation {

    private final RestExportInterface restExportInterface;

    protected DeleteRoomOperationProcessor ( Validator validator, ConversionService conversionService, ErrorMapper errorMapper, RestExportInterface restExportInterface ) {
        super(validator, conversionService, errorMapper);
        this.restExportInterface = restExportInterface;
    }

    @Override
    public Either<Errors, DeleteRoomResponse> process ( DeleteRoomRequest input ) {
       return Try.of(() ->{
           log.info("Start deleting room with ID:{}", input);
           validate(input);

               DeleteRoomOutput output = restExportInterface.deleteRoom(input.getRoomId());

           DeleteRoomResponse result = DeleteRoomResponse.builder().
                   message("Deleted room!")
                   .build();
           return result;

        })
               .toEither()
               .mapLeft(throwable -> API.Match(throwable).of());
    }
}
