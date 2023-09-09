package me.brenny.heartlles.Events;

import me.brenny.heartlles.Heartlles;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class StatsEvents implements Listener {
    @EventHandler
    public void PlayerDeath(PlayerDeathEvent e) {
        if (e.getEntity().getLastDamageCause() != null) {
            if (e.getEntity().getLastDamageCause().getEntity() instanceof Player Atk) {
                Player Plr = e.getEntity();
                Heartlles.PlrData.get(Plr.getUniqueId()).Deaths += 1;
                Heartlles.PlrData.get(Atk.getUniqueId()).Kills += 1;
            }
        }
    }
}
