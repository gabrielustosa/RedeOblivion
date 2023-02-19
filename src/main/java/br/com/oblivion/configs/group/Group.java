package br.com.oblivion.configs.group;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class Group {

    public String name;
    public boolean isDefault;
    public boolean isVip;
    public String prefix;
    public String color;
    public int priority;
    public List<String> inheritance;

    public Group(String name, boolean isDefault, boolean isVip, String prefix, String color, int priority, List<String> inheritance) {
        this.name = name;
        this.isDefault = isDefault;
        this.isVip = isVip;
        this.prefix = prefix;
        this.color = color;
        this.priority = priority;
        this.inheritance = inheritance;
    }

    public String replacedColor() {
        return this.color.replace("&", "§");
    }
}