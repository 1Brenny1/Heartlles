package me.brenny.heartlles.Commands;

import me.brenny.heartlles.Discord.DiscordMain;
import me.brenny.heartlles.Heartlles;
import me.brenny.heartlles.Objects.PlayerData;
import me.brenny.heartlles.SQL;
import me.brenny.heartlles.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LinkCmd implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player Plr) {
            ResultSet Result = SQL.ExSQL_Result("SELECT * FROM Discord WHERE Minecraft='"+ Plr.getUniqueId() +"'");
            try {
                if (Result.getString("Minecraft") == null) {
                    Integer Code = -1;
                    while (true) {
                        Code = new Random().nextInt(111111,999999);
                         ResultSet Test = SQL.ExSQL_Result("SELECT * FROM Discord WHERE LinkCode=" + Code);
                         if (Test.getInt("LinkCode") == 0) break;
                    }
                    SQL.ExSQL("INSERT INTO Discord (Minecraft, LinkCode) VALUES ('" + Plr.getUniqueId() +"', "+Code+")");
                    Plr.sendMessage(Heartlles.Prefix + Utils.Color("Use &a/Link " + Code + "&r in the discord to link your account"));
                } else if (Result.getLong("Discord") == 0){
                    Plr.sendMessage(Heartlles.Prefix + Utils.Color("Use &a/Link " + Result.getInt("LinkCode") + "&r in the discord to link your account"));
                } else {
                    Plr.sendMessage(Heartlles.Prefix + Utils.Color("&CYour account has all ready been linked"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Plr.sendMessage(Heartlles.Prefix + Utils.Color("&cOops! An error occurred"));
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return new ArrayList<>();
    }
}
