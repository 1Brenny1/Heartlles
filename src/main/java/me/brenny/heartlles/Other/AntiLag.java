package me.brenny.heartlles.Other;

import me.brenny.heartlles.Heartlles;
import me.brenny.heartlles.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;

public class AntiLag {
    public static Integer Items = 0;
    public static void Init() {
        Heartlles Pl = Heartlles.Instance;
        //Bukkit.getWorlds().forEach((world) -> world.getEntitiesByClass(Item.class).forEach((Item) -> Item.remove()));


        Bukkit.getScheduler().scheduleSyncRepeatingTask(Pl, () -> {
            Items = 0;
            Bukkit.getScheduler().scheduleSyncDelayedTask(Pl, () -> {
                Bukkit.broadcast(Heartlles.Prefix + Utils.Color("Clearing items in 5 minutes"), "");
            }, 6000);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Pl, () -> {
                Bukkit.broadcast(Heartlles.Prefix + Utils.Color("Clearing items in 2 minutes"), "");
            }, 9600);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Pl, () -> {
                Bukkit.broadcast(Heartlles.Prefix + Utils.Color("Clearing items in 1 minute"), "");
            }, 10800);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Pl, () -> {
                Bukkit.broadcast(Heartlles.Prefix + Utils.Color("Clearing items in 30 seconds"), "");
            }, 11400);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Pl, () -> {
                Bukkit.broadcast(Heartlles.Prefix + Utils.Color("Clearing items in 5 seconds"), "");
            }, 11900);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Pl, () -> {
                Bukkit.broadcast(Heartlles.Prefix + Utils.Color("Clearing items in 4 seconds"), "");
            }, 11920);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Pl, () -> {
                Bukkit.broadcast(Heartlles.Prefix + Utils.Color("Clearing items in 3 seconds"), "");
            }, 11940);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Pl, () -> {
                Bukkit.broadcast(Heartlles.Prefix + Utils.Color("Clearing items in 2 seconds"), "");
            }, 11960);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Pl, () -> {
                Bukkit.broadcast(Heartlles.Prefix + Utils.Color("Clearing items in 1 seconds"), "");
            }, 11980);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Pl, () -> {

                Bukkit.getWorlds().forEach((world) -> {
                    world.getEntitiesByClass(Item.class).forEach((Item) -> {
                        Item.remove();
                        Items += 1;
                    });
                });
                Bukkit.broadcast(Heartlles.Prefix + Utils.Color("Cleared " + Items + " Item(s)"), "");
            }, 12000);
        }, 0, 12000);
    }
}
