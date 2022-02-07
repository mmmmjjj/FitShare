package com.fitshare.backend.common.exception.handler;

import com.fitshare.backend.common.exception.BusinessException;
import com.fitshare.backend.common.exception.DuplicateException;
import com.fitshare.backend.common.exception.InvalidException;
import com.fitshare.backend.common.model.BaseResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> duplicateException(DuplicateException duplicateException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(HttpStatus.BAD_REQUEST.value(),duplicateException.getMessage()));
    }

    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<ErrorResponse> invalidException(InvalidException invalidException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(HttpStatus.BAD_REQUEST.value(),invalidException.getMessage()));
    }

    /**
     * Valid 어노테이션 BindingException 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult()));
    }

    /**
     * Entity Not Found Exception 처리
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> entityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND.getStatus(), e.getMessage()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> businessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode));
    }
}
