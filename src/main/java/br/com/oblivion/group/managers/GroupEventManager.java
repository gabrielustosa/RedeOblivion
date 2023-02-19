package br.com.oblivion.group.managers;

import br.com.oblivion.group.events.PlayerGroupEvent;
import org.bukkit.Bukkit;

import java.util.UUID;

public class GroupEventManager {

    public static void callPlayerGroupEvent(UUID uuid, PlayerGroupEvent.GroupAction action) {
        PlayerGroupEvent event = new PlayerGroupEvent(Bukkit.getOfflinePlayer(uuid), action);
        Bukkit.getPluginManager().callEvent(event);
    }
}
