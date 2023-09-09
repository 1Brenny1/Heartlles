package me.brenny.heartlles.Events;

import me.brenny.heartlles.Heartlles;
import me.brenny.heartlles.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CombatLog implements Listener {
    public static Map<UUID,Long> Players = new HashMap<>();
    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e) {
        Players.put(e.getPlayer().getUniqueId(), 0L);
    }
    @EventHandler
    public void PlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player Plr) {
            if (e.getEntity().getLastDamageCause().getEntity() instanceof Player Atk) {
                Players.replace(Plr.getUniqueId(), new Date().getTime()/1000);
                Players.replace(Atk.getUniqueId(), new Date().getTime()/1000);
            }
        }
    }
    @EventHandler
    public void PlayerLeave(PlayerQuitEvent e) {
        Long Time = Players.get(e.getPlayer().getUniqueId());
        Long CT = new Date().getTime()/1000;
        if (Time+15 >= CT) {
            Bukkit.broadcastMessage(Heartlles.Prefix + Utils.Color("&c" + e.getPlayer().getName() + " Combat Logged!"));
            for (ItemStack item : e.getPlayer().getInventory().getContents()) {
                e.getPlayer().getWorld().dropItemNaturally(e.getPlayer().getLocation(), item);
            }
            e.getPlayer().getInventory().clear();
            e.getPlayer().setHealth(0);
        }
        Players.remove(e.getPlayer().getUniqueId());
    }
}
