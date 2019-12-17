package dev.trinitrotoluene.mcmirror.mirrors;

import dev.trinitrotoluene.mcmirror.MirrorPlugin;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class DiscordMessageMirror {
    private MirrorPlugin _plugin;
    private DiscordClient _client;
    private volatile boolean _enabled;

    private final MessageCallback _callback;

    public DiscordMessageMirror() {
        this._enabled = true;
        this._plugin = JavaPlugin.getPlugin(MirrorPlugin.class);

        this._callback = new DiscordMessageCallback();
    }

    public void setEnabled(boolean value) {
        this._enabled = value;
    }

    public String getStatus() {
        if (this._client == null)
            return ChatColor.RED + "Client failed to initialise.";

        else if (this._client.isConnected()) {
            return ChatColor.GREEN + String.format("Client is connected with a response time of %dms.", this._client.getResponseTime());
        }
        else {
            return ChatColor.RED + "Client is disconnected.";
        }
    }

    public void bindAndBroadcast() {
        Bukkit.getScheduler().runTaskAsynchronously(this._plugin, () -> {
            this._client = new DiscordClientBuilder(Objects.requireNonNull(this._plugin.getConfig().getString("token")))
                    .build();

            this._client.getEventDispatcher().on(ReadyEvent.class).subscribe((ready) -> this._plugin.getLogger().info("Discord -> Minecraft online"));

            this._client.getEventDispatcher()
                    .on(MessageCreateEvent.class)
                    .filter(msg -> this._enabled)
                    .filter(msg -> {
                        var content = msg.getMessage().getContent().orElse("");
                        return content.length() > 0 && content.length() <= 500;
                    })
                    .filter(msg -> {
                        var whitelist = this._plugin.getWhitelistedRoles();
                        if (whitelist.size() == 0)
                            return true;
                        else
                            return whitelist.stream().anyMatch(rname -> {
                               var member = msg.getMember().orElse(null);
                               if (member == null)
                                   return false;
                               else
                                   return member.getRoleIds().stream().anyMatch(rid -> rid.asString().equals(rname));
                            });
                    })
                    .filter(msg -> {
                        var whitelist = this._plugin.getWhitelistedChannels();
                        if (whitelist.size() == 0)
                            return true;
                        else
                            return whitelist.stream().anyMatch(cname -> cname.equals(msg.getMessage().getChannelId().asString()));
                    })
                    .subscribe(msg -> Bukkit.getScheduler()
                        .runTask(this._plugin, () -> this._callback.onMessage(msg)));

            this._client.login().block();
        });
    }

    public void close() {
        if (this._client != null)
            this._client.logout().block();

        this._client = null;
        this._plugin = null;
    }
}
