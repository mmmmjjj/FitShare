package com.fitshare.backend.common.exception;

import com.fitshare.backend.common.exception.handler.ErrorCode;

import static com.fitshare.backend.common.exception.handler.ErrorCode.IMAGE_NOT_FOUND;

public class ImageFailedException extends BusinessException {

    public ImageFailedException(String message) {
        super(message);
    }

    public ImageFailedException() {
        super(IMAGE_NOT_FOUND);
    }
}
