package eu.astralnetwork.astraleloapi.inventory;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public enum PreDefinedItem {
    reset("&5Reset", Material.LEVER, new String[] {"&7The next elo reset is in &5%resetdays% &7days."}, 0, 5),
    unranked("&dUnranked %yours%", Material.LEATHER_CHESTPLATE, new String[] {"§7§oFrom §50 §7to §5250"}, 0, 29),
    bronze("&dBronze %yours%", Material.LEATHER_CHESTPLATE, new String[] {"§7§oFrom §2250 §7to §5500"}, 0, 30),
    silver("&dSilver %yours%", Material.LEATHER_CHESTPLATE, new String[] {"§7§oFrom §5500 §7to §5750"}, 0, 31),
    crystal("&dCrystal %yours%", Material.LEATHER_CHESTPLATE, new String[] {"§7§oFrom §5750 §7to §51000"}, 0, 32),
    champ("&dChamp %yours%", Material.LEATHER_CHESTPLATE, new String[] {"§7§oMAX RANK"}, 0, 33),
    collect("&dGet your reward", Material.SUNFLOWER, new String[] {"&Get %myreward% Coins.","&7§oClick here"}, 0, 3),
    collectnotavailable("&cYou can get your money tomorrow again", Material.SUNFLOWER, new String[] {"&7&oWaiting.."}, 0, 3);

    private int sloot;
    private int damage;
    private Material typ;
    private List<String> lore;
    private String title;

    PreDefinedItem(String msg, Material type, String[] lore, int dmg, int slot)
    {
        this.typ = type;
        List<String> temp = new ArrayList<>();
        for(String a : lore)
        {
            temp.add(ChatColor.translateAlternateColorCodes('&', a));
        }
        this.lore = temp;

        this.title = msg;

        this.damage = dmg;

        this.sloot = slot;
    }
    public void update(String t, Material type, List<String> l, int dmg, int slot)
    {
        this.typ = type;
        this.lore = l;
        this.title = ChatColor.translateAlternateColorCodes('&',t);

        this.damage = dmg;
        this.sloot = slot;
    }
    public List<String> getLore()
    {
        return lore;
    }
    public List<String> getLore(HashMap<String, String> replacements)
    {
        List<String> temp = new ArrayList<>();
        for(String a : this.lore)
        {
            for(String r: replacements.keySet())
            {
                if(a.contains(r))
                {
                    a=a.replaceAll(r,replacements.get(r));
                }
            }
            temp.add(a);
        }
        return temp;
    }


    public ItemStack getItem()
    {
        ItemStack itm = new ItemStack(this.typ);

        itm.setDurability((short) this.damage);
        ItemMeta im = itm.getItemMeta();
        im.setDisplayName(this.title);
        if(this.lore!=null)
            im.setLore(this.lore);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itm.setItemMeta(im);

        return itm;
    }
    public ItemStack getItem(HashMap<String, String> replacements)
    {
        ItemStack itm = new ItemStack(this.typ);

        itm.setDurability((short) this.damage);
        ItemMeta im = itm.getItemMeta();

        im.setDisplayName(this.getTitle(replacements));
        if(this.lore!=null)
        {
            List<String> temp = new ArrayList<>();
            for(String a : this.lore)
            {
                for(String r: replacements.keySet())
                {
                    if(a.contains(r))
                    {
                        a=a.replaceAll(r,replacements.get(r));
                    }
                }
                temp.add(a);
            }
            im.setLore(temp);
        }
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itm.setItemMeta(im);

        return itm;
    }
    public ItemStack getEnchantedItem()
    {
        ItemStack itm = new ItemStack(this.typ);

        itm.setDurability((short) this.damage);
        ItemMeta im = itm.getItemMeta();
        im.setDisplayName(this.title);
        im.addEnchant(Enchantment.DURABILITY, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if(this.lore!=null)
            im.setLore(this.lore);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itm.setItemMeta(im);

        return itm;
    }

    public ItemStack getEnchantedItem(HashMap<String, String> replacements)
    {
        ItemStack itm = new ItemStack(this.typ);

        itm.setDurability((short) this.damage);
        ItemMeta im = itm.getItemMeta();
        im.setDisplayName(this.getTitle(replacements));
        im.addEnchant(Enchantment.DURABILITY, 1, true);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if(this.lore!=null)
        {
            List<String> temp = new ArrayList<>();
            for(String a : this.lore)
            {
                for(String r: replacements.keySet())
                {
                    if(a.contains(r))
                    {
                        a=a.replaceAll(r,replacements.get(r));
                    }
                }
                temp.add(a);
            }
            im.setLore(temp);
        }
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itm.setItemMeta(im);

        return itm;
    }
    public String getTitle()
    {
        return title;
    }
    public String getTitle(HashMap<String, String> replacements)
    {
        String temp = title;
        for(String r: replacements.keySet())
        {
            if(temp.contains(r))
            {
                temp=temp.replaceAll(r,replacements.get(r));
            }
        }
        return temp;
    }

    public Material getMaterialId()
    {
        return this.typ;
    }
    public int getDamage()
    {
        return this.damage;
    }
    public int getSlot()
    {
        return this.sloot;
    }

    public String[] getLoreArray(HashMap<String, String> replacements)
    {
        String[] l = new String[this.lore.size()];
        for(int i=0;i<this.lore.size();i++)
        {
            String a = this.lore.get(i);

            for(String r: replacements.keySet())
            {
                if(a.contains(r))
                {
                    a=a.replaceAll(r,replacements.get(r));
                }
            }

            l[i]=a;
        }
        return l;
    }

    public String[] getLoreArray()
    {
        String[] l = new String[this.lore.size()];
        for(int i=0;i<this.lore.size();i++)
        {
            String a = this.lore.get(i);

            l[i]=a;
        }
        return l;
    }
}
