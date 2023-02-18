package eu.astralnetwork.astraleloapi.inventorys;

import eu.astralnetwork.astraleloapi.AstralEloAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class InventorydataConfigurator {
    private FileConfiguration config;
    private File configfile;

    public InventorydataConfigurator()
    {
        this.configfile = new File(AstralEloAPI.getInstance().getDataFolder().getPath()+File.separator+"inventory.yml");
        if(!this.configfile.exists())
        {
            try
            {
                this.configfile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return;
            }
        }
        this.config = YamlConfiguration.loadConfiguration(configfile);

        this.config.options().copyDefaults(true);
        defaultConfig();
        this.save();
        Bukkit.getScheduler().scheduleSyncDelayedTask(AstralEloAPI.getInstance(), new Runnable() {
            @Override
            public void run()
            {
                loadConfig();
            }
        },2);

    }
    public void save()
    {
        try
        {
            this.config.save(this.configfile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public void defaultConfig()
    {
        for(PreDefinedItem a : PreDefinedItem.values())
        {
            this.config.addDefault("Items."+a.name()+".Title",a.getTitle());
            this.config.addDefault("Items."+a.name()+".Lore",a.getLore());
            this.config.addDefault("Items."+a.name()+".MaterialId",a.getMaterialId());
            this.config.addDefault("Items."+a.name()+".Damage",a.getDamage());
            this.config.addDefault("Items."+a.name()+".Slot",a.getSlot());
        }
        for(InvData a : InvData.values())
        {
            this.config.addDefault("Inventare."+a.name()+".Titel",a.getTitle());
            this.config.addDefault("Inventare."+a.name()+".Zeilen",a.getRows());
        }
    }
    public void loadConfig()
    {
        for(PreDefinedItem a : PreDefinedItem.values())
        {
            a.update(
                    config.getString("Items."+a.name()+".Title","&4Bitte server reloaden!"),
                    config.getObject("Items."+a.name()+".MaterialId", Material.class),
                    config.getStringList("Items."+a.name()+".Lore"),
                    config.getInt("Items."+a.name()+".Damage", 0),
                    config.getInt("Items."+a.name()+".Slot", 0)
            );
        }
        for(InvData a : InvData.values())
        {
            a.update(
                    this.config.getString("Inventare."+a.name()+".Titel"),
                    this.config.getInt("Inventare."+a.name()+".Zeilen")
            );
        }
    }
}
