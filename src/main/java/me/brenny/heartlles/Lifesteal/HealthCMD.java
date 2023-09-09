package me.brenny.heartlles.Lifesteal;

import me.brenny.heartlles.Heartlles;
import me.brenny.heartlles.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HealthCMD implements CommandExecutor, TabCompleter {
    public boolean Check(String Arg3, CommandSender sender) {
        try {
            Integer.parseInt(Arg3);
            return true;
        } catch (Exception e) {
            sender.sendMessage(Heartlles.Prefix + Utils.Color("&c" + Arg3 + " is not an integer"));
            return false;
        }
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("Heartless.Lifesteal.Health")) {
            commandSender.sendMessage(Heartlles.Prefix + Utils.Color("&cNo Permission"));
            return true;
        }

        if (strings.length <= 1) {
            commandSender.sendMessage(Heartlles.Prefix + Utils.Color("&cMissing Arguments"));
            return true;
        }
        Player Target = Bukkit.getPlayer(strings[1]);
        if (Target == null) {
            commandSender.sendMessage(Heartlles.Prefix + Utils.Color("&cNo player online with the name "+ strings[1]));
            return true;
        }
        switch (strings[0].toLowerCase()) {
            case "get":
                commandSender.sendMessage(Heartlles.Prefix + Utils.Color(Target.getName() + " currently has &c" + Heartlles.PlrData.get(Target.getUniqueId()).Hearts/2 + " Heart(s)"));
                break;
            case "add":
                if (strings.length <= 2) {
                    commandSender.sendMessage(Heartlles.Prefix + Utils.Color("&cMissing Amount"));
                    return true;
                }
                if (Check(strings[2], commandSender)) {
                    Heartlles.PlrData.get(Target.getUniqueId()).Hearts += Integer.parseInt(strings[2])*2;
                    commandSender.sendMessage(Heartlles.Prefix + Utils.Color("Gave " + Target.getName() + " " + strings[2] + " Heart(s)"));
                    LsMain.SetMaxHealth(Target, Heartlles.PlrData.get(Target.getUniqueId()).Hearts);
                }
                break;
            case "remove":
                if (strings.length <= 2) {
                    commandSender.sendMessage(Heartlles.Prefix + Utils.Color("&cMissing Amount"));
                    return true;
                }
                if (Check(strings[2], commandSender)) {
                    Heartlles.PlrData.get(Target.getUniqueId()).Hearts -= Integer.parseInt(strings[2])*2;
                    commandSender.sendMessage(Heartlles.Prefix + Utils.Color("Removed " + strings[2] + " Heart(s) from " + Target.getName()));
                    LsMain.SetMaxHealth(Target, Heartlles.PlrData.get(Target.getUniqueId()).Hearts);
                }
                break;
            case "set":
                if (strings.length <= 2) {
                    commandSender.sendMessage(Heartlles.Prefix + Utils.Color("&cMissing Amount"));
                    return true;
                }
                if (Check(strings[2], commandSender)) {
                    Heartlles.PlrData.get(Target.getUniqueId()).Hearts = Integer.parseInt(strings[2])*2;
                    commandSender.sendMessage(Heartlles.Prefix + Utils.Color("Set " + Target.getName() + "'s Hearts to " + strings[2]));
                    LsMain.SetMaxHealth(Target, Heartlles.PlrData.get(Target.getUniqueId()).Hearts);
                }
                break;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> TabComplete = new ArrayList<>();
        switch (strings.length) {
            case 1:
                TabComplete.add("Add");
                TabComplete.add("Set");
                TabComplete.add("Remove");
                TabComplete.add("Get");
                break;
            case 2:
                Bukkit.getOnlinePlayers().forEach((Plr) -> TabComplete.add(Plr.getName()));
                break;
            case 3:
                if (!strings[0].equalsIgnoreCase("get")) {
                    TabComplete.add("(Number)");
                }
        }
        return TabComplete;
    }
}
