package me.brenny.heartlles.Commands;

import me.brenny.heartlles.Discord.DiscordMain;
import me.brenny.heartlles.Heartlles;
import me.brenny.heartlles.SQL;
import me.brenny.heartlles.Utils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WhoIsCMD implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player Plr) {
            if (strings.length >= 1) {
                Player Target = Bukkit.getPlayer(strings[0]);
                if (Target != null) {
                    ResultSet Result = SQL.ExSQL_Result("SELECT * FROM Discord WHERE Minecraft='" + Target.getUniqueId() + "'");
                    try {
                        Integer UserID = Result.getInt("Discord");
                        if (UserID <= 0) {
                            Plr.sendMessage(Heartlles.Prefix + Utils.Color("&c" + Target.getName() + " has not linked their account"));
                        } else {
                            Member Mem = DiscordMain.shardManager.getGuildById(1135807515414765689L).getMemberById(UserID);
                            if (Mem != null) {
                                Plr.sendMessage(Heartlles.Prefix + Utils.Color(Target.getName() + "'s discord is &b" + Mem.getUser().getName()));
                            } else {
                                Plr.sendMessage(Heartlles.Prefix + Utils.Color("&c" + Target.getName() + " is no longer in the discord server"));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cOops! An error occurred"));
                    }
                } else {
                    Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cNo player online with the name " + strings[0]));
                }
            } else {
                Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cMissing player argument"));
            }
        } else {
            commandSender.sendMessage(Heartlles.Prefix + Utils.Color("&cPlayer Only Command!"));
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> TabComplete = new ArrayList<>();
        if (strings.length == 1) {
            Bukkit.getOnlinePlayers().forEach((Plr) -> TabComplete.add(Plr.getName()));
        }
        return TabComplete;
    }
}
