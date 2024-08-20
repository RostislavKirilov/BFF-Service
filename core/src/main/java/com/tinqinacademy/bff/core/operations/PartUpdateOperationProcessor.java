package com.tinqinacademy.bff.core.operations;

import com.tinqinacademy.bff.api.base.BaseOperation;
import com.tinqinacademy.bff.api.errors.Errors;
import com.tinqinacademy.bff.api.operations.partitialupdate.PartUpdateOperation;
import com.tinqinacademy.bff.api.operations.partitialupdate.PartUpdateRequest;
import com.tinqinacademy.bff.api.operations.partitialupdate.PartUpdateResponse;
import com.tinqinacademy.hotel.api.operations.partialupdate.PartialUpdateInput;
import com.tinqinacademy.hotel.api.operations.partialupdate.PartialUpdateOutput;
import com.tinqinacademy.hotel.restexport.RestExportInterface;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PartUpdateOperationProcessor extends BaseOperation implements PartUpdateOperation {

    private final RestExportInterface restExportInterface;

    protected PartUpdateOperationProcessor ( Validator validator, ConversionService conversionService, RestExportInterface restExportInterface ) {
        super(validator, conversionService, null);
        this.restExportInterface = restExportInterface;
    }

    @Override
    public Either<Errors, PartUpdateResponse> process(PartUpdateRequest input) {
        log.info("Received PartUpdateRequest: {}", input);

        return Try.of(() -> {
                    log.info("Start partial update with input: {}", input);

                    validate(input);

                    PartialUpdateInput partialUpdateInput = convertToPartialUpdateInput(input);
                    log.info("Converted PartialUpdateInput: {}", partialUpdateInput);

                    PartialUpdateOutput partialUpdateOutput = restExportInterface.partialUpdateRoom(partialUpdateInput, partialUpdateInput.getRoomId());
                    log.info("Received PartialUpdateOutput: {}", partialUpdateOutput);

                    return PartUpdateResponse.builder()
                            .roomId(partialUpdateOutput.getRoomId())
                            .build();
                })
                .toEither()
                .mapLeft(this::mapError);
    }
    private PartialUpdateInput convertToPartialUpdateInput(PartUpdateRequest input) {
        return PartialUpdateInput.builder()
                .roomId(input.getRoomId())
                .bed_size(input.getBed_size() != null ? input.getBed_size() : null)
                .bathroomType(input.getBathroomType() != null ? input.getBathroomType() : null)
                .floor(input.getFloor() != null ? input.getFloor() : null)
                .roomNo(input.getRoomNo() != null ? input.getRoomNo() : null)
                .price(input.getPrice() != null ? input.getPrice() : null)
                .build();
    }


    private Errors mapError ( Throwable throwable ) {
        // Handle specific errors if needed
        return new Errors("Error during partial update: " + throwable.getMessage());
    }
}