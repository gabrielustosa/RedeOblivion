package br.com.oblivion.group.database;

import br.com.oblivion.OblivionPlugin;
import br.com.oblivion.api.database.DatabaseObject;
import br.com.oblivion.configs.group.Group;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@DatabaseTable(tableName = "ob_group_users")
@NoArgsConstructor
@Getter
public class GroupUser extends DatabaseObject {

    @DatabaseField(columnName = "id", canBeNull = false, generatedId = true)
    public @NotNull int id;

    @DatabaseField(columnName = "player_uuid", canBeNull = false)
    private @NotNull UUID uuid;

    @DatabaseField(columnName = "name", canBeNull = false)
    private @NotNull String name;

    @DatabaseField(columnName = "group_priority", canBeNull = false)
    private int groupPriority;

    @DatabaseField(columnName = "duration")
    private long duration;

    @DatabaseField(columnName = "created")
    private LocalDateTime created;

    private final List<Group> groups = new ArrayList<>();

    public GroupUser(UUID uuid) {
        setUuid(uuid);
        setDuration(0L);
    }


    public GroupUser(UUID uuid, String name, Group group, long duration) {
        setName(name);
        setUuid(uuid);
        if (group == null) {
            group = OblivionPlugin.getInstance().getGroupManager().getDefaultGroup();
        }
        setGroup(group);
        setDuration(duration);
        setCreated(LocalDateTime.now());
        getGroups().add(group);
    }


    public void setUuid(@NotNull UUID uuid) {
        this.uuid = uuid;
        setChanged(true);
    }

    public void setName(@NotNull String name) {
        this.name = name;
        setChanged(true);
    }

    public void setGroup(Group group) {
        this.groupPriority = group.priority;
        setChanged(true);
    }

    public void setDuration(long duration) {
        this.duration = duration;
        setChanged(true);
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
        setChanged(true);
    }

    public Group getGroup() {
        return OblivionPlugin.getInstance().getGroupManager().getGroup(groupPriority);
    }


}
