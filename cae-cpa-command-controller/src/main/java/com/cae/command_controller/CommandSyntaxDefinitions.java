package com.cae.command_controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandSyntaxDefinitions {

    public static final String PARAMETER_KEY = "--";

    public static final String EXAMPLE = "${tool-name} <command-name> [" + PARAMETER_KEY + "parameter-name parameter-value]";

}
