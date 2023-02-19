package br.com.oblivion.api.commands;

import br.com.oblivion.OblivionPlugin;
import br.com.oblivion.api.commands.objects.SubCommand;
import br.com.oblivion.configs.group.Group;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class CommandBase {

    public CommandBase(String command, String... aliases) {
        setCommand(command);
        setAliases(Arrays.asList(aliases));
    }

    private String command;
    private List<String> aliases = new ArrayList<>();
    private List<SubCommand> subCommands = new ArrayList<>();
    private String syntax;

    private String description;
    private SubCommand defaultSubCommand;
    private List<Integer> numArgs = new ArrayList<>();

    private String group;
    private long cooldown;

    public SubCommand getSubCommand(String subCommandName) {
        for (SubCommand sub : subCommands) {
            if (sub.getCommand().equalsIgnoreCase(subCommandName)) return sub;
            for (String alias : sub.getAliases()) {
                if (alias.equalsIgnoreCase(subCommandName)) return sub;
            }
        }
        return defaultSubCommand;
    }

    public void registerSubCommand(SubCommand sub) {
        subCommands.add(sub);
        if (sub.getGroup() == null) {
            sub.setGroup(getGroup());
        }
    }

    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        for (SubCommand subCommand : getSubCommands()) {
            completions.add(subCommand.getCommand());
            completions.addAll(subCommand.getAliases());
        }
        return completions;
    }

    public void execute(CommandSender sender, String[] args) {

    }

    public void sendHelpMessage(CommandSender sender) {
        sender.sendMessage("§aOs subcomandos disponíveis para '" + getCommand() + "' são:");
        for (SubCommand sub : getSubCommands()) {
            sender.sendMessage("§e" + sub.getSyntax() + " - " + sub.getDescription());
        }
    }

    public void setNumArgs(Integer... args) {
        this.numArgs = Arrays.asList(args);
    }

}
