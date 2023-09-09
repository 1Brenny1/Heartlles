package me.brenny.heartlles.Discord.Commands;

import me.brenny.heartlles.Discord.DiscordCMD;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;

public class InfoCMD implements DiscordCMD {

    @Override
    public SlashCommandData getCommand() {
        return Commands.slash("info","View info about the bot");
    }

    @Override
    public void onExecute(SlashCommandInteractionEvent e) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Heartlles Bot Info");
        builder.setColor(Color.red);
        builder.setDescription("The Heartlles bot and Heartlles Plugin have both been designed specifically for the Heartlles Minecraft Server.\nBoth are made by: 1Brenny1 @ Brenny.tk");
        MessageEmbed embed = builder.build();
        e.replyEmbeds(embed).queue();
    }
}
