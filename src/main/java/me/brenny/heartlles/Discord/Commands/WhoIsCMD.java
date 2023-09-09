package me.brenny.heartlles.Discord.Commands;

import me.brenny.heartlles.Discord.DiscordCMD;
import me.brenny.heartlles.SQL;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.util.UUID;

public class WhoIsCMD implements DiscordCMD {
    @Override
    public SlashCommandData getCommand() {
        return Commands.slash("whois", "get a player's minecraft username from their discord").addOption(OptionType.USER, "member","User you want to get the minecraft of",true);
    }

    @Override
    public void onExecute(SlashCommandInteractionEvent e) {
        User Target = e.getOption("member").getAsUser();
        ResultSet Result = SQL.ExSQL_Result("SELECT * FROM Discord WHERE Discord=" + Target.getId());
        try {
            String MCUUID = Result.getString("Minecraft");
            if (MCUUID != null) {
                OfflinePlayer MCUser = Bukkit.getOfflinePlayer(UUID.fromString(MCUUID));
                if (MCUser != null) {
                    e.reply(Target.getAsMention() + "'s minecraft username is **" + MCUser.getName() + "**").queue();
                } else {
                    e.reply(Target.getAsMention() + "'s account is not linked").queue();
                }
            } else {
                e.reply(Target.getAsMention() + "'s account is not linked").queue();
            }
        } catch (Exception _e) {
            _e.printStackTrace();
            e.reply("Oops! An Error Occurred").queue();
        }
    }
}
