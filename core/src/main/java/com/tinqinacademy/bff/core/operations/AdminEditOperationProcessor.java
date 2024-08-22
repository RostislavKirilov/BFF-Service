package com.tinqinacademy.bff.core.operations;

import com.tinqinacademy.bff.api.base.BaseOperation;
import com.tinqinacademy.bff.api.errors.ErrorMapper;
import com.tinqinacademy.bff.api.errors.Errors;
import com.tinqinacademy.bff.api.operations.admineditcomment.AdminEditOperation;
import com.tinqinacademy.bff.api.operations.admineditcomment.AdminEditRequest;
import com.tinqinacademy.bff.api.operations.admineditcomment.AdminEditResponse;
import com.tinqinacademy.comments.api.contracts.operations.adminedit.AdminEditInput;
import com.tinqinacademy.comments.api.contracts.operations.adminedit.AdminEditOutput;
import com.tinqinacademy.comments.restexport.CommentsClient;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AdminEditOperationProcessor extends BaseOperation implements AdminEditOperation {

    private final CommentsClient commentsClient;

    public AdminEditOperationProcessor(Validator validator, ConversionService conversionService, ErrorMapper errorMapper, CommentsClient commentsClient) {
        super(validator, conversionService, errorMapper);
        this.commentsClient = commentsClient;
    }

    @Override
    public Either<Errors, AdminEditResponse> process(AdminEditRequest input) {
        return Try.of(() -> {
                    // Валидиране на входните данни
                    validate(input);

                    // Извикване на CommentsClient за редактиране на коментар
                    AdminEditInput adminEditInput = AdminEditInput.builder()
                            .commentId(input.getCommentId())
                            .roomNo(input.getRoomNo())
                            .firstName(input.getFirstName())
                            .lastName(input.getLastName())
                            .content(input.getContent())
                            .build();

                    AdminEditOutput editOutput = commentsClient.adminEditComment(input.getCommentId(), adminEditInput);

                    return AdminEditResponse.builder()
                            .commentId(editOutput.getCommentId())
                            .message(editOutput.getMessage())
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
