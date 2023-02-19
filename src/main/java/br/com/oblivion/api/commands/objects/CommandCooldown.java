package br.com.oblivion.api.commands.objects;

import org.bukkit.entity.Player;

public class CommandCooldown {

    private Player player;
    private Command command;
    private long duration;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }
}
