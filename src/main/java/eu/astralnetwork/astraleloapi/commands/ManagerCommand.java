package eu.astralnetwork.astraleloapi.commands;

import eu.astralnetwork.astraleloapi.AstralEloAPI;
import eu.astralnetwork.astraleloapi.EloAPI;
import eu.astralnetwork.astraleloapi.EloPlayer;
import eu.astralnetwork.astraleloapi.messages.Message;
import eu.astralnetwork.astraleloapi.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ManagerCommand implements CommandExecutor {

    private AstralEloAPI main;

    public ManagerCommand(AstralEloAPI main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(main.PREFIX + Message.ONLYPLAYERS.getMessage());
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("elo.manager")) {
            p.sendMessage(Message.nopermission.getMessage());
            return true;
        }
        if (args.length <= 0) {
            p.sendMessage(main.PREFIX + "§c/elomanager help");
            return true;
        }
        if (args[0].equalsIgnoreCase("help")) {
            p.sendMessage(main.PREFIX + "Elomanager help:");
            p.sendMessage(main.PREFIX + "§7/elomanager §areset");
            p.sendMessage(main.PREFIX + "§7/elomanager §aget [Name]");
            p.sendMessage(main.PREFIX + "§7/elomanager §aset [Name] [Elo]");
            return true;
        }
        if (args[0].equalsIgnoreCase("reset")) {
            main.reset();
            Bukkit.broadcastMessage(main.PREFIX + "§cElo Reset done.");
            return true;
        }
        if (args[0].equalsIgnoreCase("get")) {
            if (args.length <= 1) {
                p.sendMessage(main.PREFIX + "§c/elomanager help");
                return true;
            }
            p.sendMessage(main.PREFIX + "Elo from:" + args[1]);
            EloPlayer e = new EloAPI().getEloPlayerByName(args[1]);
            if (e == null) {
                p.sendMessage(main.PREFIX + "§cThe player name is not registered.");
                return true;
            }
            p.sendMessage(main.PREFIX + "§7" + e.getName() + " has §f" + e.getElo() + "§7 Elo and is on Rank " + e.getRank().getOutputTitle() + "§r§7.");

            return true;
        }
        if (args[0].equalsIgnoreCase("set")) {
            if (args.length <= 2) {
                p.sendMessage(main.PREFIX + "§c/elomanager help");
                return true;
            }
            p.sendMessage(main.PREFIX + "Manipulate the Elo from:" + args[1]);
            EloPlayer e = EloAPI.getEloPlayerByName(args[1]);
            if (e == null) {
                p.sendMessage(main.PREFIX + "§cThe player name is not registered.");
                return true;
            }
            if (!Util.isInt(args[2])) {
                p.sendMessage(main.PREFIX + "§c" + args[2] + " is not a correct argument!");
                return true;
            }
            e.setElo(Integer.parseInt(args[2]));
            p.sendMessage(main.PREFIX + "§7" + e.getName() + " have now §f" + e.getElo() + "§7 Elo and is on " + e.getRank().getOutputTitle() + "§r§7 now.");
            return true;
        }
        return true;
    }
}
