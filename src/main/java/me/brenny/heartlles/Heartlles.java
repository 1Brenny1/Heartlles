package me.brenny.heartlles;

import com.google.gson.Gson;
import me.brenny.heartlles.Classes.Commands;
import me.brenny.heartlles.Classes.Events;
import me.brenny.heartlles.Discord.DiscordMain;
import me.brenny.heartlles.Events.CombatLog;
import me.brenny.heartlles.Lifesteal.LsMain;
import me.brenny.heartlles.Objects.PlayerData;
import me.brenny.heartlles.Objects.TeamData;
import me.brenny.heartlles.Other.AntiLag;
import me.brenny.heartlles.Other.Sidebar;
import me.brenny.heartlles.Other.Verification;
import me.brenny.heartlles.Teams.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public final class Heartlles extends JavaPlugin {


    public static Heartlles Instance;
    public static Logger Log;
    public static String Prefix = Utils.Color("&#aa0000&lH&#b50b0b&le&#bf1515&la&#ca2020&lr&#d52b2b&lt&#df3535&ll&#ea4040&ll&#f44a4a&le&#ff5555&ls &8&l»&r ");
    public static Map<UUID, PlayerData> PlrData = new HashMap<>();
    public static Map<Integer, TeamData> TData = new HashMap<>();
    @Override
    public void onEnable() {
        Instance = this;
        Log = getLogger();

        Log.info("");
        Log.info("| Heartlles Core");
        Log.info("| V" + getDescription().getVersion());
        Log.info("|");
        Log.info("| By: 1Brenny1 @ Brenny.tk");
        Log.info("");

        if (!Utils.SetupVault()) {
            Log.severe("Failed to hook vault");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!getDataFolder().exists()) getDataFolder().mkdirs();

        SQL.Connect();
        Config.LoadConfig();

        Verification.Init();

        Commands.Init();
        Events.Init();
        LsMain.Init();
        TeamMain.Init();
        AntiLag.Init();
        Sidebar.Init();

        DiscordMain.Init();

        getServer().getOnlinePlayers().forEach((Player Plr) -> {
            SQL.LoadData(Plr);
            CombatLog.Players.put(Plr.getUniqueId(), 0L);
        });

        ResultSet Result = SQL.ExSQL_Result("SELECT * FROM Teams");
        try {
            while (Result.next()) {
                TeamData Team = new Gson().fromJson(Result.getString("Data"), TeamData.class);
                TData.put(Result.getInt("Id"), Team);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        getServer().getOnlinePlayers().forEach((Player Plr) -> {
            SQL.SaveData(Plr);
            Plr.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        });
        TData.forEach((Id, Team) -> {
            SQL.ExSQL("REPLACE INTO Teams (Id, Data) VALUES (" + Id +", '"+ new Gson().toJson(Team)+"')");
        });
        DiscordMain.Shutdown();
        SQL.Disconnect();
    }

}
