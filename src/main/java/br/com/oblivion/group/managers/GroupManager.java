package br.com.oblivion.group.managers;

import br.com.oblivion.OblivionPlugin;
import br.com.oblivion.configs.group.Group;
import br.com.oblivion.group.commands.GroupCommand;
import lombok.Getter;

@Getter
public class GroupManager {

    private final Group defaultGroup;

    public GroupManager() {
        this.defaultGroup = OblivionPlugin.getInstance().getGroupConfig().groups.stream().filter(group -> group.isDefault).findFirst().orElse(null);

        OblivionPlugin.getInstance().getCommandManager().registerCommand(new GroupCommand());
    }


    public Group getGroup(String name) {
        return OblivionPlugin.getInstance().getGroupConfig().groups.stream().filter(group -> group.name.equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Group getGroup(int priority) {
        return OblivionPlugin.getInstance().getGroupConfig().groups.stream().filter(group -> group.priority == priority).findFirst().orElse(null);
    }

}
