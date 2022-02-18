package com.fitshare.backend.common.exception;

import javax.persistence.EntityNotFoundException;

public class MemberNotFoundException extends EntityNotFoundException {

    public MemberNotFoundException(Long memberId) {
        super(memberId + " Member is not Found");
    }
}