package com.cae.command_controller;

import com.cae.mapped_exceptions.specifics.InputMappedException;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class Command {

    private final String name;
    private final Map<String, CommandParameterDefinitions> commandParameters = new HashMap<>();

    protected Command(String name) {
        this.name = name;
    }

    public void execute(CommandRequest commandRequest){
        this.extractActualParameterValuesFrom(commandRequest);
        this.runParameterValidations();
        this.applyInternalLogic();
    }

    private void extractActualParameterValuesFrom(CommandRequest commandRequest) {
        var parametersAndTheirValues = Arrays.stream(commandRequest.getRawParameters()
                .split(CommandSyntaxDefinitions.PARAMETER_KEY))
                .filter(chunk -> !chunk.isBlank())
                .collect(Collectors.toList());
        if (!parametersAndTheirValues.isEmpty()){
            parametersAndTheirValues.forEach(parameterAndItsValue -> {
                var chunks = List.of(parameterAndItsValue.split(" "));
                var parameterKey = chunks.get(0);
                var parameterValues =chunks.subList(1, chunks.size());
                var parameterDefinitionsSearchResult = this.getParameterDefinitionsByKey(parameterKey);
                if (parameterDefinitionsSearchResult.isPresent()){
                    var parameterDefinitions = parameterDefinitionsSearchResult.get();
                    parameterDefinitions.setPresent(true);
                    if (!parameterDefinitions.isFlag() && !parameterValues.isEmpty())
                        parameterDefinitions.setActualValue(parameterValues.get(0));
                }
                else
                    CommandMessageHandler.provideWarningMessage("no parameter named '" + parameterKey + "' for the '" + this.getName() + "' command.");
            });
        }
    }

    private Optional<CommandParameterDefinitions> getParameterDefinitionsByKey(String parameterKey){
        return Optional.ofNullable(this.commandParameters.get(parameterKey));
    }

    private void runParameterValidations() {
        var errorTracking = new InputValidationErrorTracking();
        this.commandParameters.forEach((key, definitions) -> {
            if (definitions.isRequired() && !definitions.isPresent())
                errorTracking.registerError(("    | ---- the '").concat(key).concat("' parameter must be provided.\n"));
            if (((!definitions.isFlag()) && definitions.isPresent()) && definitions.getActualValue() == null)
                errorTracking.registerError(("    | ---- the '").concat(key).concat("' parameter is not a flag. Please provide a value for it.\n"));
        });
        if (errorTracking.count > 0)
            throw new InputMappedException(
                    "The input validation failed (" + errorTracking.count + " problem" + (errorTracking.count > 1? "s" : "") + " found): \n" + errorTracking.errorsBuilder
            );
    }

    protected abstract void applyInternalLogic();

    public Command registerParameter(String parameterKey, CommandParameterDefinitions definitions){
        this.commandParameters.put(parameterKey, definitions);
        return this;
    }

    static class InputValidationErrorTracking{
        private final StringBuilder errorsBuilder = new StringBuilder();
        private Integer count = 0;

        public void registerError(String message){
            this.errorsBuilder.append(message);
            this.count ++;
        }

    }

}
