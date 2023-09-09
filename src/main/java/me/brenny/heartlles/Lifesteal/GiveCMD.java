package me.brenny.heartlles.Lifesteal;

import me.brenny.heartlles.Heartlles;
import me.brenny.heartlles.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GiveCMD implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("Heartless.Lifesteal.Give")) {
            commandSender.sendMessage(Heartlles.Prefix + Utils.Color("&cNo Permission"));
            return true;
        }
        if (strings.length == 0) {
            commandSender.sendMessage(Heartlles.Prefix + Utils.Color("&cMissing arguments"));
            return true;
        }
        Integer Amnt = 1;
        if (strings.length > 1) {
            try {
                Amnt = Integer.parseInt(strings[1]);
            } catch (Exception e) {
                commandSender.sendMessage(Heartlles.Prefix + Utils.Color("&c" + strings[1] + " is not an integer"));
                return true;
            }
        }
        if (!(commandSender instanceof Player Plr)) {
            commandSender.sendMessage(Heartlles.Prefix + Utils.Color("&cPlayer only command"));
            return true;
        }
        switch (strings[0].toLowerCase()) {
            case "heart":
                ItemStack Heart = LsMain.getHeart();
                Heart.setAmount(Amnt);
                Plr.getInventory().addItem(Heart);
                Plr.sendMessage(Heartlles.Prefix + Utils.Color("Gave you " + Amnt + " Heart(s)"));
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> TabComplete = new ArrayList<>();
        switch (strings.length) {
            case 1:
                TabComplete.add("Heart");
                break;
            case 2:
                TabComplete.add("(Integer)");
                break;
        }
        return TabComplete;
    }
}
