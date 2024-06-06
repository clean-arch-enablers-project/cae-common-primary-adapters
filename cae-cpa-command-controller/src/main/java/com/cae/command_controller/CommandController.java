package com.cae.command_controller;

import com.cae.command_controller.exit_codes.ExitCodeRepository;
import com.cae.mapped_exceptions.specifics.InputMappedException;
import com.cae.mapped_exceptions.specifics.InternalMappedException;
import com.cae.command_controller.exceptions.CommandNotFound;
import com.cae.command_controller.exceptions.ResourceNotFound;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommandController {

    public static void serve(CommandRequest commandRequest){
        try{
            commandRequest.mapProperties();
            CommandController.handle(commandRequest);
            CommandController.emitSuccess();
        } catch (InputMappedException mappedException){
            CommandMessageHandler.provideErrorMessage(mappedException.getMessage());
            CommandController.emitInputProblem();
        } catch (CommandNotFound commandNotFound){
            CommandMessageHandler.provideErrorMessage(commandNotFound.getMessage());
            CommandController.emitCommandNotFound();
        } catch (ResourceNotFound resourceNotFound){
            var message = "Some required resource was not found: " + resourceNotFound.getMessage();
            CommandMessageHandler.provideErrorMessage(message);
            CommandController.emitResourceNotFound();
        } catch (InternalMappedException internalMappedException){
            CommandMessageHandler.provideErrorMessage(internalMappedException.getMessage());
            CommandController.emitInternalProblem();
        } catch (Exception genericException){
            var message = "Something went unexpectedly wrong: " + genericException;
            CommandMessageHandler.provideErrorMessage(message);
            CommandController.emitInternalProblem();
        }
    }

    private static void handle(CommandRequest commandRequest){
        var commandToExecute = CommandController.getCommandToExecute(commandRequest);
        commandToExecute.execute(commandRequest);
    }

    private static Command getCommandToExecute(CommandRequest commandRequest) {
        return CommandRepository.SINGLETON.findCommandByName(commandRequest.getName())
                .orElseThrow(() -> new CommandNotFound("command '" + commandRequest.getName() + "' was not found."));
    }

    private static void emitSuccess(){
        System.exit(ExitCodeRepository.SUCCESS.getCode());
    }

    private static void emitInternalProblem(){
        System.exit(ExitCodeRepository.INTERNAL_ERROR.getCode());
    }

    private static void emitInputProblem(){
        System.exit(ExitCodeRepository.INPUT_ERROR.getCode());
    }

    private static void emitCommandNotFound(){
        System.exit(ExitCodeRepository.COMMAND_NOT_FOUND.getCode());
    }

    private static void emitResourceNotFound(){
        System.exit(ExitCodeRepository.RESOURCE_NOT_FOUND.getCode());
    }



}