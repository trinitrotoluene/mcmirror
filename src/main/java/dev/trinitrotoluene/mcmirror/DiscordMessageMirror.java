package dev.trinitrotoluene.mcmirror;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Objects;

public class DiscordMessageMirror {
    private PluginCore Plugin;
    private DiscordClient Client;
    private volatile boolean Enabled;

    private final MessageCallback Callback = (message) -> {
        message.getMember().ifPresent(member -> {
            message.getMessage().getContent().ifPresent(content -> {
                String mirroredMessage = Objects.requireNonNull(this.Plugin.getConfig().getString("format"))
                        .replace("user", member.getDisplayName())
                        .replace("message", content)
                        .replace("@", "@\u200b");
                mirroredMessage = ChatColor.translateAlternateColorCodes('&', mirroredMessage);
                Bukkit.broadcastMessage(mirroredMessage);
            });
        });
    };

    public DiscordMessageMirror(PluginCore plugin) {
        this.Enabled = true;
        this.Plugin = plugin;
    }

    public void setEnabled(boolean value) {
        this.Enabled = value;
    }

    public String getStatus() {
        if (this.Client == null)
            return ChatColor.RED + "Client failed to initialise?";

        else if (this.Client.isConnected()) {
            return ChatColor.GREEN + String.format("Client is connected with a response time of %dms.", this.Client.getResponseTime());
        }
        else {
            return ChatColor.RED + "Client is disconnected.";
        }
    }

    public void bindAndBroadcast() {
        Bukkit.getScheduler().runTaskAsynchronously(this.Plugin, () -> {
            this.Client = new DiscordClientBuilder(Objects.requireNonNull(this.Plugin.getConfig().getString("token")))
                    .build();

            this.Client.getEventDispatcher().on(ReadyEvent.class).subscribe((ready) -> {
                this.Plugin.getLogger().info("Discord -> Minecraft online");
            });

            this.Client.getEventDispatcher()
                    .on(MessageCreateEvent.class)
                    .filter((msg) -> this.Enabled)
                    .filter(msg -> msg.getMessage().getContent().isPresent())
                    .filter(msg -> msg.getMessage().getContent().get().length() <= 500)
                    .filter(msg -> {
                        var whitelist = this.Plugin.getWhitelistedRoles();
                        if (whitelist.size() == 0)
                            return true;
                        else
                            return whitelist.stream().anyMatch(rname -> {
                               var member = msg.getMember().orElse(null);
                               if (member == null)
                                   return true;
                               else
                                   return member.getRoleIds().stream().anyMatch(rid -> rid.asString().equals(rname));
                            });
                    })
                    .filter(msg -> {
                        var whitelist = this.Plugin.getWhitelistedChannels();
                        if (whitelist.size() == 0)
                            return true;
                        else
                            return whitelist.stream().anyMatch(cname -> cname.equals(msg.getMessage().getChannelId().asString()));
                    })
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
