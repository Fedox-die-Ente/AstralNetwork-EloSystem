package eu.astralnetwork.astraleloapi;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collections;

public class EloAPI {

    public static EloPlayer getEloPlayerByPlayer(Player p) {
        return getEloPlayerByUUID(p.getUniqueId().toString());
    }

    public static EloPlayer getEloPlayerByUUID(String uuid) {
        if(!AstralEloAPI.getInstance().list.containsKey(uuid))
            return null;
        return AstralEloAPI.getInstance().list.get(uuid);
    }

    public static EloPlayer getEloPlayerByName(String name) {
        for(EloPlayer a : AstralEloAPI.getInstance().orderedlist)
            if(a.getName().equalsIgnoreCase(name))
                return a;
        return null;
    }

    public static ArrayList<EloPlayer> getTopEloPlayer() {
        Collections.sort(AstralEloAPI.getInstance().orderedlist, Collections.reverseOrder());
        ArrayList<EloPlayer> temp = (ArrayList<EloPlayer>) AstralEloAPI.getInstance().orderedlist.clone();

        return temp;
    }

    public static Inventory getInv(Player p) {
        return AstralEloAPI.getInstance().getInventory(p);
    }

}
