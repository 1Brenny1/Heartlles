package me.brenny.heartlles;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Config {

    public static Object GetConfigObject(String Path, Object Default) {
        Heartlles Pl = Heartlles.Instance;
        Object r = null;
        if (Default.getClass().equals(String.class)) {
            r = Pl.getConfig().getString(Path);
        } else if (Default.getClass().equals(Integer.class)) {
            r = Pl.getConfig().getInt(Path);
        } else if (Default.getClass().equals(Location.class)) {
            r = Pl.getConfig().getLocation(Path);
        } else if (Default.getClass().equals(Boolean.class)) {
            r = Pl.getConfig().getBoolean(Path);
        }

        if (r != null) return r;
        Pl.getConfig().set(Path, Default);
        Pl.saveConfig();
        return Default;
    }

    public static void LoadConfig() {
        Heartlles.Instance.reloadConfig();

        Data.SpawnLocation = (Location) GetConfigObject("SpawnLocation", new Location(Bukkit.getWorld("world"), 0, 64, 0));
        Data.RTPRadius = (Integer) GetConfigObject("RTPRadius", 20000);
        Data.RTPWorld = (String) GetConfigObject("RTPWorld", "world");
    }
    public class Data {
        public static Location SpawnLocation;
        public static Integer RTPRadius;
        public static String RTPWorld;
    }
}
