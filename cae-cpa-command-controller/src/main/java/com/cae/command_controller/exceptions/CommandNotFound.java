package com.cae.command_controller.exceptions;

import com.cae.mapped_exceptions.specifics.NotFoundMappedException;

public class CommandNotFound extends NotFoundMappedException {

    public CommandNotFound(String message) {
        super(message);
    }
}
