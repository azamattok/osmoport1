package com.space.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShipNotFoundException extends RuntimeException {

    public ShipNotFoundException(String id) {
        super("Ship by ID: " + id + " not found.");
    }
}
