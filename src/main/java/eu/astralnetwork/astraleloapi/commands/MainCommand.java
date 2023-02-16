package eu.astralnetwork.astraleloapi.commands;

import eu.astralnetwork.astraleloapi.AstralEloAPI;
import eu.astralnetwork.astraleloapi.message.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor
{
    private final AstralEloAPI main;

    public MainCommand(AstralEloAPI main)
    {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(!(sender instanceof Player))
        {
            sender.sendMessage(main.PREFIX+ Message.ONLYPLAYERS.getMessage());
            return true;
        }
        Player p = (Player)sender;

        //p.sendMessage(Message.yourelo.getMessage());
        //p.sendMessage(Message.yourelodata.getMessage().replaceAll("%elo%", new EloAPI().getEloPlayerByPlayer(p).getElo()+"").replaceAll("%rank%",new EloAPI().getEloPlayerByPlayer(p).getRank().getOutputTitle()));

        p.openInventory(main.getInventory(p));

        return false;
    }
}