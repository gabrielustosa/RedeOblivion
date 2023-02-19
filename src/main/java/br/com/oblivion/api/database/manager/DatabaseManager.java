package br.com.oblivion.api.database.manager;

import br.com.oblivion.OblivionPlugin;
import br.com.oblivion.api.database.types.*;
import br.com.oblivion.configs.SQL;
import br.com.oblivion.group.managers.tablemanager.ExpiredGroupUserTableManager;
import br.com.oblivion.group.managers.tablemanager.GroupUserTableManager;
import com.j256.ormlite.field.DataPersisterManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.DatabaseTypeUtils;
import com.j256.ormlite.support.ConnectionSource;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.SQLException;

@Getter
public class DatabaseManager {

    @Getter(AccessLevel.NONE)
    private ConnectionSource connectionSource;

    private GroupUserTableManager groupUserTableManager;
    private ExpiredGroupUserTableManager expiredGroupUserTableManager;

    public void init() throws SQLException {
        SQL sqlConfig = OblivionPlugin.getInstance().getSql();
        String databaseURL = getDatabaseURL(sqlConfig);

        DataPersisterManager.registerDataPersisters(XMaterialType.getSingleton());
        DataPersisterManager.registerDataPersisters(LocationType.getSingleton());
        DataPersisterManager.registerDataPersisters(InventoryType.getSingleton());
        DataPersisterManager.registerDataPersisters(LocalDateTimeType.getSingleton());

        this.connectionSource = new JdbcConnectionSource(
                databaseURL,
                sqlConfig.username,
                sqlConfig.password,
                DatabaseTypeUtils.createDatabaseType(databaseURL)
        );

        this.groupUserTableManager = new GroupUserTableManager(connectionSource);
        this.expiredGroupUserTableManager = new ExpiredGroupUserTableManager(connectionSource);
    }

    /**
     * Database connection String used for establishing a connection.
     *
     * @return The database URL String
     */
    private @NotNull String getDatabaseURL(SQL sqlConfig) {
        switch (sqlConfig.driver) {
            case MYSQL:
                return "jdbc:" + sqlConfig.driver.name().toLowerCase() + "://" + sqlConfig.host + ":" + sqlConfig.port + "/" + sqlConfig.database + "?useSSL=" + sqlConfig.useSSL;
            case SQLITE:
                return "jdbc:sqlite:" + new File(OblivionPlugin.getInstance().getDataFolder(), sqlConfig.database + ".db");
            default:
                throw new UnsupportedOperationException("Unsupported driver (how did we get here?): " + sqlConfig.driver.name());
        }
    }

    public void closeConnection()  {
        try {
            this.connectionSource.close();
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§cErro ao tentar fechar a conexão.");
            e.printStackTrace();
        }
    }

}
