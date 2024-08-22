package com.tinqinacademy.bff.core.operations;

import com.tinqinacademy.bff.api.base.BaseOperation;
import com.tinqinacademy.bff.api.errors.ErrorMapper;
import com.tinqinacademy.bff.api.errors.Errors;
import com.tinqinacademy.bff.api.operations.leavecomment.LeaveCommentOperation;
import com.tinqinacademy.bff.api.operations.leavecomment.LeaveCommentRequest;
import com.tinqinacademy.bff.api.operations.leavecomment.LeaveCommentResponse;
import com.tinqinacademy.comments.api.contracts.operations.leavecomment.LeaveCommentInput;
import com.tinqinacademy.comments.api.contracts.operations.leavecomment.LeaveCommentOutput;
import com.tinqinacademy.comments.restexport.CommentsClient;
import io.vavr.API;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LeaveCommentOperationProcessor extends BaseOperation implements LeaveCommentOperation {

    private final CommentsClient commentsClient;

    public LeaveCommentOperationProcessor(
            Validator validator,
            ConversionService conversionService,
            ErrorMapper errorMapper,
            CommentsClient commentsClient) {
        super(validator, conversionService, errorMapper);
        this.commentsClient = commentsClient;
    }

    @Override
    public Either<Errors, LeaveCommentResponse> process(LeaveCommentRequest request) {
        return Try.of(() -> {
                    log.info("Start leaving comment with request: {}", request);
                    validate(request);

                    LeaveCommentInput coreInput = convertToCoreInput(request);
                    LeaveCommentOutput coreOutput = commentsClient.leaveComment(coreInput.getRoomId(), coreInput);

                    LeaveCommentResponse result = convertToBffResponse(coreOutput);

                    log.info("Comment left successfully for room ID: {}", result.getRoomId());
                    return result;

                })
                .toEither()
                .mapLeft(throwable -> API.Match(throwable).of());
    }

    private LeaveCommentInput convertToCoreInput(LeaveCommentRequest request) {
        return LeaveCommentInput.builder()
                .roomId(request.getRoomId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .comment(request.getComment())
                .build();
    }

    private LeaveCommentResponse convertToBffResponse(LeaveCommentOutput output) {
        return LeaveCommentResponse.builder()
                .roomId(output.getRoomId())
                .comment(output.getComment())
                .publishedDate(output.getPublishedDate())
                .lastEditBy(output.getLastEditBy())
                .editedBy(output.getEditedBy())
                .build();
    }
}