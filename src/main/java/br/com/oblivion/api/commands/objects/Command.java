package br.com.oblivion.api.commands.objects;

import br.com.oblivion.OblivionPlugin;
import br.com.oblivion.api.commands.CommandBase;
import br.com.oblivion.configs.group.Group;
import br.com.oblivion.group.database.GroupUser;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Command extends CommandBase implements CommandExecutor, TabCompleter {


    public Command(String command, String... aliases) {
        super(command, aliases);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, String[] args) {
        CommandBase currentCommand = this;
        if (args.length > 0 && getSubCommands().size() > 0) {
            SubCommand subCommand = getArgsLastCommand(args);
            if (subCommand == null) return true;
            currentCommand = subCommand;
        }

        if (currentCommand.getGroup() != null && sender instanceof Player) {
            GroupUser groupUser = OblivionPlugin.getInstance().getGroupUserManager().getUser((Player) sender);
            Group group = OblivionPlugin.getInstance().getGroupManager().getGroup(currentCommand.getGroup());
            if (!groupUser.getGroups().contains(group)) {
                sender.sendMessage("§cVocê precisa ser do grupo " + group.replacedColor() + group.name + " §cou superior para executar esse comando.");
                return true;
            }
        }
        if (!validateArgs(args, currentCommand.getNumArgs())) {
            sender.sendMessage("§cUse: " + currentCommand.getSyntax());
            return true;
        }

        currentCommand.execute(sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, String[] args) {
        return onTabComplete(sender, args);
    }

    public void execute(CommandSender sender, String[] args) {

    }

    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return super.onTabComplete(sender, args);
        } else {
            SubCommand subCommand = getArgsLastCommand(Arrays.copyOfRange(args, 0, args.length - 1));
            if (subCommand == null) return new ArrayList<>();
            return subCommand.onTabComplete(sender, args);
        }
    }

    public SubCommand getArgsLastCommand(String[] args) {
        SubCommand subCommand = getSubCommand(args[0]);
        if (subCommand == null) return null;

        for (String arg : Arrays.asList(args).subList(1, args.length)) {
            SubCommand subSubCommand = subCommand.getSubCommand(arg);
            if (subSubCommand != null) subCommand = subSubCommand;
        }
        return subCommand;
    }

    public boolean validateArgs(Object[] args, List<Integer> numArgs) {
        return numArgs.size() == 0 || numArgs.stream().anyMatch(num -> args.length == num);
    }

}
