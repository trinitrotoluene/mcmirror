package dev.trinitrotoluene.mcmirror;

import club.minnced.discord.webhook.WebhookClient;
import dev.trinitrotoluene.mcmirror.mirrors.DiscordMessageMirror;
import dev.trinitrotoluene.mcmirror.mirrors.MinecraftMessageMirror;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class MirrorPlugin extends JavaPlugin {
    private DiscordMessageMirror DiscordMirror;
    private MinecraftMessageMirror MinecraftMirror;

    public DiscordMessageMirror getDiscordMessageMirror() {
        return this.DiscordMirror;
    }

    public void setDiscordMessageMirror(DiscordMessageMirror value) {
        this.DiscordMirror = value;
    }

    public MinecraftMessageMirror getMinecraftMessageMirror() {
        return this.MinecraftMirror;
    }

    public void setMinecraftMessageMirror(MinecraftMessageMirror value) {
        this.MinecraftMirror = value;
    }

    public List<String> getWhitelistedRoles() {
        return getConfig().getStringList("roles");
    }

    public List<String> getWhitelistedChannels() {
        return getConfig().getStringList("channels");
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        var webhookUrls = getConfig().getStringList("webhooks");

        var hookList = new ArrayList<WebhookClient>();
        for (var url : webhookUrls)
            hookList.add(WebhookClient.withUrl(url));
        this.MinecraftMirror = new MinecraftMessageMirror(hookList);

        getServer().getPluginManager().registerEvents(this.MinecraftMirror, this);
        getLogger().info(String.format("Minecraft -> Discord online with %s hooks", webhookUrls.size()));

        this.DiscordMirror = new DiscordMessageMirror();
        this.DiscordMirror.bindAndBroadcast();

        registerCommands();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        if (this.MinecraftMirror != null)
            this.MinecraftMirror.close();
        if (this.DiscordMirror != null)
            this.DiscordMirror.close();

        this.DiscordMirror = null;
        this.MinecraftMirror = null;
    }

    private void registerCommands() {
        var executor = new CommandListener();
        var completer = new PluginTabCompleter();
        this.getCommand("mirror").setExecutor(executor);
        this.getCommand("mirror").setTabCompleter(completer);
    }
}
