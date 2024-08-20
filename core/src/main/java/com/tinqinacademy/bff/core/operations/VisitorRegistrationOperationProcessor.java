package com.tinqinacademy.bff.core.operations;

import com.tinqinacademy.bff.api.base.BaseOperation;
import com.tinqinacademy.bff.api.errors.ErrorMapper;
import com.tinqinacademy.bff.api.errors.Errors;
import com.tinqinacademy.bff.api.operations.registervisitor.RegisterVisitorOperation;
import com.tinqinacademy.bff.api.operations.registervisitor.RegisterVisitorRequest;
import com.tinqinacademy.bff.api.operations.registervisitor.RegisterVisitorResponse;
import com.tinqinacademy.hotel.api.operations.visitorregistration.input.VisitorRegistrationInput;
import com.tinqinacademy.hotel.api.operations.visitorregistration.output.VisitorRegistrationOutput;
import com.tinqinacademy.hotel.restexport.RestExportInterface;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;


@Service
@Slf4j
public class VisitorRegistrationOperationProcessor extends BaseOperation implements RegisterVisitorOperation {

    private final RestExportInterface restExportInterface;

    protected VisitorRegistrationOperationProcessor(Validator validator, ConversionService conversionService, ErrorMapper errorMapper, RestExportInterface restExportInterface) {
        super(validator, conversionService, errorMapper);
        this.restExportInterface = restExportInterface;
    }

    @Override
    public Either<Errors, RegisterVisitorResponse> process(RegisterVisitorRequest input) {
        return Try.of(() -> {
                    log.info("Start registering visitor with input: {}", input);
                    validate(input);

                    VisitorRegistrationInput visitorInput = VisitorRegistrationInput.builder()
                            .roomId(input.getRoomId())
                            .firstName(input.getFirstName())
                            .lastName(input.getLastName())
                            .phoneNo(input.getPhoneNo())
                            .startDate(input.getStartDate())
                            .endDate(input.getEndDate())
                            .idCardNo(input.getIdCardNo())
                            .idCardValidity(input.getIdCardValidity())
                            .idCardIssueAuthority(input.getIdCardIssueAuthority())
                            .idCardIssueDate(input.getIdCardIssueDate())
                            .build();

                    VisitorRegistrationOutput visitorOutput = restExportInterface.registerVisitor(visitorInput);

                    return RegisterVisitorResponse.builder()
                            .guestId(visitorOutput.getGuestId())
                            .build();
                })
                .toEither()
                .mapLeft(this::mapError);
    }

    private Errors mapError(Throwable throwable) {
        return Match(throwable).of(
                Case($(instanceOf(IllegalArgumentException.class)), t -> new Errors("Invalid argument: " + t.getMessage())),
                Case($(), t -> new Errors("Internal server error: " + t.getMessage()))
        );
    }
}
