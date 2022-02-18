package com.fitshare.backend.common.exception;

public class InvalidException extends RuntimeException {

    public InvalidException(Long id) {
        super(id + " Invalid");
    }

    public InvalidException(Long memberId, Long friendId) {
        super(memberId + " " + friendId + " Invalid");
    }

}
