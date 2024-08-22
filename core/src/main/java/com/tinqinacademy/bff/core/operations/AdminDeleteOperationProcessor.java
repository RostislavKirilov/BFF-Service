package com.tinqinacademy.bff.core.operations;

import com.tinqinacademy.bff.api.base.BaseOperation;
import com.tinqinacademy.bff.api.errors.ErrorMapper;
import com.tinqinacademy.bff.api.errors.ErrorOutput;
import com.tinqinacademy.bff.api.errors.Errors;
import com.tinqinacademy.bff.api.operations.admindelete.AdminDeleteRequest;
import com.tinqinacademy.bff.api.operations.admindelete.AdminDeleteOperation;
import com.tinqinacademy.bff.api.operations.admindelete.AdminDeleteResponse;
import com.tinqinacademy.comments.api.contracts.operations.admindelete.AdminDeleteOutput;
import com.tinqinacademy.comments.restexport.CommentsClient;
import feign.FeignException;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminDeleteOperationProcessor extends BaseOperation implements AdminDeleteOperation {

    private final CommentsClient commentsClient;

    public AdminDeleteOperationProcessor(Validator validator, ConversionService conversionService, ErrorMapper errorMapper, CommentsClient commentsClient) {
        super(validator, conversionService, errorMapper);
        this.commentsClient = commentsClient;
    }

    @Override
    public Either<Errors, AdminDeleteResponse> process(AdminDeleteRequest input) {
        return Try.of(() -> {
                    // Валидиране на входните данни
                    validate(input);

                    // Извикване на CommentsClient за изтриване на коментар
                    AdminDeleteOutput deleteOutput = commentsClient.adminDeleteComment(input.getCommentId());

                    // Успешен резултат
                    return AdminDeleteResponse.builder()
                            .commentId(deleteOutput.getCommentId())
                            .message(deleteOutput.getMessage())
                            .build();
                })
                .toEither()
                .mapLeft(this::handleErrors);
    }

    private Errors handleErrors(Throwable throwable) {
        // Обработка на грешки с ErrorMapper
        return errorMapper.map(throwable, HttpStatus.BAD_REQUEST).getErrors().get(0);
    }
}