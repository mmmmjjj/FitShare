package com.fitshare.backend.common.exception;

import javax.persistence.EntityNotFoundException;

public class ExceedParticipantCountException extends RuntimeException {

    public ExceedParticipantCountException() {
        super("Exceed the number of participants");
    }
}
