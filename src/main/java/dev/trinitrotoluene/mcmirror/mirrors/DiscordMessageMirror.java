package dev.trinitrotoluene.mcmirror.mirrors;

import dev.trinitrotoluene.mcmirror.MirrorPlugin;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Channel;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.Role;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.gateway.json.dispatch.MessageCreate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.Optional;

public class DiscordMessageMirror {
    private MirrorPlugin _plugin;
    private DiscordClient _client;
    private volatile boolean _enabled;

    private MessageCallback _callback;

    public DiscordMessageMirror() {
        this._enabled = true;
        this._plugin = JavaPlugin.getPlugin(MirrorPlugin.class);
    }

    public void setEnabled(boolean value) {
        this._enabled = value;
    }

    public DiscordClient getClient() {
        return this._client;
    }

    public void close() {
        if (this._client != null)
            this._client.logout().block();

        this._client = null;
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
            var token = this._plugin.getConfig().getString("bot.token");
            if (token == null)
                return;

            this._client = new DiscordClientBuilder(token).build();
            this._callback = new DiscordMessageCallback(this._client, this. _plugin);

            this._client.getEventDispatcher()
                    .on(ReadyEvent.class)
                    .subscribe((ready) -> {
                        this._plugin.getLogger().info("Connected to Discord!");
                        updateMOTDInPresence();
                    });

            this._client.getEventDispatcher()
                    .on(MessageCreateEvent.class)
                    .filter(msg -> this._enabled && isValidMessage(msg))
                    .filter(msg -> {
                        var channel = msg.getMessage().getChannel().block();
                        if (isWhitelistedChannel(channel)) {
                            Optional<Member> member;
                            if ((member = msg.getMember()).isEmpty())
                                return false;

                            return isWhitelistedUser(member.get()) || isWhitelistedRole(member.get());
                        }
                        return false;
                    })
                    .subscribe(msg -> Bukkit.getScheduler()
                            .runTask(this._plugin, () -> this._callback.onMessage(msg))
                    );

            this._client.login().block();
        });
    }

    private boolean isValidMessage(MessageCreateEvent message) {
        var content = message.getMessage().getContent().orElse("");
        return content.length() > 0 && content.length() <= 500;
    }

    private boolean isWhitelistedUser(Member member) {
        var whitelist = this._plugin.getConfig().getStringList("whitelist.users");
        if (whitelist.size() == 0)
            return true;

        for (var userId : whitelist) {
            if (userId.equals(member.getId().asString()))
                return true;
        }

        return false;
    }

    private boolean isWhitelistedChannel(Channel channel) {
        var whitelist = this._plugin.getConfig().getStringList("whitelist.channels");
        if (whitelist.size() == 0)
            return true;

        for (var channelId : whitelist) {
            if (channelId.equals(channel.getId().asString()))
                return true;
        }

        return false;
    }

    private boolean isWhitelistedRole(Member member) {
        var whitelist = this._plugin.getConfig().getStringList("whitelist.roles");
        if (whitelist.size() == 0)
            return true;

        for (var role : member.getRoleIds()) {
            for (var roleId : whitelist) {
                if (roleId.equals(role.asString()))
                    return true;
            }
        }

        return false;
    }

    public void updateMOTDInPresence() {
        var onlineCount = Bukkit.getOnlinePlayers().size();
        var maxPlayerCount = Bukkit.getMaxPlayers();
        var message = String.format("%s/%s players online", onlineCount, maxPlayerCount);
        var newPresence = Presence.online(Activity.playing(message));

        this._client.updatePresence(newPresence).block();
    }
}
