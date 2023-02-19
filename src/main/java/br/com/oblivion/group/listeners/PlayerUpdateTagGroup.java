package br.com.oblivion.group.listeners;

import br.com.oblivion.group.events.PlayerGroupEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerUpdateTagGroup implements Listener {

    @EventHandler
    public void onPlayerUpdateTagGroup(PlayerGroupEvent event) {
        System.out.println(event.getPlayer().getUniqueId());
        System.out.println(event.getAction().toString());
    }
}
