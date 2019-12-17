package dev.trinitrotoluene.mcmirror;

import discord4j.core.event.domain.message.MessageCreateEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class DiscordMessageCallback implements MessageCallback {
    private final MirrorPlugin _plugin;

    DiscordMessageCallback() {
        this._plugin = JavaPlugin.getPlugin(MirrorPlugin.class);
    }

    @Override
    public void onMessage(MessageCreateEvent message) {
        message.getMember().ifPresent(member -> message.getMessage().getContent().ifPresent(content -> {
            String mirroredMessage = Objects.requireNonNull(this._plugin.getConfig().getString("format"))
                    .replace("user", member.getDisplayName())
                    .replace("message", content)
                    .replace("@", "@\u200b");
            mirroredMessage = ChatColor.translateAlternateColorCodes('&', mirroredMessage);
            Bukkit.broadcastMessage(mirroredMessage);
        }));
    }
}
