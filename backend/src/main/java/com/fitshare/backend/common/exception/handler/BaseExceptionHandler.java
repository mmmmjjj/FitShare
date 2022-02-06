package com.fitshare.backend.common.exception.handler;

import com.fitshare.backend.common.exception.DuplicateException;
import com.fitshare.backend.common.exception.InvalidException;
import com.fitshare.backend.common.model.BaseResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(DuplicateException.class)
    public BaseResponseBody duplicateException(DuplicateException duplicateException) {
        return BaseResponseBody.of(HttpStatus.OK, duplicateException.getMessage());
    }

    @ExceptionHandler(InvalidException.class)
    public BaseResponseBody invalidException(InvalidException invalidException) {
        return BaseResponseBody.of(HttpStatus.OK, invalidException.getMessage());
    }

}
