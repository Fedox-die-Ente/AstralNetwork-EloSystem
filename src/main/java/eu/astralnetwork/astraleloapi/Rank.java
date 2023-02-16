package eu.astralnetwork.astraleloapi;

import org.bukkit.ChatColor;

public class Rank implements Comparable {

    private String name;
    private String chatcolor;
    private int min;
    private int max;
    private int level;
    private int money = -1;

    public String getName() {
        return name;
    }

    public String getChatColor() {
        return chatcolor;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public Rank(String name, String chatcolor, int min, int max, int level, int money) {
        this.name = name;
        this.chatcolor = chatcolor;
        this.min = min;
        this.max = max;
        this.level = level;
        this.money = money;
    }

    public int getLevel() {
        return level;
    }

    public String getOutputTitle() {
        return ChatColor.COLOR_CHAR + getChatColor() + "§l" + this.getName();
    }

    @Override
    public int compareTo(Object o) {
        int tmp = ((Rank) o).getMax();
        return this.getMax() - tmp;
    }

    public int getMoney() {
        return this.money;
    }
}
