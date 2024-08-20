package com.tinqinacademy.bff.api.base;

import com.tinqinacademy.bff.api.base.messages.InvalidInputException;
import com.tinqinacademy.bff.api.base.messages.RoomNotFoundException;
import com.tinqinacademy.bff.api.errors.ErrorMapper;
import com.tinqinacademy.bff.api.errors.ErrorOutput;
import com.tinqinacademy.bff.api.errors.Errors;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import io.vavr.API;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.vavr.API.$;
import static io.vavr.Predicates.instanceOf;


@Getter
@Setter
public abstract class BaseOperation {
    protected final Validator validator;
    protected final ConversionService conversionService;
    protected final ErrorMapper errorMapper;


    protected BaseOperation ( Validator validator, ConversionService conversionService, ErrorMapper errorMapper ) {
        this.validator = validator;
        this.conversionService = conversionService;
        this.errorMapper = errorMapper;
    }

    /**
     * Валидация на входящи данни.
     * Той проверява дали данните отговарят на зададените ограничения и правила за валидност.
     * Ако има нарушения - `InvalidInputException`.
     */

    public <T extends OperationRequest> void validate(T input) {
        Set<ConstraintViolation<T>> violations = validator.validate(input);

        if (!violations.isEmpty()) {
            List<Errors> errorList = new ArrayList<>();
            violations.forEach(violation -> {
                Errors error = Errors.builder()
                        .message(violation.getMessage())
                        .field(violation.getPropertyPath().toString())
                        .build();
                errorList.add(error);
            });
            throw new InvalidInputException(errorMapper.mapErrors(errorList, HttpStatus.BAD_REQUEST));
        }
    }

    protected API.Match.Case<RoomNotFoundException, ErrorOutput> caseRoomNotFound(Throwable cause) {
        return API.Case($(instanceOf(RoomNotFoundException.class)), errorMapper.map(cause, HttpStatus.NOT_FOUND));
    }
}
