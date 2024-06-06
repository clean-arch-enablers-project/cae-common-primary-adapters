package com.cae.command_controller.default_commands;

import com.cae.command_controller.*;

public class ListAllCommand extends Command {

    public ListAllCommand() {
        super("ls");
    }

    @Override
    protected void applyInternalLogic() {
        var commands = CommandRepository.SINGLETON.retrieveAll()
                    .stream()
                    .map(command -> "    | ---- <" + command.getName() + "> " + this.getParametersFrom(command) + "\n")
                    .reduce("", String::concat);
        CommandMessageHandler.provideInfoMessage("all commands available (" + CommandRepository.SINGLETON.retrieveAll().size() + " registered): \n" + commands);
    }

    private String getParametersFrom(Command command) {
        if (command.getCommandParameters().isEmpty())
            return "";
        var stringBuilder = new StringBuilder("[");
        command.getCommandParameters().forEach((key, definitions) -> {
            stringBuilder.append(" ")
                    .append(CommandSyntaxDefinitions.PARAMETER_KEY)
                    .append(key)
                    .append(definitions.isFlag()? " (flag: " : " (value: ")
                    .append(this.checkWhetherRequired(definitions));
        });
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private String checkWhetherRequired(CommandParameterDefinitions definitions) {
        return (definitions.isRequired()? "required) " : "optional)");
    }
}
