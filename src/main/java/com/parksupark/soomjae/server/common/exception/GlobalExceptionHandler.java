package com.parksupark.soomjae.server.common.exception;

import com.parksupark.soomjae.server.community.common.exception.AlreadyLikedException;
import com.parksupark.soomjae.server.community.common.exception.InvalidPostIdException;
import com.parksupark.soomjae.server.community.common.exception.InvalidPostTypeException;
import com.parksupark.soomjae.server.community.common.exception.LikeNotFoundException;
import com.parksupark.soomjae.server.member.exception.DuplicateEmailException;
import com.parksupark.soomjae.server.member.exception.MemberNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
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
        DuplicateEmailException.class})
    protected ResponseEntity<String> handleInvalidPostTypeException(
        RuntimeException e) {

        return ResponseEntity.status(400).body(e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<String> handleDataIntegrityViolationException(
        DataIntegrityViolationException e) {

        return ResponseEntity.status(400)
            .body(ErrorMessages.DATA_INTEGRITY_VIOLATION_EXCEPTION_MESSAGE);
    }
}
