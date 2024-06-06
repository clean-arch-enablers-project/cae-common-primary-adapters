package com.cae.command_controller;

import com.cae.command_controller.default_commands.ListAllCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandRepository {

    private CommandRepository(){
        this.registerNewCommand(new ListAllCommand());
    }

    public static final CommandRepository SINGLETON = new CommandRepository();

    private final List<Command> availableCommands = new ArrayList<>();

    public List<Command> retrieveAll(){
        return this.availableCommands;
    }

    public void registerNewCommand(Command command){
        this.availableCommands.add(command);
    }

    public void registerNewCommands(List<Command> commands){
        this.availableCommands.addAll(commands);
    }

    public Optional<Command> findCommandByName(String name){
        return this.retrieveAll()
                .stream()
                .filter(command -> command.getName().equals(name))
                .findFirst();
    }

}
