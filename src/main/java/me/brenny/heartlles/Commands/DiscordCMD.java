package me.brenny.heartlles.Commands;

import me.brenny.heartlles.Heartlles;
import me.brenny.heartlles.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class DiscordCMD implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        ComponentBuilder builder = new ComponentBuilder(Utils.Color("&x&a&a&0&0&0&0&lH&x&b&5&0&b&0&b&le&x&b&f&1&5&1&5&la&x&c&a&2&0&2&0&lr&x&d&5&2&b&2&b&lt&x&d&f&3&5&3&5&ll&x&e&a&4&0&4&0&ll&x&f&4&4&a&4&a&le&x&f&f&5&5&5&5&ls &8&l»&r Click me for our discord"));
        builder.event(new ClickEvent(ClickEvent.Action.OPEN_URL,"https://discord.gg/Gnrv3tJZBx"));
        commandSender.spigot().sendMessage(builder.create());
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return new ArrayList<>();
    }
}
