package br.com.oblivion.group.managers;

import br.com.oblivion.OblivionPlugin;
import br.com.oblivion.group.database.GroupUser;
import br.com.oblivion.group.listeners.PlayerJoinListener;
import br.com.oblivion.group.listeners.PlayerUpdateTagGroup;
import br.com.oblivion.group.tasks.GroupExpireTask;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;


public class GroupUserManager {

    public GroupUserManager() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(OblivionPlugin.getInstance(), new GroupExpireTask(), 0, 20 * 60 * 5);

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), OblivionPlugin.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerUpdateTagGroup(), OblivionPlugin.getInstance());
    }

    public @NotNull GroupUser getUser(@NotNull UUID playerUuid, String playerName) {
        Optional<GroupUser> userOptional = OblivionPlugin.getInstance().getDatabaseManager().getGroupUserTableManager().getUser(playerUuid);
        return userOptional.orElseGet(() -> OblivionPlugin.getInstance().getDatabaseManager().getGroupUserTableManager().getUserOrCreate(playerUuid, playerName));
    }

    public @NotNull GroupUser getUser(@NotNull OfflinePlayer offlinePlayer) {
        return getUser(offlinePlayer.getUniqueId(), offlinePlayer.getName());
    }

}
