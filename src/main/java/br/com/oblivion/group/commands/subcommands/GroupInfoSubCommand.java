package br.com.oblivion.group.commands.subcommands;

import br.com.oblivion.OblivionPlugin;
import br.com.oblivion.api.commands.objects.SubCommand;
import br.com.oblivion.api.utils.TimeUtils;
import br.com.oblivion.configs.group.Group;
import br.com.oblivion.group.database.ExpiredGroupUser;
import br.com.oblivion.group.database.GroupUser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.time.Duration;
import java.util.List;

public class GroupInfoSubCommand extends SubCommand {
    public GroupInfoSubCommand() {
        super("info");
        setSyntax("/group info <jogador>");
        setDescription("Ver as informações de grupo de um jogador.");
        setNumArgs(2);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        OfflinePlayer player = Bukkit.getServer().getOfflinePlayer(args[1]);

        if (!player.hasPlayedBefore()) {
            sender.sendMessage("§cJogador '" + args[1] + "' não foi encontrado.");
            return;
        }

        sender.sendMessage("§aGrupos ativos do jogador " + player.getName() + ".");

        List<GroupUser> userGroups = OblivionPlugin.getInstance().getDatabaseManager().getGroupUserTableManager().getUserGroups(player.getUniqueId());

        userGroups.forEach(userGroup -> {
            Group group = userGroup.getGroup();

            String durationMessage = "";
            if (userGroup.getDuration() != 0L) {
                durationMessage = " (" + TimeUtils.formatRemainingTime(userGroup.getDuration()).trim() + ") " +
                        "Expira em: " + TimeUtils.formatLocalDateTime(userGroup.getCreated().plus(Duration.ofMillis(userGroup.getDuration())));
            }
            sender.sendMessage("§a - " + group.replacedColor() + group.name + durationMessage);
        });

        List<ExpiredGroupUser> expiredGroupUsers = OblivionPlugin.getInstance().getExpiredGroupUserManager().getUserExpiredGroups(player.getUniqueId());

        if (expiredGroupUsers.size() == 0) return;

        sender.sendMessage("");
        sender.sendMessage("§aGrupos expirados do jogador " + player.getName() + ".");

        expiredGroupUsers.forEach(userGroup -> {
            Group group = userGroup.getGroup();

            String durationMessage = "";
            if (userGroup.getDuration() != 0L) {
                durationMessage = " (" + TimeUtils.formatRemainingTime(userGroup.getDuration()).trim() + ") " +
                        "Expirou em: " + TimeUtils.formatLocalDateTime(userGroup.getCreated());
            }
            sender.sendMessage("§a - " + group.replacedColor() + group.name + durationMessage);
        });

    }
}
