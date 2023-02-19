package br.com.oblivion.group.commands;

import br.com.oblivion.api.commands.objects.Command;
import br.com.oblivion.group.commands.subcommands.*;
import org.bukkit.command.CommandSender;

public class GroupCommand extends Command {
    public GroupCommand() {
        super("group");
        setGroup("master");
        registerSubCommand(new GroupListSubCommand());
        registerSubCommand(new GroupSetSubCommand());
        registerSubCommand(new GroupRemoveSubCommand());
        registerSubCommand(new GroupResetSubCommand());
        registerSubCommand(new GroupInfoSubCommand());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sendHelpMessage(sender);
    }
}
