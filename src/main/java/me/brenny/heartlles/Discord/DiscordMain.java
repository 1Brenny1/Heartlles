package me.brenny.heartlles.Discord;

import me.brenny.heartlles.Discord.Commands.DiscordCommands;
import me.brenny.heartlles.Discord.Commands.InfoCMD;
import me.brenny.heartlles.Discord.Listeners.EventListener;
import me.brenny.heartlles.Heartlles;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscordMain implements net.dv8tion.jda.api.hooks.EventListener {
    public static ShardManager shardManager;

    public static List<DiscordCMD> Commands = new ArrayList<>();
    public static void RegisterCMD(DiscordCMD cmd) {
        Commands.add(cmd);
    }

    public static void Init() {
        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault("MTE0NjA3MDYxNjM2NjEzNzM4NA.G8UwBV.jw3ibfOEsCG04Pt6pBYMk_FWEh26wDnHbgEYik");
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.setActivity(Activity.playing("Heartlles"));
        builder.enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));
        shardManager = builder.build();

        shardManager.addEventListener(new EventListener());
        shardManager.addEventListener(new DiscordCommands());
        shardManager.addEventListener(new DiscordMain());

        DiscordCommands.RegisterCommands();
    }
    public static void Shutdown() {
        shardManager.shutdown();
    }
    @Override
    public void onEvent(@NotNull GenericEvent genericEvent) {
        if (genericEvent instanceof ReadyEvent) {
            Heartlles.Log.info("Logged in as " + genericEvent.getJDA().getSelfUser().getName());
        }
    }
}
