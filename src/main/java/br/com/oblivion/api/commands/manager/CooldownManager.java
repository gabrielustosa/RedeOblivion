package br.com.oblivion.api.commands.manager;

import br.com.oblivion.api.commands.objects.Command;
import br.com.oblivion.api.commands.objects.CommandCooldown;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CooldownManager {

    private static ArrayList<CommandCooldown> cooldowns = new ArrayList<>();

    public static boolean inCooldown(Player player, Command command) {
        if (command.getCooldown() == 0) return false;
        for (CommandCooldown cooldown : cooldowns) {
            if (cooldown.getPlayer().equals(player) && cooldown.getCommand().equals(command) && cooldown.getDuration() > System.currentTimeMillis()) {
                return true;
            }
        }
        return false;
    }

    public static void putInCooldown(Player player, Command command) {
        CommandCooldown cooldown = new CommandCooldown();
        cooldown.setCommand(command);
        cooldown.setPlayer(player);
        cooldown.setDuration(System.currentTimeMillis() + command.getCooldown());
        cooldowns.add(cooldown);
    }

    public static CommandCooldown getCooldown(Player player, Command command) {
        List<CommandCooldown> cooldownsList = cooldowns.stream().filter(c -> c.getPlayer().equals(player)).filter(c -> c.getCommand().equals(command)).collect(Collectors.toList());
        if (cooldownsList.size() <= 1) {
            return cooldownsList.size() == 1 ? cooldownsList.get(0) : null;
        }
        cooldowns.remove(cooldowns.size() - 2);
        return cooldowns.get(cooldowns.size() - 1);
    }
}
