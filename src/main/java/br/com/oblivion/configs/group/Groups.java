package br.com.oblivion.configs.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Groups {

    public List<Group> groups = new ArrayList<>();

    public Groups() {
        groups.add(new Group("default", true, false, "", "",1, Collections.emptyList()));
    }


}
