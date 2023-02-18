package eu.astralnetwork.astraleloapi;

import eu.astralnetwork.astraleloapi.messages.Message;
import org.bukkit.Bukkit;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class EloPlayer implements Comparable {

    private Date pickup;
    private int elo;
    private String uuid;
    private String name;
    private Rank rank = null;

    // Create
    public EloPlayer(String uuid, String name) {
        this.uuid = uuid;
        this.elo = AstralEloAPI.getInstance().startelo;
        this.name = name;
        AstralEloAPI.getMySQL().update("INSERT INTO "+AstralEloAPI.getInstance().db_tableprefix+AstralEloAPI.getInstance().db_table_elo+" (UUID, NAME, ELO, PICKUP) VALUES ('"+this.uuid+"','"+this.name+"','"+this.elo+"', '00.00.0000')");
        updateRank();
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Date pdate = new Date();
        try {
            pdate = df.parse("00.00.0000");
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        this.pickup = pdate;
    }

    // Loading
    public EloPlayer(String uuid, String name, int elo, String pickup) {
        this.uuid = uuid;
        this.elo = elo;
        this.name = name;
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Date pdate = new Date();
        try {
            pdate = df.parse(pickup);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        this.pickup = pdate;
        updateRank();
    }

    public Date getLastPickup() {
        return pickup;
    }

    private void update() {
        this.updateRank();
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        AstralEloAPI.getMySQL().update("UPDATE "+AstralEloAPI.getInstance().db_tableprefix+AstralEloAPI.getInstance().db_table_elo+" SET NAME='"+this.name+"', ELO='"+this.elo+"', PICKUP='"+df.format(pickup)+"' WHERE UUID='"+this.uuid+"'");
    }

    private void updateRank() {
        for(Rank e : AstralEloAPI.getInstance().elos) {
            if(this.elo<=e.getMax() && this.elo>=e.getMin())
                this.rank = e;
        }
    }

    public Rank getRank() {
        return this.rank;
    }

    public void setElo(int set) {
        elo = set;
        if(elo<0)elo=0;
        this.update();
    }

    public void addElo(int add) {
        elo += add;
        this.update();
    }

    public int removeElo(int re) {
        if(this.elo<AstralEloAPI.getInstance().littleelo_min)
            re=AstralEloAPI.getInstance().littleelo_action;
        elo -= re;
        if(elo<0) {
            elo = 0;
        }
        this.update();
        return re;
    }

    public int getElo() {
        return elo;
    }

    @Override
    public int compareTo(Object compare) {
        int c_elo=((EloPlayer)compare).getElo();
        return this.getElo()-c_elo;
    }

    public String getName() {
        return this.name;
    }

    public void updateName(String n) {
        this.name=n;
        this.update();
    }

    public void takeEloFromOther(EloPlayer t, int ammount) {
        if(t.elo<AstralEloAPI.getInstance().littleelo_min)
            ammount=AstralEloAPI.getInstance().littleelo_action;
        t.removeElo(ammount);
        this.addElo(ammount);
    }

    public void giveEloToOther(EloPlayer t, int ammount) {
        if(this.elo<AstralEloAPI.getInstance().littleelo_min)
            ammount=AstralEloAPI.getInstance().littleelo_action;
        this.removeElo(ammount);
        t.addElo(ammount);
    }

    public void pickup()
    {
        if(AstralEloAPI.getInstance().getDays(this.getLastPickup(), new Date())!=0)
        {
            this.pickup=new Date();
            this.update();
            AstralEloAPI.getInstance().getEco().depositPlayer(Bukkit.getPlayer(UUID.fromString(this.uuid)), this.getRank().getMoney());
        }
        else
        {
            Bukkit.getPlayer(UUID.fromString(this.uuid)).sendMessage(Message.notavailable.getMessage());
        }
    }

}
