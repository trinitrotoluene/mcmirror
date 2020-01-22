package dev.trinitrotoluene.mcmirror.mirrors;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DiscordMessageMirrorPresenceListener implements Listener {
    private final DiscordMessageMirror _discordMirror;

    public DiscordMessageMirrorPresenceListener(DiscordMessageMirror discordMirror) {
        this._discordMirror = discordMirror;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        this._discordMirror.updateMOTDInPresence();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        this._discordMirror.updateMOTDInPresence();
    }
}
