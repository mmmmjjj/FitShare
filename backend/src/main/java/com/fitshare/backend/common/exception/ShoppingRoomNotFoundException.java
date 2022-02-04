package com.fitshare.backend.common.exception;

import javax.persistence.EntityNotFoundException;

public class ShoppingRoomNotFoundException extends EntityNotFoundException {

    public ShoppingRoomNotFoundException(Long shoppingRoomId) {
        super(shoppingRoomId + " Shopping Room is not found");
    }
}
