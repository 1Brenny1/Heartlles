package me.brenny.heartlles;


import com.google.gson.Gson;
import me.brenny.heartlles.Objects.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;

public class SQL {
    public static Connection DB = null;

    public static void ExSQL(String SQL) {
        try {
            Statement Stmt = DB.createStatement();
            Stmt.execute(SQL);
        } catch (Exception e) {
            Heartlles.Log.severe("Failed to execute SQL: " + SQL);
        }
    }
    public static ResultSet ExSQL_Result(String SQL) {
        try {
            Statement Stmt = DB.createStatement();
            return Stmt.executeQuery(SQL);
        } catch (Exception e) {
            Heartlles.Log.severe("Failed to execute SQL: " + SQL);
        }
        return null;
    }

    public static void LoadData(Player Plr) {
        ResultSet Result = ExSQL_Result("SELECT Data FROM Players WHERE UUID='" + Plr.getUniqueId() + "'");

        PlayerData PlrData = new PlayerData();
        try {
            String Data = Result.getString("Data");
            if (Data == null) throw new Exception();
            PlrData = new Gson().fromJson(Data, PlayerData.class);
        } catch (Exception e) {
            ExSQL("INSERT INTO Players (UUID, Data) VALUES ('" + Plr.getUniqueId() + "', '"+ new Gson().toJson(PlrData) +"')");
        }
        Heartlles.PlrData.put(Plr.getUniqueId(), PlrData);
    }

    public static void SaveData(Player Plr) {
        ExSQL("UPDATE Players SET Data='"+ new Gson().toJson(Heartlles.PlrData.get(Plr.getUniqueId())) +"' WHERE UUID='"+ Plr.getUniqueId() +"'");
    }

    public static void Connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            DB = DriverManager.getConnection("jdbc:sqlite:" + Heartlles.Instance.getDataFolder() + "/Database.db");
            DB.setAutoCommit(true);
            String SQL = String.join("\n", new String[] {
                    "CREATE TABLE IF NOT EXISTS Players (",
                    "UUID TEXT PRIMARY KEY UNIQUE NOT NULL,",
                    "Data TEXT NOT NULL",
                    ")"
            });
            ExSQL(SQL);
            SQL = String.join("\n", new String[] {
                    "CREATE TABLE IF NOT EXISTS Teams (",
                    "Id INT AUTO INCREMENT PRIMARY KEY UNIQUE NOT NULL,",
                    "Data TEXT",
                    ")"
            });
            ExSQL(SQL);
            SQL = String.join("\n", new String[] {
                    "CREATE TABLE IF NOT EXISTS Discord (",
                    "Discord INT UNIQUE,",
                    "Minecraft TEXT UNIQUE,",
                    "LinkCode BIGINT UNIQUE",
                    ")"
            });
            ExSQL(SQL);


            Heartlles.Log.info("Connected to database");
        } catch (Exception e) {
            Heartlles.Log.severe("Failed to connect to database");
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Heartlles.Instance, ()->{
            Heartlles.Log.info("Saving User Data");
            Bukkit.getOnlinePlayers().forEach((Plr)->SaveData(Plr));
        },6000,6000);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Heartlles.Instance, ()->{
            Heartlles.Log.info("Saving Team Data");
            Heartlles.TData.forEach((Id, Team) -> {
                SQL.ExSQL("REPLACE INTO Teams (Id, Data) VALUES (" + Id +", '"+ new Gson().toJson(Team)+"')");
            });
        },6000,6000);
    }
    public static void Disconnect() {
        try {
            DB.close();
            Heartlles.Log.info("Disconnected from database");
        } catch (Exception e) {
            Heartlles.Log.severe("Failed to disconnect from database");
        }
    }
}
