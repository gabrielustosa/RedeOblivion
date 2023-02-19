package br.com.oblivion.group.commands.subcommands;

import br.com.oblivion.OblivionPlugin;
import br.com.oblivion.api.commands.objects.SubCommand;
import br.com.oblivion.configs.group.Group;
import br.com.oblivion.group.database.GroupUser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.List;

public class GroupRemoveSubCommand extends SubCommand {
    public GroupRemoveSubCommand() {
        super("remove", "delete");
        setSyntax("/group remove <jogador> <grupo>");
        setDescription("Remove um grupo de um jogador.");
        setNumArgs(3);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(args[1]);

        if (!player.hasPlayedBefore()) {
            sender.sendMessage("§cJogador '" + args[1] + "' não foi encontrado.");
            return;
        }

        Group group = OblivionPlugin.getInstance().getGroupManager().getGroup(args[2]);
        if (group == null) {
            sender.sendMessage("§cGrupo '" + args[2] + "' não foi encontrado.");
            return;
        }

        if (group.isDefault) {
            sender.sendMessage("§cVocê não pode remover o grupo padrão.");
            return;
        }

        List<GroupUser> userGroups = OblivionPlugin.getInstance().getDatabaseManager().getGroupUserTableManager().getUserGroups(player.getUniqueId());
        GroupUser toRemove = userGroups.stream().filter(u -> u.getGroup() == group).findFirst().orElse(null);

        if (toRemove == null) {
            sender.sendMessage("§cEsse jogador não tem esse grupo.");
            return;
        }

        OblivionPlugin.getInstance().getDatabaseManager().getGroupUserTableManager().delete(toRemove).join();
        OblivionPlugin.getInstance().getExpiredGroupUserManager().createExpiredGroup(toRemove);

        sender.sendMessage("§aVocê removeu o grupo " + group.replacedColor() + group.name + " §ado jogador " + toRemove.getName());
    }
}
