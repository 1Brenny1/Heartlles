package me.brenny.heartlles.Events;

import me.brenny.heartlles.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;


public class SpawnEvent implements Listener {
    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPlayedBefore()) {
            e.getPlayer().teleport(Config.Data.SpawnLocation);
        }
    }
    @EventHandler(priority=EventPriority.HIGHEST)
    public void PlayerRespawn(PlayerRespawnEvent e) {
        if (!e.isBedSpawn()) {
            e.getPlayer().teleport(Config.Data.SpawnLocation);
        }
    }
}
