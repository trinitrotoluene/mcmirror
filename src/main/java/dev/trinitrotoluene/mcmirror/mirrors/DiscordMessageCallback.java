package dev.trinitrotoluene.mcmirror.mirrors;

import co.aikar.commands.BukkitCommandManager;
import dev.trinitrotoluene.mcmirror.MirrorPlugin;
import dev.trinitrotoluene.mcmirror.util.AttachmentMessageGenerator;
import dev.trinitrotoluene.mcmirror.util.DefaultPermConsoleSender;
import dev.trinitrotoluene.mcmirror.util.MessageBroadcaster;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Channel;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Snowflake;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.data.type.Snow;
import org.bukkit.block.structure.Mirror;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class DiscordMessageCallback implements MessageCallback {
    public static final String mentionRegex = "<@!?(\\d+)>";
    public static final String emoteRegex = "<a?(:\\w+:)\\d+>";
    public static final String channelMentionRegex = "<#(\\d+)>";
    public static final String roleMentionRegex = "<@&(\\d+)>";
    private static final Pattern mentionPattern = Pattern.compile(mentionRegex);
    private static final Pattern emotePattern = Pattern.compile(emoteRegex);
    private static final Pattern channelPattern = Pattern.compile(channelMentionRegex);
    private static final Pattern rolePattern = Pattern.compile(roleMentionRegex);

    private final MirrorPlugin _plugin;
    private final DefaultPermConsoleSender _defaultSender;
    private final FileConfiguration _config;

    public DiscordMessageCallback(MirrorPlugin plugin, DefaultPermConsoleSender defaultSender) {
        this._plugin = plugin;
        this._defaultSender = defaultSender;
        this._config = this._plugin.getConfig();
    }

    @Override
    public void onMessage(MessageCreateEvent message) {
        message.getMember().ifPresent(member -> {
            String content;
            var prefix = this._config.getString("bot.prefix", "mc/");
            if (this._config.getBoolean("bot.modules.remote-execution", false) && (content = message.getMessage().getContent().orElse("")).startsWith(prefix)) {
                Bukkit.dispatchCommand(this._defaultSender, content.substring(prefix.length()));
                return;
            }

            var formatString = this._config.getString("format.minecraft.user", "&7[D]&f <%user%> %message%");
            formatString = ChatColor.translateAlternateColorCodes('&', formatString);
            String mirroredMessage = formatString
                    .replace("%user%", member.getDisplayName())
                    .replace("%message%", sanitizeContent(message.getClient(), message.getMessage().getContent().orElse("")));

            MessageBroadcaster.broadcast(mirroredMessage);
            for (var attachment : message.getMessage().getAttachments()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), AttachmentMessageGenerator.createTellRawCommand(attachment.getFilename(), attachment.getUrl()));
            }
        });
    }

    private String sanitizeContent(DiscordClient client, String content) {
        var emoteMatcher = emotePattern.matcher(content);
        if (emoteMatcher.find())
            content = emoteMatcher.replaceAll(matchResult -> matchResult.group(1));

        var mentionMatcher = mentionPattern.matcher(content);
        if (mentionMatcher.find()) {
            content = mentionMatcher.replaceAll(matchResult -> {
                try {
                    var guildId = Snowflake.of(this._config.getString("guild_id"));
                    var userId = Snowflake.of(matchResult.group(1));
                    var member = client.getMemberById(guildId, userId).block();

                    return "@" + member.getDisplayName();
                }
                catch (Exception e) {
                    return "@" + matchResult.group(1);
                }
            });
        }

        var channelMatcher = channelPattern.matcher(content);
        if (channelMatcher.find()) {
            content = channelMatcher.replaceAll(matchResult -> {
                try {
                    var channelId = Snowflake.of(matchResult.group(1));
                    var channel = (TextChannel)client.getChannelById(channelId).block();
                    return "#" + channel.getName();
                }
                catch (Exception e) {
                    return "#" + matchResult.group(1);
                }
            });
        }

        var roleMatcher = rolePattern.matcher(content);
        if (roleMatcher.find()){
            content = roleMatcher.replaceAll(matchResult -> {
                try {
                    var roleId = Snowflake.of(matchResult.group(1));
                    var guildId = Snowflake.of(this._config.getString("guild_id"));
                    var role = client.getRoleById(guildId, roleId).block();

                    return "@" + role.getName();
                }
                catch (Exception e) {
                    return "@" + matchResult.group(1);
                }
            });
        }

        return content;
    }
}
