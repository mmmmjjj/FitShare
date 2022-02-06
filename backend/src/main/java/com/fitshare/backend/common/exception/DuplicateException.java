package com.fitshare.backend.common.exception;

public class DuplicateException extends RuntimeException {

    public DuplicateException(Long id) {
        super(id + " Duplicate");
    }

    public DuplicateException(Long memberId, Long friendId) {
        super(memberId + " " + friendId + " Duplicate");
    }

}
