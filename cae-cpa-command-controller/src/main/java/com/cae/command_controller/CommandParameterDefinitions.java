package com.cae.command_controller;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class CommandParameterDefinitions {

    private final boolean required;
    private final boolean flag;
    private boolean present;
    private String actualValue;

    public static CommandParameterDefinitions newRequiredDefaultParameter(){
        return CommandParameterDefinitions.builder()
                .required(true)
                .flag(false)
                .present(false)
                .actualValue(null)
                .build();
    }

    public static CommandParameterDefinitions newOptionalDefaultParameter(){
        return CommandParameterDefinitions.builder()
                .required(false)
                .flag(false)
                .present(false)
                .actualValue(null)
                .build();
    }

    public static CommandParameterDefinitions newRequiredFlagParameter(){
        return CommandParameterDefinitions.builder()
                .required(true)
                .flag(true)
                .present(false)
                .actualValue(null)
                .build();
    }

    public static CommandParameterDefinitions newOptionalFlagParameter(){
        return CommandParameterDefinitions.builder()
                .required(false)
                .flag(true)
                .present(false)
                .actualValue(null)
                .build();
    }

}
