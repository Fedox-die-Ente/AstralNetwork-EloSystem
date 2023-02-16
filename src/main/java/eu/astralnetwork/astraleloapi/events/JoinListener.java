package eu.astralnetwork.astraleloapi.events;

import eu.astralnetwork.astraleloapi.AstralEloAPI;
import eu.astralnetwork.astraleloapi.EloAPI;
import eu.astralnetwork.astraleloapi.Eloplayer;
import eu.astralnetwork.astraleloapi.inventory.InvData;
import eu.astralnetwork.astraleloapi.inventory.PreDefinedItem;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JoinListener implements Listener {

    private final AstralEloAPI main;

    public JoinListener(AstralEloAPI main)
    {
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(!main.list.containsKey(p.getUniqueId().toString())) {
            main.newPlayer(p);
        } else {
            main.updatePlayername(p);
        }
        PlaceholderAPI.registerPlaceholderHook("eloapi", new PlaceholderHook() {
            @Override
            public String onRequest(OfflinePlayer p, String params) {
                if (p == null)
                    return null;
                if (!p.isOnline())
                    return null;
                Player t = p.getPlayer();
                if (params.equalsIgnoreCase("elo")) {
                    return "" + EloAPI.getEloPlayerByPlayer(t).getElo();
                }
                if (params.equalsIgnoreCase("league")) {
                    return "" + EloAPI.getEloPlayerByPlayer(t).getRank().getOutputTitle();
                }
                return null;
            }

            @Override
            public String onPlaceholderRequest(Player p, String params)
            {
                return null;
            }
        });
    }

    @EventHandler
    public void invclick(InventoryClickEvent e)
    {
        if(e.getWhoClicked().getOpenInventory().getTitle()==null)
            return;
        if(e.getWhoClicked().getOpenInventory().getTitle().equals(InvData.mainmenu.getTitle()))
            e.setCancelled(true);
        if(!e.getSlotType().equals(InventoryType.SlotType.CONTAINER))
            return;
        if(e.getView().getTitle()==null)
            return;
        if(!e.getView().getTitle().equals(InvData.mainmenu.getTitle()))
            return;
        if(!(e.getWhoClicked() instanceof Player))
            return;
        Player p = (Player) e.getWhoClicked();

        Eloplayer ep=main.list.get(p.getUniqueId().toString());

        if(e.getSlot() == PreDefinedItem.collect.getSlot())
        {
            ep.pickup();
            p.openInventory(main.getInventory(p));
        }
    }

}
