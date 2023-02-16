package eu.astralnetwork.astraleloapi;

import eu.astralnetwork.astraleloapi.commands.MainCommand;
import eu.astralnetwork.astraleloapi.commands.ManagerCommand;
import eu.astralnetwork.astraleloapi.database.MySQL;
import eu.astralnetwork.astraleloapi.events.JoinListener;
import eu.astralnetwork.astraleloapi.inventory.InvData;
import eu.astralnetwork.astraleloapi.inventory.InventorydataConfigurator;
import eu.astralnetwork.astraleloapi.inventory.PreDefinedItem;
import eu.astralnetwork.astraleloapi.message.MessageConfigurator;
import eu.astralnetwork.astraleloapi.util.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public final class AstralEloAPI extends JavaPlugin {

    private static EloAPI ELOAPI;
    private static AstralEloAPI MAIN;
    public static String PREFIX = "&5Astral: &7";
    public HashMap<String, Eloplayer> list = new HashMap<>();
    public ArrayList<Eloplayer> orderedlist = new ArrayList<>();
    public ArrayList<Rank> elos = new ArrayList<>();
    public int littleelo_min = 20;
    public int littleelo_action = 2;
    private String db_host = "localhost";
    private String db_db = "database";
    private String db_name = "user";
    private String db_pw = "pass";
    public String db_tableprefix = "myplugin_";
    private boolean db_usessl = false;
    private static MySQL mysql;
    public int startelo = 0;
    public String db_table_elo = "elo";
    private String invtitle = "&dYour Elo";
    private Date lastReset = new Date();
    private int resetplan = 30;
    private Economy ECO_API;
    private int eloreset_percent = 75;
    private String yourstext = "Your Rank";
    private List<String> db_extralist = new ArrayList<>();

    @Override
    public void onEnable() {
        this.startup();
    }

    private void startup() {
        db_extralist.add("characterEncoding=latin1");
        this.setupEconomy();
        ELOAPI = new EloAPI();
        MAIN = this;
        this.loadConfig();
        this.registerElos();
        new MessageConfigurator();
        new InventorydataConfigurator();
        this.registerCommands();
        this.registerEvents();
        installDatabase();
        loadFromDatabase();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                if(getDays(lastReset, new Date())>=resetplan) {
                    reset();
                }
            }
        }, 20*60*5, 20*60*5);
    }

    public void reset() {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        for(Eloplayer a : list.values()) {
            a.setElo((int) Math.round(a.getElo() * (eloreset_percent / 100.00)));
        }
        this.lastReset = new Date();
        this.getConfig().set("lastreset", df.format(lastReset));
        this.saveConfig();
    }

    private void registerElos() {
        int un_max = this.getConfig().getInt("elo.unranked.max");
        int bron_max = this.getConfig().getInt("elo.bronze.max");
        int sil_max = this.getConfig().getInt("elo.silver.max");
        int cry_max = this.getConfig().getInt("elo.crystal.max");
        elos.add(new Rank("Unranked", "f", 0, un_max, 1, 0));
        elos.add(new Rank("Bronze", "6", un_max+1, bron_max, 2, this.getConfig().getInt("rewards.bronze")));
        elos.add(new Rank("Silver", "7", bron_max+1, sil_max, 3, this.getConfig().getInt("rewards.silver")));
        elos.add(new Rank("Crystal", "b", sil_max+1, cry_max, 4, this.getConfig().getInt("rewards.crystal")));
        elos.add(new Rank("Champion", "5", cry_max+1, 1000000000, 5, this.getConfig().getInt("rewards.champ")));
        Collections.sort(elos);
    }

    public static EloAPI getAPI() {
        return ELOAPI;
    }

    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new JoinListener(this), this);
    }

    private void registerCommands() {
        getCommand("elo").setExecutor(new MainCommand(this));
        getCommand("elomanager").setExecutor(new ManagerCommand(this));
    }

    public void loadConfig() {
        FileConfiguration c = this.getConfig();

        c.options().copyDefaults(true);
        c.addDefault("Prefix", this.PREFIX);

        c.addDefault("startelo",this.startelo);
        c.addDefault("eloresetpercent",this.eloreset_percent);
        c.addDefault("yourelo",this.yourstext);
        c.addDefault("eloinvTitle",this.invtitle);
        c.addDefault("MYSQL.host",this.db_host);
        c.addDefault("MYSQL.database",this.db_db);
        c.addDefault("MYSQL.user",this.db_name);
        c.addDefault("MYSQL.pass",this.db_pw);
        c.addDefault("MYSQL.tableprefix",this.db_tableprefix);
        c.addDefault("MYSQL.usessl",this.db_usessl);
        c.addDefault("MYSQL.table",this.db_table_elo);
        c.addDefault("MYSQL.extras",db_extralist);

        c.addDefault("rewards.bronze",100);
        c.addDefault("rewards.silver",200);
        c.addDefault("rewards.crystal",350);
        c.addDefault("rewards.champ",500);

        c.addDefault("elo.unranked.max",249);
        c.addDefault("elo.bronze.max",499);
        c.addDefault("elo.silver.max",749);
        c.addDefault("elo.crystal.max",999);
        c.addDefault("littleelo.ammount", littleelo_action);
        c.addDefault("littleelo.max", littleelo_min);

        Date tmp = new Date();
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        c.addDefault("lastreset", df.format(tmp));
        c.addDefault("resetplan", resetplan);

        this.saveConfig();

        this.PREFIX = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("Prefix"));

        littleelo_min = this.getConfig().getInt("littleelo.max");
        littleelo_action = this.getConfig().getInt("littleelo.ammount");
        this.db_host = this.getConfig().getString("MYSQL.host");
        this.db_db = this.getConfig().getString("MYSQL.database");
        this.db_name = this.getConfig().getString("MYSQL.user");
        this.db_pw = this.getConfig().getString("MYSQL.pass");
        this.db_tableprefix = this.getConfig().getString("MYSQL.tableprefix");
        this.db_table_elo = this.getConfig().getString("MYSQL.table");
        this.invtitle = ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("eloinvTitle"));
        this.db_usessl = this.getConfig().getBoolean("MYSQL.usessl");
        db_extralist = this.getConfig().getStringList("MYSQL.extras");
        this.startelo = this.getConfig().getInt("startelo");
        this.resetplan = this.getConfig().getInt("resetplan");
        String tmp2 = this.getConfig().getString("lastreset");
        this.yourstext = this.getConfig().getString("yourelo");
        eloreset_percent = this.getConfig().getInt("eloresetpercent");
        try {
            this.lastReset = df.parse(tmp2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private boolean installDatabase()
    {
        this.mysql = new MySQL(this, this.db_host, this.db_db, this.db_name, this.db_pw,this.db_usessl, this.db_extralist);
        mysql.update("CREATE TABLE IF NOT EXISTS `"+db_tableprefix+db_table_elo+"` ( `ID` INT NOT NULL UNIQUE AUTO_INCREMENT , `UUID` VARCHAR(64) NOT NULL PRIMARY KEY , `NAME` VARCHAR(64) NOT NULL , `ELO` INT NOT NULL, `PICKUP` VARCHAR(64) NOT NULL) ENGINE = InnoDB;");
        if(!this.mysql.isConnected())
            return false;
        return true;
    }

    public static AstralEloAPI getInstance()
    {
        return MAIN;
    }

    public static MySQL getMySQL()
    {
        return mysql;
    }

    public void newPlayer(Player p) {
        Eloplayer ep = new Eloplayer(p.getUniqueId().toString(), p.getName());
        this.list.put(p.getUniqueId().toString(), ep);
        this.orderedlist.add(ep);
    }

    public void updatePlayername(Player p) {
        if(this.list.get(p.getUniqueId().toString()).getName().equals(p.getName()))
            return;
        this.list.get(p.getUniqueId().toString()).updateName(p.getName());
    }

    public void loadFromDatabase()
    {
        ResultSet rs = mysql.query("SELECT * FROM `"+db_tableprefix+db_table_elo+"` WHERE 1");

        try
        {
            while (rs.next())
            {
                Eloplayer ep = new Eloplayer(rs.getString("UUID"), rs.getString("NAME"), rs.getInt("ELO"), rs.getString("PICKUP"));
                this.list.put(rs.getString("UUID"), ep);
                this.orderedlist.add(ep);
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public Inventory getInventory(Player p)
    {
        Inventory inv = Bukkit.createInventory(null, InvData.mainmenu.getSlots(), InvData.mainmenu.getTitle());
        inv = Utils.invFrame(inv);

        Eloplayer ep = list.get(p.getUniqueId().toString());
        Rank act = ep.getRank();

        HashMap<String, String> rep = new HashMap<>();

        rep.put("%resetdays%",""+getResetDays());
        rep.put("%myreward%",""+act.getMoney());
        rep.put("%yours%","");

        if(act.getLevel()==1)
        {
            inv.setItem(PreDefinedItem.reset.getSlot()-1, PreDefinedItem.reset.getItem(rep));
        }
        else
        {
            inv.setItem(PreDefinedItem.reset.getSlot(), PreDefinedItem.reset.getItem(rep));
            if(getDays(new Date(), ep.getLastPickup())!=0)
            {
                inv.setItem(PreDefinedItem.collect.getSlot(), PreDefinedItem.collect.getEnchantedItem(rep));
            }
            else
            {
                inv.setItem(PreDefinedItem.collectnotavailable.getSlot(), PreDefinedItem.collectnotavailable.getItem(rep));
            }
        }

        inv.setItem(PreDefinedItem.unranked.getSlot(), PreDefinedItem.unranked.getItem(rep));
        inv.setItem(PreDefinedItem.bronze.getSlot(), PreDefinedItem.bronze.getItem(rep));
        inv.setItem(PreDefinedItem.silver.getSlot(), PreDefinedItem.silver.getItem(rep));
        inv.setItem(PreDefinedItem.crystal.getSlot(), PreDefinedItem.crystal.getItem(rep));
        inv.setItem(PreDefinedItem.champ.getSlot(), PreDefinedItem.champ.getItem(rep));

        rep.put("%yours%","§8(§7"+yourstext+"§8)");

        if(act.getLevel()==1)
        {
            inv.setItem(PreDefinedItem.unranked.getSlot(), PreDefinedItem.unranked.getEnchantedItem(rep));
        }
        if(act.getLevel()==2)
        {
            inv.setItem(PreDefinedItem.bronze.getSlot(), PreDefinedItem.bronze.getEnchantedItem(rep));
        }
        if(act.getLevel()==3)
        {
            inv.setItem(PreDefinedItem.silver.getSlot(), PreDefinedItem.silver.getEnchantedItem(rep));
        }
        if(act.getLevel()==4)
        {
            inv.setItem(PreDefinedItem.crystal.getSlot(), PreDefinedItem.crystal.getEnchantedItem(rep));
        }
        if(act.getLevel()==5)
        {
            inv.setItem(PreDefinedItem.champ.getSlot(), PreDefinedItem.champ.getEnchantedItem(rep));
        }

        return inv;
    }

    private int getResetDays()
    {
        return (resetplan-getDays(lastReset, new Date()));
    }

    public int getDays(Date from, Date to)
    {
        long diffInMillies = to.getTime()-from.getTime();
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return (int)diff;
    }

    private boolean setupEconomy()
    {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        ECO_API = rsp.getProvider();
        return ECO_API != null;
    }
    public Economy getEco()
    {
        return ECO_API;
    }
}
