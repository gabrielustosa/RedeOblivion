package br.com.oblivion.group.database;

import br.com.oblivion.configs.group.Group;
import com.j256.ormlite.table.DatabaseTable;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@DatabaseTable(tableName = "ob_expired_group_users")
@NoArgsConstructor
public class ExpiredGroupUser extends GroupUser {

    public ExpiredGroupUser(UUID uuid, String name, Group group, long duration) {
        setName(name);
        setUuid(uuid);
        setGroup(group);
        setDuration(duration);
        setCreated(LocalDateTime.now());
    }

}
