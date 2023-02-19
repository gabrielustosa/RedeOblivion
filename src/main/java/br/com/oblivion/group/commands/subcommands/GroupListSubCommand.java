package br.com.oblivion.group.commands.subcommands;

import br.com.oblivion.OblivionPlugin;
import br.com.oblivion.api.commands.objects.SubCommand;
import org.bukkit.command.CommandSender;

public class GroupListSubCommand extends SubCommand {
    public GroupListSubCommand() {
        super("list", "listar");
        setSyntax("/group list");
        setDescription("Lista todos os grupos disponíveis.");
        setNumArgs(1);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("§aOs grupos disponíveis são: ");
        sender.sendMessage("");
        OblivionPlugin.getInstance().getGroupConfig().groups.forEach(group -> {
            sender.sendMessage("§a- " + group.replacedColor() + group.name);
        });
    }
}
