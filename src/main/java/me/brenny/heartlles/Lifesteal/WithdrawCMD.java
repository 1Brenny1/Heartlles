package me.brenny.heartlles.Lifesteal;

import me.brenny.heartlles.Heartlles;
import me.brenny.heartlles.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WithdrawCMD implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player Plr) {
            Integer Amnt = 1;
            if (strings.length > 0) {
                try { Amnt = Integer.parseInt(strings[0]); } catch (Exception e) {}
            }

            if (Heartlles.PlrData.get(Plr.getUniqueId()).Hearts <= 2 || Heartlles.PlrData.get(Plr.getUniqueId()).Hearts-(Amnt*2) < 2) {
                Plr.sendMessage(Utils.Color("&cYou cannot withdraw all of your hearts"));
                return true;
            }
            for(int i = 0; i < Amnt; i++) {
                Heartlles.PlrData.get(Plr.getUniqueId()).Hearts -= 2;
                Plr.getInventory().addItem(LsMain.getHeart());
                LsMain.SetMaxHealth(Plr, Heartlles.PlrData.get(Plr.getUniqueId()).Hearts);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        Player Plr = (Player) commandSender;

        List<String> TabComplete = new ArrayList<>();
        if (strings.length == 1) {
            for (int i = 1; i < Heartlles.PlrData.get(Plr.getUniqueId()).Hearts/2; i++) {
                TabComplete.add(i + "");
            }
        }
        return TabComplete;
    }
}
