package me.brenny.heartlles.Other;
import me.brenny.heartlles.Heartlles;
import me.brenny.heartlles.Objects.PlayerData;
import me.brenny.heartlles.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public class Sidebar {
    public static void SetScore(Objective Ob, Scoreboard Sb, List<String> Scores) {
        for (Integer i = 0; i < Scores.size(); i++) {
            Team team = Sb.registerNewTeam(i.toString());
            String EntName;
            if (i.toString().length() == 1) EntName = "&#00000" + i;
            else EntName = "&#0000" + i;
            team.setSuffix(Utils.Color(Scores.get(i) + EntName));
            team.addEntry(Utils.Color(EntName));
            Ob.getScore(Utils.Color(EntName)).setScore(15-i);
            //Ob.getScore(Utils.Color(Scores.get(i))).setScore(15-i);
        }
    }
    public static void Init() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Heartlles.Instance, () -> {
            Bukkit.getOnlinePlayers().forEach((Plr)->CreateSidebar(Plr));
        }, 0L, 10L);
    }
    public static void CreateSidebar(Player Plr) {
        Scoreboard Sb = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective Ob = Sb.registerNewObjective("Sidebar",Criteria.DUMMY, "Sidebar");
        Ob.setDisplaySlot(DisplaySlot.SIDEBAR);
        Ob.setDisplayName(Utils.Color("&c&lʜᴇᴀʀᴛʟʟᴇꜱ"));

        PlayerData PlrData = Heartlles.PlrData.get(Plr.getUniqueId());
        SetScore(Ob, Sb, Arrays.asList(
                "  &8◆&m                             &r&8◆",
                "    &c&lᴘʟᴀʏᴇʀ",
                "     &8 ▪ &7ʀᴀɴᴋ: " + Utils.Vault.chat.getPlayerPrefix(Plr),
                "     &8 ▪ &7ʙᴀʟᴀɴᴄᴇ: &c$" + Utils.Format(Utils.Vault.eco.getBalance(Plr)),
                "",
                "    &c&lꜱᴛᴀᴛꜱ",
                "     &8 ▪ &7ᴋɪʟʟꜱ: &c" + PlrData.Kills,
                "     &8 ▪ &7ᴅᴇᴀᴛʜꜱ: &c" + PlrData.Deaths,
                "  &8◆&m                             &r&8◆"
        ));
        Plr.setScoreboard(Sb);
    }

}
