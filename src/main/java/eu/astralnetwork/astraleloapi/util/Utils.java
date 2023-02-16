package eu.astralnetwork.astraleloapi.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Utils {
    public static String hiddenString(String s) {
        String hidden = "";
        for (char c : s.toCharArray()) hidden += ChatColor.COLOR_CHAR+""+c;
        hidden += ChatColor.COLOR_CHAR+"r";

        return hidden;
    }
    public static String unHiddenString(String s) {
        String[] hidden = s.split(ChatColor.COLOR_CHAR+"");
        String show = "";

        for (String a : hidden)
        {
            if(a.equals("r")){
                return show;
            }
            show += a;
        }
        return show;
    }
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static Location StringToLocation(String str)
    {
        String[] data = str.split(";");
        if (data.length != 6)
        {
            return Bukkit.getServer().getWorlds().get(0).getSpawnLocation();
        }
        Location res = new Location(Bukkit.getWorld(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]), Float.parseFloat(data[4]), Float.parseFloat(data[5]));
        return res;
    }

    public static String LocationToString(Location location)
    {
        if (location == null)
        {
            return "nirgendwo";
        }
        if (location.getWorld() == null)
        {
            return "nirgendwo";
        }
        String res = location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getYaw() + ";" + location.getPitch();
        return res;
    }





    public static ItemStack getKopf(String title, String base64, String[] lore)
    {

        ItemStack item = new ItemStack(Material.PLAYER_HEAD,1, (short) 3);
        UUID hashAsId = new UUID(base64.hashCode(), base64.hashCode());


        item = Bukkit.getUnsafe().modifyItemStack(item,
                "{display:{Name:\"" + title + "\"},SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\"" + base64 + "\"}]}}}"
        );
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(title);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if(lore!=null)
        {
            List<String> li = new ArrayList<>();
            for(String a : lore)
                li.add(a);
            im.setLore(li);
        }
        item.setItemMeta(im);
        return item;

    }
    public static ItemStack getPlayerKopf(String title, String player, String[] lore)
    {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD,1, (short) 3);

        item = Bukkit.getUnsafe().modifyItemStack(item,
                "{display:{Name:\"" + title + "\"},SkullOwner:{Name:\""+player+"\"}}"
        );
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(title);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if(lore!=null)
        {
            List<String> li = new ArrayList<>();
            for(String a : lore)
                li.add(a);
            im.setLore(li);
        }
        item.setItemMeta(im);
        return item;

    }

    public static ItemStack getGlass()
    {
        ItemStack item = new ItemStack(Material.LEGACY_STAINED_GLASS_PANE,1, (short) 7);
        ItemMeta im = item.getItemMeta();
        im.setDisplayName("§f");
        item.setItemMeta(im);
        return item;
    }



    public static Inventory invFrame(Inventory inv)
    {
        for (int i = 1; i < inv.getSize() / 9; i++)
        {
            inv.setItem(i * 9, getGlass());
            inv.setItem(i * 9 + 8, getGlass());
        }
        for (int i = 0; i < 9; i++)
        {
            inv.setItem(i, getGlass());
            inv.setItem((i + 10), getGlass());
            inv.setItem(inv.getSize()-(i+1), getGlass());
        }
        return inv;
    }



}
