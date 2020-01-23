package dev.trinitrotoluene.mcmirror.mirrors;

import dev.trinitrotoluene.mcmirror.MirrorPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DiscordMessageMirrorPresenceListener implements Listener {
    private final DiscordMessageMirror _discordMirror;
    private final MirrorPlugin _plugin;

    public DiscordMessageMirrorPresenceListener(DiscordMessageMirror discordMirror, MirrorPlugin plugin) {
        this._discordMirror = discordMirror;
        this._plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        scheduleUpdatePresence();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        scheduleUpdatePresence();
    }

    private void scheduleUpdatePresence() {
        Bukkit.getScheduler().runTaskAsynchronously(this._plugin, this._discordMirror::updateMOTDInPresence);
    }
}
