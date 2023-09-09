package me.brenny.heartlles.Events;

import me.brenny.heartlles.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChatEvents implements Listener {
    @EventHandler
    public void PlayerJoin(PlayerJoinEvent e) {
        String Prefix = Utils.Vault.chat.getPlayerPrefix(e.getPlayer());
        e.setJoinMessage(Utils.Color("&8[&a+&8]&r " + Prefix + e.getPlayer().getDisplayName()));
    }

    @EventHandler
    public void PlayerLeave(PlayerQuitEvent e) {
        String Prefix = Utils.Vault.chat.getPlayerPrefix(e.getPlayer());
        e.setQuitMessage(Utils.Color("&8[&c-&8]&r " + Prefix + e.getPlayer().getDisplayName()));
    }

    @EventHandler
    public void PlayerChat(AsyncPlayerChatEvent e) {
        if (e.getPlayer().hasPermission("Heartlles.Chat.Color")) e.setMessage(Utils.Color(e.getMessage()));
        String Prefix = Utils.Vault.chat.getPlayerPrefix(e.getPlayer());
        e.setFormat(Utils.Color(Prefix + e.getPlayer().getDisplayName() + " &8&l»&r ") + e.getMessage());
    }
}
