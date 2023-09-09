package me.brenny.heartlles.Lifesteal;

import me.brenny.heartlles.Heartlles;
import me.brenny.heartlles.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class LsMain implements Listener {
    public static void Init() {
        Heartlles Pl = Heartlles.Instance;
        PluginManager Pm = Pl.getServer().getPluginManager();
        Pm.registerEvents(new LsMain(), Pl);

        Pl.getCommand("withdraw").setExecutor(new WithdrawCMD());
        Pl.getCommand("withdraw").setTabCompleter(new WithdrawCMD());
        Pl.getCommand("health").setExecutor(new HealthCMD());
        Pl.getCommand("health").setTabCompleter(new HealthCMD());
        Pl.getCommand("lsgive").setExecutor(new GiveCMD());
        Pl.getCommand("lsgive").setTabCompleter(new GiveCMD());
    }
    public static ItemStack getHeart() {
        ItemStack Heart = new ItemStack(Material.RED_DYE);
        ItemMeta HeartMeta = Heart.getItemMeta();
        HeartMeta.setDisplayName(Utils.Color("&c❤ &fHeart &c❤"));
        HeartMeta.setLore(Arrays.asList(new String[]{Utils.Color("&7Right-Click to Redeem")}));
        HeartMeta.addEnchant(Enchantment.MENDING, 1, true);
        HeartMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        Heart.setItemMeta(HeartMeta);
        return Heart;
    }
    public static void SetMaxHealth(Player Plr, Integer Health) { Plr.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(Health); }
    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Heartlles.Instance,() -> {
            if (Heartlles.PlrData.get(e.getPlayer().getUniqueId()).Hearts <= 0) {
                e.getPlayer().kickPlayer(Utils.Color("&c❤ &4You ran out of Hearts &c❤"));
            }
            SetMaxHealth(e.getPlayer(), Heartlles.PlrData.get(e.getPlayer().getUniqueId()).Hearts);
        }, 5l);
    }
    @EventHandler
    public void PlayerRespawn(PlayerRespawnEvent e) {
        if (Heartlles.PlrData.get(e.getPlayer().getUniqueId()).Hearts <= 0) {
            e.getPlayer().kickPlayer(Utils.Color("&c❤ &4You ran out of Hearts &c❤"));
        }
        SetMaxHealth(e.getPlayer(), Heartlles.PlrData.get(e.getPlayer().getUniqueId()).Hearts);
    }

    @EventHandler
    public void PlayerDeath(PlayerDeathEvent e) {
        Entity Killer = e.getEntity().getKiller();
        if (Killer instanceof Player Attacker) {
            if (Heartlles.PlrData.get(Attacker.getUniqueId()).Hearts == 100) {
                Attacker.sendMessage(Utils.Color("&cMax Hearts Achieved"));
                return;
            }
            Heartlles.PlrData.get(Attacker.getUniqueId()).Hearts += 2;
            Heartlles.PlrData.get(e.getEntity().getUniqueId()).Hearts -= 2;
            SetMaxHealth(Attacker, Heartlles.PlrData.get(Attacker.getUniqueId()).Hearts);
        }
    }
    @EventHandler
    public void PlayerRightClick(PlayerInteractEvent e) {
        ItemStack Item = e.getPlayer().getInventory().getItemInMainHand().clone();
        Item.setAmount(1);
        if (Item.equals(getHeart())) {
            if (Heartlles.PlrData.get(e.getPlayer().getUniqueId()).Hearts == 100) {
                e.getPlayer().sendMessage(Utils.Color("&cMax Hearts Achieved"));
                return;
            }
            e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount()-1);

            Heartlles.PlrData.get(e.getPlayer().getUniqueId()).Hearts += 2;
            SetMaxHealth(e.getPlayer(), Heartlles.PlrData.get(e.getPlayer().getUniqueId()).Hearts);
        }
    }
}
