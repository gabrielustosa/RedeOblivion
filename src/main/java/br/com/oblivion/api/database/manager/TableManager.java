package br.com.oblivion.api.database.manager;

import br.com.oblivion.api.database.DatabaseObject;
import com.iridium.iridiumcore.utils.SortedList;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.support.DatabaseConnection;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class TableManager<T extends DatabaseObject, S> {

    private final SortedList<T> entries;
    private final Dao<T, S> dao;
    private final Comparator<T> comparator;

    private final ConnectionSource connectionSource;

    public TableManager(ConnectionSource connectionSource, Class<T> clazz, Comparator<T> comparator) throws SQLException {
        this.connectionSource = connectionSource;
        this.comparator = comparator;
        this.entries = new SortedList<>(comparator);
        TableUtils.createTableIfNotExists(connectionSource, clazz);
        this.dao = DaoManager.createDao(connectionSource, clazz);
        this.dao.setAutoCommit(getDatabaseConnection(), false);
    }


    public void save() {
        try {
            List<T> entryList = new ArrayList<>(entries);
            for (T t : entryList) {
                if (!t.isChanged()) continue;
                this.dao.createOrUpdate(t);
                t.setChanged(false);
            }
            this.dao.commit(getDatabaseConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addEntry(T t) {
        entries.add(t);
    }

    public List<T> getEntries() {
        return entries;
    }

    public Optional<T> getEntry(T t) {
        int index = Collections.binarySearch(getEntries(), t, comparator);
        if (index < 0) return Optional.empty();
        return Optional.of(getEntries().get(index));
    }

    public void create(T t) {
        CompletableFuture.runAsync(() -> {
            try {
                this.dao.create(t);
                this.dao.commit(getDatabaseConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void removeEntry(T t) {
        entries.remove(t);
    }

    public CompletableFuture<Void> delete(T t) {
        removeEntry(t);
        return CompletableFuture.runAsync(() -> {
            try {
                dao.delete(t);
                dao.commit(getDatabaseConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<Void> delete(Collection<T> t) {
        entries.removeAll(t);
        return CompletableFuture.runAsync(() -> {
            try {
                dao.delete(t);
                dao.commit(getDatabaseConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private DatabaseConnection getDatabaseConnection() throws SQLException {
        return connectionSource.getReadWriteConnection(null);
    }

    public Dao<T, S> getDao() {
        return dao;
    }
}
