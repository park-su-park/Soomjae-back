package com.parksupark.soomjae.server.common.exception;

import com.parksupark.soomjae.server.auth.common.exception.RefreshFailedException;
import com.parksupark.soomjae.server.auth.oauth.exception.OAuth2AuthenticationProcessingException;
import com.parksupark.soomjae.server.common.dto.ValidationErrorDetail;
import com.parksupark.soomjae.server.common.dto.ValidationErrorResponse;
import com.parksupark.soomjae.server.community.common.exception.AlreadyLikedException;
import com.parksupark.soomjae.server.community.common.exception.InvalidPostIdException;
import com.parksupark.soomjae.server.community.common.exception.InvalidPostTypeException;
import com.parksupark.soomjae.server.community.common.exception.LikeNotFoundException;
import com.parksupark.soomjae.server.member.exception.DuplicateEmailException;
import com.parksupark.soomjae.server.member.exception.MemberNotFoundException;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<String> handleMethodArgumentNotValidException(
        IllegalStateException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

    // 어떠한 Exception 발생하더라도 동작이 같다면 굳이 나눌 필요는 없어보이긴 함
    @ExceptionHandler({InvalidPostTypeException.class, InvalidPostIdException.class,
        LikeNotFoundException.class, AlreadyLikedException.class, MemberNotFoundException.class,
        DuplicateEmailException.class, ResourceNotFoundException.class})
    protected ResponseEntity<String> handleBadRequestException(
        RuntimeException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<String> handleDataIntegrityViolationException(
        DataIntegrityViolationException e) {

        return ResponseEntity.status(400)
            .body(ErrorMessages.DATA_INTEGRITY_VIOLATION_EXCEPTION_MESSAGE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ValidationErrorResponse> handleValidationException(
        MethodArgumentNotValidException e) {

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        List<ValidationErrorDetail> errors = fieldErrors.stream()
            .map(error -> new ValidationErrorDetail(
                error.getField(),
                error.getRejectedValue(),
                error.getDefaultMessage()
            ))
            .toList();

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
            ErrorMessages.VALIDATION_FAILED_MESSAGE, errors);

        return ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler(RefreshFailedException.class)
    protected ResponseEntity<String> handleRefreshFailedException(RuntimeException e) {

        return ResponseEntity.status(401)
            .header("Set-Cookie", "refresh_token=; Path=/; HttpOnly; Max-Age=0")
            .body(e.getMessage());
    }

    @ExceptionHandler(OAuth2AuthenticationProcessingException.class)
    protected ResponseEntity<String> handleOAuth2AuthenticationProcessingException(
        RuntimeException e) {

        return ResponseEntity.status(400)
            .body(e.getMessage());
    }
}
