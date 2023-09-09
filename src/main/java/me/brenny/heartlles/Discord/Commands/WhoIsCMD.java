package me.brenny.heartlles.Discord.Commands;

import me.brenny.heartlles.Discord.DiscordCMD;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class WhoIsCMD implements DiscordCMD {
    @Override
    public SlashCommandData getCommand() {
        return Commands.slash("whois", "get a player's minecraft username from their discord").addOption(OptionType.USER, "member","User you want to get the minecraft of",true);
    }

    @Override
    public void onExecute(SlashCommandInteractionEvent e) {
        User Target = e.getOption("member").getAsUser();

    }
}
