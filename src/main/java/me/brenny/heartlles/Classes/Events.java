package me.brenny.heartlles.Classes;

import me.brenny.heartlles.Events.*;
import me.brenny.heartlles.Heartlles;
import org.bukkit.plugin.PluginManager;

public class Events {
    public static void Init() {
        Heartlles Pl = Heartlles.Instance;
        PluginManager Pm = Pl.getServer().getPluginManager();

        Pm.registerEvents(new ChatEvents(), Pl);
        Pm.registerEvents(new SQLEvents(), Pl);
        Pm.registerEvents(new HeadDrops(), Pl);
        Pm.registerEvents(new SpawnEvent(), Pl);
        Pm.registerEvents(new StatsEvents(), Pl);
        Pm.registerEvents(new CombatLog(), Pl);
    }
}
