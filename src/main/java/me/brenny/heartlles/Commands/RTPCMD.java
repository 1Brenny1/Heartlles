package me.brenny.heartlles.Commands;

import me.brenny.heartlles.Config;
import me.brenny.heartlles.Heartlles;
import me.brenny.heartlles.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RTPCMD implements CommandExecutor, TabCompleter, Listener {
    public static List<UUID> Pvp = new ArrayList<>();
    @EventHandler
    public void PlayerDmg(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player Plr) {
            if (e.getEntity().getLastDamageCause().getEntity() instanceof Player Atk) {
                if (!Pvp.contains(Plr.getUniqueId())) Pvp.add(Plr.getUniqueId());
                if (!Pvp.contains(Atk.getUniqueId())) Pvp.add(Plr.getUniqueId());
            }
        }
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player Plr) {
            if (Pvp.contains(Plr.getUniqueId())) Pvp.remove(Plr.getUniqueId());
            Random rand = new Random();
            Integer X = rand.nextInt(-Config.Data.RTPRadius, Config.Data.RTPRadius);
            Integer Z = rand.nextInt(-Config.Data.RTPRadius, Config.Data.RTPRadius);

            World world = Bukkit.getWorld(Config.Data.RTPWorld);
            Chunk chunk = world.getChunkAt(X,Z);
            if (!chunk.isLoaded()) chunk.load();
            Location Pos = new Location(world, X, 128, Z);

            if (Plr.hasPermission("Heartlles.RTP.Bypass")) {
                Plr.sendMessage(Heartlles.Prefix + Utils.Color("Teleporting to a random location"));
            } else {
                Plr.sendMessage(Heartlles.Prefix + Utils.Color("Teleporting to a random location in &b3 Seconds"));
            }

            Bukkit.getScheduler().runTaskAsynchronously(Heartlles.Instance, () -> {
                while (world.getBlockAt(Pos).isEmpty()) Pos.setY(Pos.getY()-2);
                Pos.setY(Pos.getY() + 2);
                if (Plr.hasPermission("Heartlles.RTP.Bypass")) {
                    Plr.sendTitle(Utils.Color("&aRtp Complete"), "%s, %s, %s".formatted(Pos.getX(), Pos.getY(), Pos.getZ()));
                    Bukkit.getScheduler().runTask(Heartlles.Instance, () -> Plr.teleport(Pos));
                }
            });
            if (!Plr.hasPermission("Heartlles.RTP.Bypass")) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(Heartlles.Instance, ()-> {
                    if (Pvp.contains(Plr.getUniqueId())) {
                        Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cFailed to RTP, in combat"));
                        return;
                    }
                    Plr.sendTitle(Utils.Color("&aRtp Complete"), "%s, %s, %s".formatted(Pos.getX(), Pos.getY(), Pos.getZ()));
                    Plr.teleport(Pos);
                }, 60L);
            }
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return new ArrayList<>();
    }
}
