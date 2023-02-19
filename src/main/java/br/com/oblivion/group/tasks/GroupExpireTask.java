package br.com.oblivion.group.tasks;

import br.com.oblivion.OblivionPlugin;
import br.com.oblivion.group.database.GroupUser;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

public class GroupExpireTask implements Runnable {

    public GroupExpireTask() {
        super();
    }

    @Override
    public void run() {
        try {
            Dao<GroupUser, Integer> dao = OblivionPlugin.getInstance().getDatabaseManager().getGroupUserTableManager().getDao();

            QueryBuilder<GroupUser, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().ne("duration", 0).and().raw("duration + created <= " + System.currentTimeMillis());

            List<GroupUser> groupUsers = queryBuilder.query();
            if (groupUsers.size() > 0) {
                OblivionPlugin.getInstance().getDatabaseManager().getGroupUserTableManager().delete(groupUsers);
                OblivionPlugin.getInstance().getExpiredGroupUserManager().createExpiredGroup(groupUsers);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
