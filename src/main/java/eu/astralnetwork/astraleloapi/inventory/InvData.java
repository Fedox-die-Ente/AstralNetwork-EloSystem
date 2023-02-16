package eu.astralnetwork.astraleloapi.inventory;

import org.bukkit.ChatColor;

public enum InvData {
    mainmenu("&5Your elo", 6);

    private int rows;
    private String title;

    InvData(String t, int rows)
    {
        this.title = t;
        this.rows = rows;
    }

    public String getTitle()
    {
        return this.title;
    }
    public int getRows(){return this.rows;}
    public int getSlots(){return this.rows*9;}

    public void update(String t, int r)
    {
        this.rows = r;
        title = ChatColor.translateAlternateColorCodes('&', t);
    }
}
