package br.com.oblivion.group.events;

import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerGroupEvent extends Event {

    private OfflinePlayer player;
    private GroupAction action;


    public PlayerGroupEvent(OfflinePlayer player, GroupAction action) {
        this.player = player;
        this.action = action;
    }

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum GroupAction {
        CREATE, DELETE
    }
}
