package me.brenny.heartlles.Events;
import me.brenny.heartlles.Heartlles;
import me.brenny.heartlles.Lifesteal.LsMain;
import me.brenny.heartlles.SQL;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SQLEvents implements Listener {
    @EventHandler(priority=EventPriority.HIGHEST)
    public void PlayerJoin(PlayerJoinEvent e) {
        SQL.LoadData(e.getPlayer());
    }
    @EventHandler(priority=EventPriority.LOWEST)
    public void PlayerLeave(PlayerQuitEvent e) {
        SQL.SaveData(e.getPlayer());
    }
}
