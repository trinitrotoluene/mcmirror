package dev.trinitrotoluene.mcmirror.mirrors;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static dev.trinitrotoluene.mcmirror.mirrors.DiscordMessageCallback.*;

public final class PatternTests {
    private static final Pattern mentionPattern = Pattern.compile(mentionRegex);
    private static final Pattern emotePattern = Pattern.compile(emoteRegex);
    private static final Pattern channelPattern = Pattern.compile(channelMentionRegex);
    private static final Pattern rolePattern = Pattern.compile(roleMentionRegex);

    @Test
    void ensureReplacesEmoji() {
        var matcher = emotePattern.matcher("<:mmlol:251009518631256064>");
        matcher.find();
        assertEquals(":mmlol:", matcher.group(1));
    }

    @Test
    void ensureReplacesChannel() {
        var matcher = channelPattern.matcher("<#251009518631256064>");
        matcher.find();
        assertEquals("251009518631256064", matcher.group(1));
    }

    @Test
    void ensureReplacesUserA() {
        var matcher = mentionPattern.matcher("<@!251009518631256064>");
        matcher.find();
        assertEquals("251009518631256064", matcher.group(1));
    }

    @Test
    void ensureReplacesUserB() {
        var matcher = mentionPattern.matcher("<@251009518631256064>");
        matcher.find();
        assertEquals("251009518631256064", matcher.group(1));
    }

    @Test
    void ensureReplacesRole() {
        var matcher = rolePattern.matcher("<@&251009518631256064>");
        matcher.find();
        assertEquals("251009518631256064", matcher.group(1));
    }
}
