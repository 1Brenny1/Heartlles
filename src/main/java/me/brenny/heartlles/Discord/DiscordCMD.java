package me.brenny.heartlles.Discord;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public interface DiscordCMD {
    SlashCommandData getCommand();
    void onExecute(SlashCommandInteractionEvent e);
}
