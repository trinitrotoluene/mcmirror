package dev.trinitrotoluene.mcmirror.mirrors;

import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import dev.trinitrotoluene.mcmirror.WebhookProvider;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MinecraftMessageMirror implements Listener {
    private volatile boolean _enabled;
    private final WebhookProvider _webhookProvider;

    public MinecraftMessageMirror(WebhookProvider webhookProvider) {
        this._webhookProvider = webhookProvider;
        this._enabled = true;
    }

    public void setEnabled(Boolean value) {
        this._enabled = value;
    }

    public void close() {
        this.setEnabled(false);
        this._webhookProvider.close();
    }

    @EventHandler
    public void onPlayerMessage(AsyncPlayerChatEvent playerChatEvent) {
        if (!playerChatEvent.getPlayer().hasPermission("mcmirror.mirror"))
            return;

        sendMessage(playerChatEvent.getPlayer().getDisplayName(), playerChatEvent.getMessage());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent playerDeathEvent) {
        if (!playerDeathEvent.getEntity().hasPermission("mcmirror.mirror"))
            return;

        sendSystemMessage(playerDeathEvent.getDeathMessage());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        if (!playerJoinEvent.getPlayer().hasPermission("mcmirror.mirror"))
            return;

        sendSystemMessage(playerJoinEvent.getJoinMessage());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        if (!playerQuitEvent.getPlayer().hasPermission("mcmirror.mirror"))
            return;

        sendSystemMessage(playerQuitEvent.getQuitMessage());
    }

    private void sendMessage(String username, String content) {
        if (!this._enabled)
            return;

        var message = new WebhookMessageBuilder()
                .setUsername(username)
                .setContent(content)
                .setAvatarUrl(String.format("https://minotar.net/avatar/%s", username))
                .build();

        this._webhookProvider.execute(message);
    }

    private void sendSystemMessage(String content) {
        if (!this._enabled)
            return;

        var message = new WebhookMessageBuilder()
                .setUsername("System")
                .setContent(ChatColor.stripColor(String.format("**%s**", content)))
                .setAvatarUrl("https://minotar.net/avatar/Herobrine")
                .build();

        this._webhookProvider.execute(message);
    }
}
