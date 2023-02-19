package br.com.oblivion.group.commands.subcommands;

import br.com.oblivion.OblivionPlugin;
import br.com.oblivion.api.commands.objects.SubCommand;
import br.com.oblivion.api.utils.TimeUtils;
import br.com.oblivion.configs.group.Group;
import br.com.oblivion.group.database.GroupUser;
import br.com.oblivion.group.managers.tablemanager.GroupUserTableManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class GroupSetSubCommand extends SubCommand {

    public GroupSetSubCommand() {
        super("set", "give");
        setSyntax("/group set <jogador> <grupo> (duração)");
        setDescription("Seta um grupo para um jogador onde a duração é opcional.");
        setNumArgs(3, 4);
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

        GroupUser user = OblivionPlugin.getInstance().getGroupUserManager().getUser(player);
        System.out.println(user.getGroup().name);
        if (user.getGroups().contains(group)) {
            sender.sendMessage("§cEsse jogador já tem esse grupo.");
            return;
        }

        long duration = 0;
        if (args.length == 4) {
            String durationString = args[3];
            if (!group.isVip) {
                sender.sendMessage("§cApenas VIPs podem ter duração.");
                return;
            }
            long period = TimeUtils.parsePeriod(durationString);
            char periodChar = durationString.toLowerCase().charAt(durationString.length() - 1);
            if (period == 0 || !Arrays.asList("h", "d", "m").contains(String.valueOf(periodChar))) {
                sender.sendMessage("§cDuração invalida, use [1h, 1d, 1m].");
                return;
            }
            duration = period;
        }
        String timeMessage = duration == 0 ? "" : " por " + TimeUtils.formatRemainingTime(duration).trim();
        sender.sendMessage("§aVocê setou o grupo " + group.replacedColor() + group.name + " §apara o jogador " + user.getName() + timeMessage + ".");


        GroupUserTableManager tableManager = OblivionPlugin.getInstance().getDatabaseManager().getGroupUserTableManager();

        GroupUser newUser = new GroupUser(user.getUuid(), user.getName(), group, duration);
        tableManager.create(newUser);

        // update cache
        if (group.priority < user.getGroup().priority) {
            tableManager.removeEntry(user);
            tableManager.addEntry(newUser);
            newUser.getGroups().addAll(user.getGroups());
        } else {
            user.getGroups().add(group);
        }

    }
}
