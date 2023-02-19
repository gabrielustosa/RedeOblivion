package br.com.oblivion.group.commands.subcommands;

import br.com.oblivion.OblivionPlugin;
import br.com.oblivion.api.commands.objects.SubCommand;
import br.com.oblivion.configs.group.Group;
import br.com.oblivion.group.database.GroupUser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class GroupResetSubCommand extends SubCommand {
    public GroupResetSubCommand() {
        super("reset", "resetar");
        setSyntax("/group reset <jogador>");
        setDescription("Reseta todos os grupos de um jogador.");
        setNumArgs(2);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(args[1]);

        if (!player.hasPlayedBefore()) {
            sender.sendMessage("§cJogador '" + args[1] + "' não foi encontrado.");
            return;
        }

        GroupUser user = OblivionPlugin.getInstance().getGroupUserManager().getUser(player);
        Group defaultGroup = OblivionPlugin.getInstance().getGroupManager().getDefaultGroup();
        if (user.getGroup() == defaultGroup) {
            sender.sendMessage("§cEsse usuário não possui nenhum grupo.");
            return;
        }

        List<GroupUser> userGroups = OblivionPlugin.getInstance().getDatabaseManager().getGroupUserTableManager().getUserGroups(player.getUniqueId())
                .stream().filter(u -> u.getGroup() != defaultGroup).collect(Collectors.toList());

        OblivionPlugin.getInstance().getDatabaseManager().getGroupUserTableManager().delete(userGroups).join();
        OblivionPlugin.getInstance().getExpiredGroupUserManager().createExpiredGroup(userGroups);

        sender.sendMessage("§aVocê resetou os grupos do jogador " + user.getName());
    }
}
