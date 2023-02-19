package br.com.oblivion.group.listeners;

import br.com.oblivion.OblivionPlugin;
import br.com.oblivion.group.database.GroupUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {
        GroupUser user = OblivionPlugin.getInstance().getGroupUserManager().getUser(event.getUniqueId(), event.getName());

        user.setName(event.getName());
    }
}
