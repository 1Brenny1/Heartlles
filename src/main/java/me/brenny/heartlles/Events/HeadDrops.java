package me.brenny.heartlles.Events;

import me.brenny.heartlles.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class HeadDrops implements Listener {
    @EventHandler
    public void EntityDeath(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player Plr) {
            ItemStack Item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta ItemMeta = (SkullMeta) Item.getItemMeta();
            ItemMeta.setOwningPlayer(Plr);
            ItemMeta.setDisplayName(Utils.Color("&f" + Plr.getName() + "'s Head"));
            Item.setItemMeta(ItemMeta);
            Plr.getWorld().dropItem(Plr.getLocation(), Item);
        } else {
            ItemStack Item = new ItemStack(Material.AIR);

            switch (e.getEntity().getType()) {
                case ENDER_DRAGON:
                    Item = new ItemStack(Material.DRAGON_HEAD);
                    break;
                case ZOMBIE:
                    Item = new ItemStack(Material.ZOMBIE_HEAD);
                    break;
                case SKELETON:
                    Item = new ItemStack(Material.SKELETON_SKULL);
                    break;
                case CREEPER:
                    Item = new ItemStack(Material.CREEPER_HEAD);
                    break;
                case PIGLIN, PIGLIN_BRUTE:
                    Item = new ItemStack(Material.PIGLIN_HEAD);
                    break;
            }
            if (Item.getType() != Material.AIR) e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), Item);
        }
    }
}
