package br.com.oblivion;

import br.com.oblivion.api.commands.manager.CommandManager;
import br.com.oblivion.api.database.manager.DatabaseManager;
import br.com.oblivion.configs.group.Groups;
import br.com.oblivion.configs.SQL;
import br.com.oblivion.group.managers.ExpiredGroupUserManager;
import br.com.oblivion.group.managers.GroupManager;
import br.com.oblivion.group.managers.GroupUserManager;
import com.iridium.iridiumcore.IridiumCore;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.sql.SQLException;

@Getter
public final class OblivionPlugin extends IridiumCore {
    private static OblivionPlugin instance;

    private SQL sql;
    private Groups groupConfig;

    private CommandManager commandManager;
    private DatabaseManager databaseManager;
    private GroupManager groupManager;
    private GroupUserManager groupUserManager;
    private ExpiredGroupUserManager expiredGroupUserManager;


    @Override
    public void onEnable() {
        instance = this;

        this.commandManager = new CommandManager(this);
        this.databaseManager = new DatabaseManager();
        this.groupManager = new GroupManager();
        this.groupUserManager = new GroupUserManager();
        this.expiredGroupUserManager = new ExpiredGroupUserManager();

        try {
            databaseManager.init();
        } catch (SQLException exception) {
            exception.printStackTrace();
            databaseManager.closeConnection();
            Bukkit.getPluginManager().disablePlugin(this);
        }

        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        databaseManager.closeConnection();
    }

    @Override
    public void loadConfigs() {
        this.sql = getPersist().load(SQL.class);
        this.groupConfig = getPersist().load(Groups.class);
    }

    @Override
    public void saveConfigs() {
        getPersist().save(sql);
        getPersist().save(groupConfig);
    }

    @Override
    public void saveData() {
        getDatabaseManager().getGroupUserTableManager().save();
    }

    public static OblivionPlugin getInstance() {
        return instance;
    }
}
