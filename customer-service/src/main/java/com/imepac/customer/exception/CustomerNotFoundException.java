package com.imepac.customer.exception;

import com.imepac.commons.exception.EntityNotFoundException;

public class CustomerNotFoundException extends EntityNotFoundException {

    public CustomerNotFoundException(Long id) {
        super("Customer", id);
    }

    public CustomerNotFoundException(String email) {
        super("Customer not found with email: " + email);
    }
}
