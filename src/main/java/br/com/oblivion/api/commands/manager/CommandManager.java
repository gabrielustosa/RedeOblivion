package br.com.oblivion.api.commands.manager;

import br.com.oblivion.OblivionPlugin;
import br.com.oblivion.api.commands.objects.Command;

import java.util.HashMap;

public class CommandManager {

    private OblivionPlugin instance;


    public CommandManager(OblivionPlugin instance) {
        this.instance = instance;
    }

    private HashMap<String, Command> commands = new HashMap<>();

    public HashMap<String, Command> getCommands() {
        return commands;
    }


    public void registerCommand(Command command) {
        if (!getCommands().containsValue(command)) {
            getCommands().put(command.getCommand(), command);
            instance.getCommand(command.getCommand()).setExecutor(command);
            instance.getCommand(command.getCommand()).setTabCompleter(command);
        }
    }


}
