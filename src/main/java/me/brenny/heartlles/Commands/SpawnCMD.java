package me.brenny.heartlles.Commands;

import me.brenny.heartlles.Config;
import me.brenny.heartlles.Heartlles;
import me.brenny.heartlles.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpawnCMD implements CommandExecutor, TabCompleter, Listener {
    public static List<UUID> Combat = new ArrayList<>();
    @EventHandler
    public void PlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player Plr) {
            if (e.getEntity().getLastDamageCause() == null) return;
            if (e.getEntity().getLastDamageCause().getEntity() instanceof Player ATk) {
                Combat.add(Plr.getUniqueId());
                Combat.add(ATk.getUniqueId());
            }
        }
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length > 0) {
            if (commandSender.hasPermission("Heartlles.Spawn.Other")) {
                Player Target = Bukkit.getPlayer(strings[0]);
                if (Target != null) {
                    Target.teleport(Config.Data.SpawnLocation);
                    commandSender.sendMessage(Utils.Color("&6Teleporting " + Target.getName() + " to spawn"));
                } else commandSender.sendMessage(Utils.Color("&cNo player online with name " + strings[0]));
            } else commandSender.sendMessage(Utils.Color("&6No permission!"));
        } else {
            if (commandSender instanceof Player Plr) {
                if (Plr.hasPermission("Heartlles.Spawn.Override")) {
                    Plr.teleport(Config.Data.SpawnLocation);
                    Plr.sendMessage(Utils.Color("&6Teleported to spawn"));
                } else {
                    Plr.sendMessage(Utils.Color("&6Teleporting to spawn in 3 seconds"));
                    if (Combat.contains(Plr.getUniqueId())) Combat.remove(Plr.getUniqueId());
                    Location Loc = Plr.getLocation().clone();
                    Loc.setDirection(new Vector());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Heartlles.Instance, () -> {
                        if (Combat.contains(Plr.getUniqueId())) {
                            Plr.sendMessage(Utils.Color("&cTeleport failed, In combat"));
                        } else if (Loc.equals(Plr.getLocation().clone().setDirection(new Vector()))) {
                            Plr.sendMessage(Utils.Color("&cTeleport failed, You moved"));
                        } else {
                            Plr.teleport(Config.Data.SpawnLocation);
                            Plr.sendMessage(Utils.Color("&6Teleported to spawn"));
                        }
                    }, 60l);
                }
            } else {
                commandSender.sendMessage("Player only command");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> TabComplete = new ArrayList<>();
        if (strings.length == 1) Bukkit.getServer().getOnlinePlayers().forEach((Plr) -> TabComplete.add(Plr.getName()));
        return TabComplete;
    }
}
