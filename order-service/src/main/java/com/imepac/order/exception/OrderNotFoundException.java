package com.imepac.order.exception;

import com.imepac.commons.exception.EntityNotFoundException;

public class OrderNotFoundException extends EntityNotFoundException {

    public OrderNotFoundException(Long id) {
        super("Order", id);
    }
}
