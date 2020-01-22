package dev.trinitrotoluene.mcmirror.mirrors;

import dev.trinitrotoluene.mcmirror.MirrorPlugin;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Channel;
import discord4j.core.object.entity.TextChannel;
import discord4j.core.object.util.Snowflake;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.data.type.Snow;
import org.bukkit.block.structure.Mirror;
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
    private final FileConfiguration _config;
    private final DiscordClient _discordClient;

    DiscordMessageCallback(DiscordClient discordClient, MirrorPlugin plugin) {
        _discordClient = discordClient;
        this._plugin = plugin;
        this._config = this._plugin.getConfig();
    }

    @Override
    public void onMessage(MessageCreateEvent message) {
        message.getMember().ifPresent(member -> message.getMessage().getContent().ifPresent(content -> {
            var formatString = this._config.getString("format.minecraft.user", "&7[D]&f <%user%> %message%");
            formatString = ChatColor.translateAlternateColorCodes('&', formatString);

            String mirroredMessage = formatString
                    .replace("%user%", member.getDisplayName())
                    .replace("%message%", sanitizeContent(content));

            Bukkit.broadcastMessage(mirroredMessage);
        }));
    }

    private String sanitizeContent(String content) {
        var emoteMatcher = emotePattern.matcher(content);
        if (emoteMatcher.find())
            content = emoteMatcher.replaceAll(matchResult -> matchResult.group(1));

        var mentionMatcher = mentionPattern.matcher(content);
        if (mentionMatcher.find()) {
            content = mentionMatcher.replaceAll(matchResult -> {
                try {
                    var guildId = Snowflake.of(this._config.getString("guild_id"));
                    var userId = Snowflake.of(matchResult.group(1));
                    var member = this._discordClient.getMemberById(guildId, userId).block();

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
                    var channel = (TextChannel)this._discordClient.getChannelById(channelId).block();
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
                    var role = this._discordClient.getRoleById(guildId, roleId).block();

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
