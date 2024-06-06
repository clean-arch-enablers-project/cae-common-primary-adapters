package com.cae.command_controller.exceptions;

import com.cae.mapped_exceptions.specifics.NotFoundMappedException;

public class ResourceNotFound extends NotFoundMappedException {

    public ResourceNotFound(String message) {
        super(message);
    }
}
