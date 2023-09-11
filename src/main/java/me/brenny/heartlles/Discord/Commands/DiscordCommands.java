package me.brenny.heartlles.Discord.Commands;

import me.brenny.heartlles.Discord.DiscordCMD;
import me.brenny.heartlles.Discord.DiscordMain;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class DiscordCommands extends ListenerAdapter {

    public static void RegisterCommands() {
        DiscordMain.RegisterCMD(new InfoCMD());
        DiscordMain.RegisterCMD(new LinkCMD());
        DiscordMain.RegisterCMD(new WhoIsCMD());
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        if (e.getUser().isBot() || !e.isFromGuild()) return;
        for (DiscordCMD Cmd : DiscordMain.Commands) {
            if (!Cmd.getCommand().getName().equalsIgnoreCase(e.getName())) continue;
            Cmd.onExecute(e);
            break;
        }
    }
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent e) {
        List<CommandData> Commands = new ArrayList<>();
        DiscordMain.Commands.forEach((Cmd) -> Commands.add(Cmd.getCommand()));
        e.getGuild().updateCommands().addCommands(Commands).queue();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent e) {
        List<CommandData> Commands = new ArrayList<>();
        DiscordMain.Commands.forEach((Cmd) -> Commands.add(Cmd.getCommand()));
        e.getGuild().updateCommands().addCommands(Commands).queue();
    }
}
