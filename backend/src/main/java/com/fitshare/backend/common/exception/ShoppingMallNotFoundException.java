package com.fitshare.backend.common.exception;

import javax.persistence.EntityNotFoundException;

public class ShoppingMallNotFoundException extends EntityNotFoundException {

    public ShoppingMallNotFoundException(Long shoppingMallId) {
        super(shoppingMallId + " Shopping Mall is not found");
    }
}
