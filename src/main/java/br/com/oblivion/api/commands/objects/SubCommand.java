package br.com.oblivion.api.commands.objects;

import br.com.oblivion.api.commands.CommandBase;
import org.bukkit.command.CommandSender;


public abstract class SubCommand extends CommandBase {


    public SubCommand(String command, String... aliases) {
        super(command, aliases);
    }

    public abstract void execute(CommandSender sender, String[] args);

}
