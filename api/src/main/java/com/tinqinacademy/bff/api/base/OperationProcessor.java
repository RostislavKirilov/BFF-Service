package com.tinqinacademy.bff.api.base;

import com.tinqinacademy.bff.api.errors.Errors;
    import io.vavr.control.Either;

public interface OperationProcessor<I extends OperationRequest, O extends OperationResponse>{

    Either<Errors, O> process( I input);

}
