package dev.trinitrotoluene.mcmirror.mirrors;

import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import dev.trinitrotoluene.mcmirror.WebhookProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.BroadcastMessageEvent;

public class MinecraftMessageMirror implements Listener {
    private volatile boolean _enabled;
    private final WebhookProvider _webhookProvider;
    private final FileConfiguration _config;

    public MinecraftMessageMirror(WebhookProvider webhookProvider, FileConfiguration config) {
        this._webhookProvider = webhookProvider;
        this._config = config;
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
    public void onKick(PlayerKickEvent kickEvent) {
        sendSystemMessage(kickEvent.getPlayer().getName() + " was kicked from the server.");
    }

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent preprocessEvent) {
        if (!preprocessEvent.getPlayer().hasPermission("mcmirror.mirror"))
            return;

        var command = preprocessEvent.getMessage().substring(1).split(" ", 2);
        if (command.length != 2) return;
        /* I hate this */
        if (command[0].equals("me")) {
            sendMessage(preprocessEvent.getPlayer().getName(), "\\* " + preprocessEvent.getPlayer().getName() + " " + command[1]);
        }
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

    public void sendMessage(String username, String content) {
        if (!this._enabled)
            return;

        content = this._config.getString("format.discord.user", "%message%")
                .replace("%message%", content);
        var avatarUrl = this._config.getString("webhook.appearance.player.avatar", "https://minotar.net/avatar/%user%")
                .replace("%user%", username);

        var message = new WebhookMessageBuilder()
                .setUsername(username)
                .setContent(sanitize(content))
                .setAvatarUrl(avatarUrl)
                .build();

        this._webhookProvider.execute(message);
    }

    public void sendSystemMessage(String content) {
        if (!this._enabled)
            return;

        content = this._config.getString("format.discord.system", "**%message%**")
            .replace("%message%", content);

        var name = this._config.getString("webhook.appearance.system.name", "Server");
        var url = this._config.getString("webhook.appearance.system.avatar", "https://minotar.net/avatar/Herobrine");

        var embed = new WebhookEmbedBuilder()
                .setDescription(sanitize(content));

        var message = new WebhookMessageBuilder()
                .setUsername(name)
                .setAvatarUrl(url)
                .addEmbeds(embed.build())
                .build();

        this._webhookProvider.execute(message);
    }

    private String sanitize(String content) {
        content = ChatColor.stripColor(content);
        content = content.replace("@", "@\u200b");
        return content;
    }
}
