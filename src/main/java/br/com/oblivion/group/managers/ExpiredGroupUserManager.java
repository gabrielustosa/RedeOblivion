package br.com.oblivion.group.managers;

import br.com.oblivion.OblivionPlugin;
import br.com.oblivion.group.database.ExpiredGroupUser;
import br.com.oblivion.group.database.GroupUser;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExpiredGroupUserManager {

    public void createExpiredGroup(GroupUser groupUser) {
        ExpiredGroupUser expiredGroupUser = new ExpiredGroupUser(groupUser.getUuid(), groupUser.getName(), groupUser.getGroup(), groupUser.getDuration());
        OblivionPlugin.getInstance().getDatabaseManager().getExpiredGroupUserTableManager().create(expiredGroupUser);
    }

    public void createExpiredGroup(List<GroupUser> groupUsers) {
        List<ExpiredGroupUser> expiredGroupUsers = new ArrayList<>();
        for (GroupUser groupUser : groupUsers) {
            expiredGroupUsers.add(new ExpiredGroupUser(groupUser.getUuid(), groupUser.getName(), groupUser.getGroup(), groupUser.getDuration()));
        }
        OblivionPlugin.getInstance().getDatabaseManager().getExpiredGroupUserTableManager().create(expiredGroupUsers);
    }

    public @NotNull List<ExpiredGroupUser> getUserExpiredGroups(UUID playerUUID) {
        return OblivionPlugin.getInstance().getDatabaseManager().getExpiredGroupUserTableManager().list(playerUUID).join();
    }

}
