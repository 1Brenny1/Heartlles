package me.brenny.heartlles.Teams;

import me.brenny.heartlles.Heartlles;
import me.brenny.heartlles.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class TeamEvents implements Listener {
    @EventHandler
    public void PlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player Plr) {
            if (e.getEntity().getLastDamageCause().getEntity() instanceof Player Atk) {
                if (Heartlles.PlrData.get(Plr.getUniqueId()).TeamId == Heartlles.PlrData.get(Atk.getUniqueId()).TeamId) {
                    if (!Heartlles.TData.get(Heartlles.PlrData.get(Plr.getUniqueId()).TeamId).PVP) {
                        e.setCancelled(true);
                        Atk.sendMessage(Heartlles.Prefix + Utils.Color("&cTeam PVP is disabled, Enable with &4/team pvp"));
                    }
                }
            }
        }
    }
}
