package dev.trinitrotoluene.mcmirror;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class DiscordMessageBinding {
    private PluginCore Plugin;
    private DiscordClient Client;
    private volatile boolean Enabled;

    private final MessageCallback Callback = (message) -> {
        message.getMember().ifPresent(member -> {
            message.getMessage().getContent().ifPresent(content -> {
                String mirroredMessage = String.format("%s%s: %s",
                    ChatColor.translateAlternateColorCodes('&', this.Plugin.getConfig().getString("prefix")),
                    member.getDisplayName(),
                    content);
                Bukkit.broadcastMessage(mirroredMessage);
            });
        });
    };

    public DiscordMessageBinding(PluginCore plugin) {
        this.Enabled = true;
        this.Plugin = plugin;
    }

    public void setEnabled(boolean value) {
        this.Enabled = value;
    }

    public void bindAndBroadcast() {
        Bukkit.getScheduler().runTaskAsynchronously(this.Plugin, () -> {
            this.Client = new DiscordClientBuilder(this.Plugin.getConfig().getString("token"))
                    .build();

            this.Client.getEventDispatcher().on(ReadyEvent.class).subscribe((ready) -> {
                this.Plugin.getLogger().info("Connected to Discord");
            });

            this.Client.getEventDispatcher()
                    .on(MessageCreateEvent.class)
                    .filter((msg) -> this.Enabled)
                    .filter(msg -> !msg.getMessage().getContent().isEmpty())
                    .filter(msg -> msg.getMessage().getContent().get().length() < 500)
                    .subscribe(msg -> {
                        Bukkit.getScheduler()
                            .runTask(this.Plugin, () -> this.Callback.onMessage(msg));
                    });

            this.Client.login().block();
        });
    }

    public void close() {
        if (this.Client != null)
            this.Client.logout().block();

        this.Client = null;
        this.Plugin = null;
    }
}
