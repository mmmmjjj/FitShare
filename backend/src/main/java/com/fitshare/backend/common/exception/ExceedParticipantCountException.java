package com.fitshare.backend.common.exception;

import com.fitshare.backend.common.exception.handler.ErrorCode;

public class ExceedParticipantCountException extends BusinessException {

    public ExceedParticipantCountException() {
        super(ErrorCode.EXCEED_PARTICIPANT_COUNT);
    }

    private ExceedParticipantCountException(String message) {
        super(message);
    }
}
