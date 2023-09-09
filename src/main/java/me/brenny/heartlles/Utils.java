package me.brenny.heartlles;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import net.milkbowl.vault.chat.Chat;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]){6}");
    public static String Color(String message){
        Matcher matcher = hexPattern.matcher(message);
        while (matcher.find()) {
            final ChatColor hexColor = ChatColor.of(matcher.group().replace("&", ""));
            final String before = message.substring(0, matcher.start());
            final String after = message.substring(matcher.end());
            message = before + hexColor + after;
            matcher = hexPattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static String Format(Double Num) {
        String Data = "No,30|O,27|SP,24|SX,21|QT,18|Q,15|T,12|B,9|M,6|k,3";
        for (String Part : Data.split("\\|")) { // Escape the pipe character
            String[] Parts = Part.split(",");
            if (Num >= Math.pow(10, Integer.parseInt(Parts[1]))) {
                return new DecimalFormat("0.00").format(Num / Math.pow(10, Integer.parseInt(Parts[1]))) + Parts[0];
            }
        }
        return Num.toString();
    }

    public static boolean SetupVault() {
        if (Heartlles.Instance.getServer().getPluginManager().getPlugin("Vault") == null) return false;

        RegisteredServiceProvider<Chat> ChatRSP = Heartlles.Instance.getServer().getServicesManager().getRegistration(Chat.class);
        RegisteredServiceProvider<Economy> EcoRSP = Heartlles.Instance.getServer().getServicesManager().getRegistration(Economy.class);

        if (ChatRSP == null || EcoRSP == null) return false;
        Vault.chat = ChatRSP.getProvider();
        Vault.eco = EcoRSP.getProvider();
        return Vault.chat != null && Vault.eco != null;
    }

    public static class Vault {
        public static Chat chat;
        public static Economy eco;
    }
}
