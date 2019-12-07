package dev.trinitrotoluene.mcmirror;

import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MessageListener implements Listener {
    private final PluginCore Plugin;
    private volatile boolean Enabled;

    public MessageListener(PluginCore plugin) {
        this.Plugin = plugin;
        this.Enabled = true;
    }

    public void setEnabled(Boolean value) {
        this.Enabled = value;
    }

    @EventHandler
    public void onPlayerMessage(AsyncPlayerChatEvent playerChatEvent) {
        sendMessage(playerChatEvent.getPlayer().getDisplayName(), playerChatEvent.getMessage());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent playerDeathEvent) {
        sendSystemMessage(playerDeathEvent.getDeathMessage());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        sendSystemMessage(playerJoinEvent.getJoinMessage());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        sendSystemMessage(playerQuitEvent.getQuitMessage());
    }

    private void sendMessage(String username, String content) {
        if (!this.Enabled)
            return;

        var message = new WebhookMessageBuilder()
                .setUsername(username)
                .setContent(content)
                .setAvatarUrl(String.format("https://minotar.net/avatar/%s", username))
                .build();

        this.Plugin.getWebhook().send(message);
    }

    private void sendSystemMessage(String content) {
        if (!this.Enabled)
            return;

        var message = new WebhookMessageBuilder()
                .setUsername("System")
                .setContent(ChatColor.stripColor(String.format("**%s**", content)))
                .setAvatarUrl("https://minotar.net/avatar/Herobrine")
                .build();

        this.Plugin.getWebhook().send(message);
    }
}
