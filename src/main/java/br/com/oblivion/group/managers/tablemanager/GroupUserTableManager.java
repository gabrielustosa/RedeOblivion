package br.com.oblivion.group.managers.tablemanager;

import br.com.oblivion.api.database.manager.TableManager;
import br.com.oblivion.group.database.GroupUser;
import br.com.oblivion.group.events.PlayerGroupEvent;
import br.com.oblivion.group.managers.GroupEventManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class GroupUserTableManager extends TableManager<GroupUser, Integer> {
    public GroupUserTableManager(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, GroupUser.class, Comparator.comparing(GroupUser::getUuid));
    }


    public Optional<GroupUser> getUser(UUID uuid) {
        return getEntry(new GroupUser(uuid));
    }

    public List<GroupUser> getUserGroups(UUID uuid) {
        try {
            QueryBuilder<GroupUser, Integer> queryBuilder = getDao().queryBuilder();
            queryBuilder.where().eq("player_uuid", uuid);
            return queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public void create(GroupUser groupUser) {
        GroupEventManager.callPlayerGroupEvent(groupUser.getUuid(), PlayerGroupEvent.GroupAction.CREATE);
        super.create(groupUser);
    }

    @Override
    public CompletableFuture<Void> delete(GroupUser groupUser) {
        GroupEventManager.callPlayerGroupEvent(groupUser.getUuid(), PlayerGroupEvent.GroupAction.DELETE);
        getEntry(groupUser).ifPresent(this::removeEntry);
        return super.delete(groupUser);
    }

    @Override
    public CompletableFuture<Void> delete(Collection<GroupUser> groupUsers) {
        groupUsers.forEach(u -> {
            GroupEventManager.callPlayerGroupEvent(u.getUuid(), PlayerGroupEvent.GroupAction.DELETE);
            getEntry(u).ifPresent(this::removeEntry);
        });
        return super.delete(groupUsers);
    }

    public GroupUser getUserOrCreate(UUID uuid, String playerName) {
        List<GroupUser> userGroups = getUserGroups(uuid);
        GroupUser currentUser = userGroups.stream().min(Comparator.comparing(GroupUser::getGroupPriority)).orElse(null);
        if (currentUser != null) {
            currentUser.getGroups().addAll(userGroups.stream().map(GroupUser::getGroup).collect(Collectors.toList()));
            addEntry(currentUser);
            return currentUser;
        } else {
            Optional<String> name = Optional.ofNullable(playerName);
            GroupUser user = new GroupUser(uuid, name.orElse(""), null, 0L);
            create(user);
            addEntry(user);
            return user;
        }
    }


}
