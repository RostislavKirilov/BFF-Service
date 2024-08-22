package com.tinqinacademy.bff.core.operations;


import com.tinqinacademy.bff.api.base.BaseOperation;
import com.tinqinacademy.bff.api.errors.ErrorMapper;
import com.tinqinacademy.bff.api.errors.Errors;
import com.tinqinacademy.bff.api.operations.getallcomments.CommentResponse;
import com.tinqinacademy.bff.api.operations.getallcomments.GetCommentsOperation;
import com.tinqinacademy.bff.api.operations.getallcomments.GetCommentsRequest;
import com.tinqinacademy.bff.api.operations.getallcomments.GetCommentsResponse;
import com.tinqinacademy.comments.api.contracts.operations.getallcomments.GetCommentsInput;
import com.tinqinacademy.comments.api.contracts.operations.getallcomments.GetCommentsOutput;
import com.tinqinacademy.comments.restexport.CommentsClient;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GetCommentsOperationProcessor extends BaseOperation implements GetCommentsOperation {

    private final CommentsClient commentsClient;

    protected GetCommentsOperationProcessor ( Validator validator, ConversionService conversionService, ErrorMapper errorMapper, CommentsClient commentsClient ) {
        super(validator, conversionService, errorMapper);
        this.commentsClient = commentsClient;
    }

    @Override
    public Either<Errors, GetCommentsResponse> process(GetCommentsRequest input) {
        return Try.of(() -> {
                    // Валидиране на входните данни
                    validate(input);

                    // Извикване на CommentsClient за извличане на коментари
                    GetCommentsOutput commentsResult = commentsClient.getComments(input.getRoomId());

                    List<CommentResponse> commentResponses = commentsResult.getComments().stream()
                            .map(comment -> CommentResponse.builder()
                                    .roomId(comment.getRoomId())
                                    .comment(comment.getComment())
                                    .publishDate(comment.getPublishDate())
                                    .lastEditTime(comment.getLastEditTime())
                                    .editedBy(comment.getEditedBy())
                                    .name(comment.getName())
                                    .build())
                            .collect(Collectors.toList());

                    return GetCommentsResponse.builder()
                            .comments(commentResponses)
                            .build();
                })
                .toEither()
                .mapLeft(this::handleErrors);
    }

    private Errors handleErrors(Throwable throwable) {
        return errorMapper.map(throwable, HttpStatus.BAD_REQUEST).getErrors().get(0);
    }
}
