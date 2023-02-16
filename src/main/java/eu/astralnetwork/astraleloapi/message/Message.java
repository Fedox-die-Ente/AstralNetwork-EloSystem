package eu.astralnetwork.astraleloapi.message;

import eu.astralnetwork.astraleloapi.AstralEloAPI;
import org.bukkit.ChatColor;

public enum Message {

    error("A error has occured!"),
    ONLYPLAYERS("This command is only for players!"),
    nopermission("You don't have permission to do this!"),
    yourelo("Your elo:"),
    yourelodata("%elo% / %rank%"),
    notavailable("This feature is tomorrow available again.");

    private String message;

    Message(String msg)
    {
        this.message = msg;
    }

    public String getMessage()
    {
        return ChatColor.translateAlternateColorCodes('&',message);
    }
    public void setMessage(String msg)
    {
        message = AstralEloAPI.PREFIX+ msg;
    }

}
