package br.com.oblivion.group.managers.tablemanager;

import br.com.oblivion.group.database.ExpiredGroupUser;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ExpiredGroupUserTableManager {

    private final Dao<ExpiredGroupUser, Integer> dao;
    private final ConnectionSource connectionSource;

    public ExpiredGroupUserTableManager(ConnectionSource connectionSource) throws SQLException {
        this.connectionSource = connectionSource;
        TableUtils.createTableIfNotExists(connectionSource, ExpiredGroupUser.class);
        this.dao = DaoManager.createDao(connectionSource, ExpiredGroupUser.class);
    }

    public void create(ExpiredGroupUser expiredGroupUser) {
        CompletableFuture.runAsync(() -> {
            try {
                this.dao.create(expiredGroupUser);
                this.dao.commit(getDatabaseConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void create(List<ExpiredGroupUser> expiredGroupsUsers) {
        CompletableFuture.runAsync(() -> {
            try {
                this.dao.create(expiredGroupsUsers);
                this.dao.commit(getDatabaseConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<List<ExpiredGroupUser>> list(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                QueryBuilder<ExpiredGroupUser, Integer> queryBuilder = this.dao.queryBuilder();
                queryBuilder.where().eq("player_uuid", uuid);
                return queryBuilder.query();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return Collections.emptyList();
        });
    }

    private DatabaseConnection getDatabaseConnection() throws SQLException {
        return connectionSource.getReadWriteConnection(null);
    }

}
