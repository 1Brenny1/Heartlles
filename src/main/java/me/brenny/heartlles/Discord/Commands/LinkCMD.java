package me.brenny.heartlles.Discord.Commands;

import me.brenny.heartlles.Discord.DiscordCMD;
import me.brenny.heartlles.Heartlles;
import me.brenny.heartlles.SQL;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.util.UUID;

public class LinkCMD implements DiscordCMD {
    @Override
    public SlashCommandData getCommand() {
        return Commands.slash("link","link your discord to your minecraft account").addOption(OptionType.NUMBER, "code","Insert your linking code here to link your account", true);
    }

    @Override
    public void onExecute(SlashCommandInteractionEvent e) {
        if (e.getOption("code") == null) {
            e.reply("Missing Linking Code").setEphemeral(true).queue();
            return;
        }
        ResultSet Result = SQL.ExSQL_Result("SELECT * From Discord WHERE LinkCode=" + e.getOption("code").getAsDouble());
        try {
            ResultSet R2 = SQL.ExSQL_Result("SELECT * FROM Discord WHERE Discord=" + e.getUser().getId());
            if (R2.getString("Minecraft") != null) {
                e.reply("Your account has all ready been linked").setEphemeral(true).queue();
                return;
            }
            if (Result.getString("Minecraft") != null) {
                SQL.ExSQL("UPDATE Discord SET Discord=" + e.getUser().getId() + ", LinkCode=-1 WHERE LinkCode=" + e.getOption("code").getAsDouble());
                e.getGuild().addRoleToMember(UserSnowflake.fromId(e.getUser().getId()), e.getGuild().getRoleById(1147200915875561542L)).queue();
                e.reply("Successfully linked to minecraft account **" + Bukkit.getOfflinePlayer(UUID.fromString(Result.getString("Minecraft"))).getName() +"**").setEphemeral(true).queue();
            } else {
                e.reply("Invalid Linking Code").setEphemeral(true).queue();
            }
        } catch (Exception _e) {
            _e.printStackTrace();
            e.reply("Oops! An Error Occurred").queue();
        }
    }
}
