package eu.astralnetwork.astraleloapi.message;

import eu.astralnetwork.astraleloapi.AstralEloAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessageConfigurator {
    private FileConfiguration config;
    private File configfile;

    public MessageConfigurator()
    {
        this.configfile = new File(AstralEloAPI.getInstance().getDataFolder().getPath()+File.separator+"messages.yml");
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
        loadConfig();

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
        for(Message a : Message.values())
        {
            this.config.addDefault("Message."+a.name(),a.getMessage());
        }
    }
    public void loadConfig()
    {
        for(Message a : Message.values())
        {
            a.setMessage( this.config.getString("Message."+a.name(),"§4The message "+a.name()+" was not configured correctly!"));
        }
    }
}
