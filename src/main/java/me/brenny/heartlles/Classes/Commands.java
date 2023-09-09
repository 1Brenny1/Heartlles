package me.brenny.heartlles.Classes;

import me.brenny.heartlles.Commands.DiscordCMD;
import me.brenny.heartlles.Commands.LinkCmd;
import me.brenny.heartlles.Commands.RTPCMD;
import me.brenny.heartlles.Commands.SpawnCMD;
import me.brenny.heartlles.Heartlles;
import org.bukkit.plugin.PluginManager;

public class Commands {
    public static void Init() {
        Heartlles Pl = Heartlles.Instance;
        PluginManager Pm = Pl.getServer().getPluginManager();

        Pl.getCommand("Spawn").setExecutor(new SpawnCMD());
        Pl.getCommand("Spawn").setTabCompleter(new SpawnCMD());
        Pm.registerEvents(new SpawnCMD(), Pl);

        Pl.getCommand("Discord").setExecutor(new DiscordCMD());
        Pl.getCommand("Discord").setTabCompleter(new DiscordCMD());

        Pl.getCommand("Link").setExecutor(new LinkCmd());
        Pl.getCommand("Link").setTabCompleter(new LinkCmd());

        Pl.getCommand("RTP").setExecutor(new RTPCMD());
        Pl.getCommand("RTP").setTabCompleter(new RTPCMD());
        Pm.registerEvents(new RTPCMD(), Pl);
    }
}
