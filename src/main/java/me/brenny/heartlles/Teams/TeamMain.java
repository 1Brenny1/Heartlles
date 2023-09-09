package me.brenny.heartlles.Teams;

import me.brenny.heartlles.Heartlles;
import org.bukkit.plugin.PluginManager;

public class TeamMain {
    public static void Init() {
        Heartlles Pl = Heartlles.Instance;
        PluginManager Pm = Pl.getServer().getPluginManager();
        Pl.getCommand("team").setExecutor(new TeamCMD());
        Pl.getCommand("team").setTabCompleter(new TeamCMD());
        Pm.registerEvents(new TeamEvents(), Pl);
    }
}
