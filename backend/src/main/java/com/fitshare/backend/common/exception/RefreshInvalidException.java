package com.fitshare.backend.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RefreshInvalidException extends RuntimeException {

    public RefreshInvalidException() { super("RefreshToken is invalid");}
}
