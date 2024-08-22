package com.tinqinacademy.bff.core.operations;

import com.tinqinacademy.bff.api.base.BaseOperation;
import com.tinqinacademy.bff.api.errors.ErrorMapper;
import com.tinqinacademy.bff.api.errors.Errors;
import com.tinqinacademy.bff.api.operations.editcomment.EditCommentOperation;
import com.tinqinacademy.bff.api.operations.editcomment.EditCommentRequest;
import com.tinqinacademy.bff.api.operations.editcomment.EditCommentResponse;
import com.tinqinacademy.comments.api.contracts.operations.adminedit.AdminEditInput;
import com.tinqinacademy.comments.api.contracts.operations.adminedit.AdminEditOutput;
import com.tinqinacademy.comments.restexport.CommentsClient;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class EditCommentOperationProcessor extends BaseOperation implements EditCommentOperation {

    private final CommentsClient commentsClient;
    private final ErrorMapper errorMapper;


    protected EditCommentOperationProcessor ( Validator validator, ConversionService conversionService, ErrorMapper errorMapper, CommentsClient commentsClient, ErrorMapper errorMapper1 ) {
        super(validator, conversionService, errorMapper);
        this.commentsClient = commentsClient;
        this.errorMapper = errorMapper1;
    }
    @Override
    public Either<Errors, EditCommentResponse> process(EditCommentRequest input) {
        return Try.of(() -> {
                    // Валидиране на входните данни
                    validate(input);

                    AdminEditInput adminEditInput = AdminEditInput.builder()
                            .content(input.getContent())
                            .roomNo(input.getRoomId())
                            .build();

                    // Извикване на метода на CommentsClient
                    AdminEditOutput adminEditOutput = commentsClient.userEditComment(input.getCommentId(), adminEditInput);

                    EditCommentResponse response = EditCommentResponse.builder()
                            .commentId(adminEditOutput.getCommentId())
                            .content(adminEditOutput.getMessage())
                            .roomId(input.getRoomId()) // Прехвърляме roomId от входния обект
                            .build();

                    return response;
                })
                .toEither()
                .mapLeft(this::handleErrors);
    }


    private Errors handleErrors(Throwable throwable) {
        // Обработка на грешки с ErrorMapper
        return errorMapper.map(throwable, HttpStatus.BAD_REQUEST).getErrors().get(0);
    }
}
