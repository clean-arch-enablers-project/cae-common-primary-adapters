package com.cae.command_controller;

import com.cae.mapped_exceptions.specifics.InputMappedException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
@Getter
public class CommandRequest {

    private final String[] rawArgs;
    private String name;
    private String rawParameters;

    public static CommandRequest of(String[] args){
        return CommandRequest.builder()
                .rawArgs(args)
                .build();
    }

    protected void mapProperties(){
        var argsList = List.of(this.rawArgs);
        if (argsList.isEmpty())
            throw new InputMappedException("no command provided");
        this.name = argsList.get(0);
        this.rawParameters = CommandRequest.getRawParametersFrom(argsList);
    }

    private static String getRawParametersFrom(List<String> argsList) {
        return argsList.subList(1, argsList.size())
                .stream()
                .reduce("", (previous, next) -> previous.concat(" ").concat(next));
    }

}
