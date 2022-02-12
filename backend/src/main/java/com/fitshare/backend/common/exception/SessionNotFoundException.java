package com.fitshare.backend.common.exception;

import javax.persistence.EntityNotFoundException;

public class SessionNotFoundException extends EntityNotFoundException {

    public SessionNotFoundException(String sessionId) {
        super(sessionId + " Session is not Found");
    }
}