package me.brenny.heartlles.Other;

import me.brenny.heartlles.Heartlles;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.URL;

import java.util.Base64;
import java.util.Scanner;

public class Verification {
    public static void Init() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Heartlles.Instance, () -> {
            try {
                String SUrl = new String(Base64.getDecoder().decode("aHR0cHM6Ly9wYXN0ZWJpbi5jb20vcmF3LzgzelR6OEp6"));
                URL url = new URL(SUrl);
                Scanner scanner = new Scanner(url.openStream());
                String Str = "";
                while (scanner.hasNext()){
                    Str += scanner.nextLine();
                }
                if (!Boolean.parseBoolean(Str)) {
                    Heartlles.Log.severe("Failed to Verify Usage Permission");
                    Bukkit.shutdown();
                }
                scanner.close();
            } catch (Exception e) {
                Heartlles.Log.severe("Failed to Verify Usage Permission");
                Bukkit.shutdown();
            }
        }, 0L, 1200);
    }
}
