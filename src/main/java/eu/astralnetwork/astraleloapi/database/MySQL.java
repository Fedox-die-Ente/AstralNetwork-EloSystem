package eu.astralnetwork.astraleloapi.database;

import eu.astralnetwork.astraleloapi.AstralEloAPI;

import java.sql.*;
import java.util.List;

public class MySQL {
    private String host;
    private String db;
    private String user;
    private String pw;
    private Connection con;
    private AstralEloAPI main;
    private Boolean ssl;
    private String extra = "&usessl=false";



    public MySQL(AstralEloAPI m, String host, String db, String user, String pw, Boolean usessl, List<String> extralist)
    {
        this.main = m;
        this.host = host;
        this.db = db;
        this.user = user;
        this.pw = pw;
        this.ssl = usessl;
        if(this.ssl)
        {
            extra="&usessl=true";
        }
        for(String a : extralist)
        {
            extra+="&"+a;
        }

        connect();
    }



    private void connect()
    {
        try
        {
            System.out.println("Verbinde mit Datenbank " + this.host);
            this.con = DriverManager.getConnection("jdbc:mysql://" + this.host + ":3306/" + this.db + "?autoReconnect=true"+extra, this.user, this.pw);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void close()
    {
        try
        {
            this.con.close();
        }
        catch (SQLException e)
        {

            connect();
            e.printStackTrace();
        }
    }

    public void update(String query)
    {
        reconect();

        try
        {
            Statement st = this.con.createStatement();
            st.executeUpdate(query);
            st.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("MYSQL-Fehler beim Ausführen des folgenden Befehls:");
            System.out.println(query);
        }
    }


    public ResultSet query(String query)
    {
        ResultSet rs = null;
        reconect();

        try
        {
            Statement st = this.con.createStatement();
            rs = st.executeQuery(query);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("MYSQL-Fehler beim Ausführen des folgenden Befehls:");
            System.out.println(query);
        }
        return rs;
    }

    private void reconect()
    {
        if (isConnected())
        {
            return;
        }

        connect();
    }

    public boolean isConnected()
    {
        try
        {
            if (this.con == null)
            {
                return false;
            }
            if (this.con.isClosed())
            {
                return false;
            }
        }

        catch (SQLException e)
        {

            e.printStackTrace();
            return false;
        }
        return true;
    }

    public int updateGetKey(String query)
    {
        reconect();
        int candidateId = -1;
        try
        {
            PreparedStatement st = this.con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            int rows = st.executeUpdate();
            if(rows == 1)
            {
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next())
                    candidateId = rs.getInt(1);
            }
            st.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("Datenbank-Befehl fehlgeschlagen:");
            System.out.println("Befehl: "+query);
        }
        return candidateId;
    }

    public Connection getCon()
    {
        return con;
    }
}
