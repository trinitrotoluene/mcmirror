package dev.trinitrotoluene.mcmirror.util;

import org.bukkit.Bukkit;

public class MessageBroadcaster {
    public static void broadcast(String content) {
        var players = Bukkit.getOnlinePlayers();
        for (var player : players) {
            player.sendMessage(content);
        }
    }
}
