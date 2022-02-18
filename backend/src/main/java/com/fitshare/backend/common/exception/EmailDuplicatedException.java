package com.fitshare.backend.common.exception;

import com.fitshare.backend.common.exception.handler.ErrorCode;

public class EmailDuplicatedException extends BusinessException {

    public EmailDuplicatedException() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }

    public EmailDuplicatedException(String message) {
        super(message);
    }
}
