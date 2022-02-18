package com.fitshare.backend.common.exception;

public class ParticipantNotFoundException extends RuntimeException {
    public ParticipantNotFoundException(Long memberId, Long shoppingRoomId) {
        super(memberId + " Member does not participant " + shoppingRoomId + " shopping room ");
    }
}
