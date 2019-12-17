package dev.trinitrotoluene.mcmirror;

import club.minnced.discord.webhook.WebhookClient;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

class MirrorPlugin extends JavaPlugin {
    private DiscordMessageMirror DiscordMirror;
    private MinecraftMessageMirror MinecraftMirror;

    DiscordMessageMirror getDiscordMessageMirror() {
        return this.DiscordMirror;
    }

    void setDiscordMessageMirror(DiscordMessageMirror value) {
        this.DiscordMirror = value;
    }

    MinecraftMessageMirror getMinecraftMessageMirror() {
        return this.MinecraftMirror;
    }

    public void setMinecraftMessageMirror(MinecraftMessageMirror value) {
        this.MinecraftMirror = value;
    }

    List<String> getWhitelistedRoles() {
        return getConfig().getStringList("roles");
    }

    List<String> getWhitelistedChannels() {
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
        // TODO: Autocompletion, break these out into subcommands.
        var executor = new CommandListener();
        this.getCommand("mirrorenable").setExecutor(executor);
        this.getCommand("mirrordisable").setExecutor(executor);
        this.getCommand("mirrorkill").setExecutor(executor);
        this.getCommand("mirrorconnect").setExecutor(executor);
        this.getCommand("mirrorstatus").setExecutor(executor);

    }
}
