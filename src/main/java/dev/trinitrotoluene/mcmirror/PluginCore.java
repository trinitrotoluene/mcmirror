package dev.trinitrotoluene.mcmirror;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookCluster;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PluginCore extends JavaPlugin {
    private DiscordMessageMirror MessageBinding;
    private MinecraftMessageMirror MessageListener;

    public DiscordMessageMirror getDiscordMessageMirror() {
        return this.MessageBinding;
    }

    public MinecraftMessageMirror getMinecraftMessageMirror() {
        return this.MessageListener;
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
        this.MessageListener = new MinecraftMessageMirror(hookList);

        getServer().getPluginManager().registerEvents(this.MessageListener, this);
        getLogger().info(String.format("Minecraft -> Discord online with %s hooks", webhookUrls.size()));

        this.MessageBinding = new DiscordMessageMirror(this);
        this.MessageBinding.bindAndBroadcast();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        if (this.MessageListener != null)
            this.MessageListener.close();
        if (this.MessageBinding != null)
            this.MessageBinding.close();

        this.MessageBinding = null;
        this.MessageListener = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mirrorenable")) {
            if (this.getDiscordMessageMirror() != null)
                this.getDiscordMessageMirror().setEnabled(true);

            if (this.getMinecraftMessageMirror() != null)
                this.getMinecraftMessageMirror().setEnabled(true);

            sender.sendMessage(ChatColor.GREEN + "Set mirroring to enabled!");
            return true;
        }
        else if (command.getName().equalsIgnoreCase("mirrordisable")) {
            if (this.getDiscordMessageMirror() != null)
                this.getDiscordMessageMirror().setEnabled(false);

            if (this.getMinecraftMessageMirror() != null)
                this.getMinecraftMessageMirror().setEnabled(false);

            sender.sendMessage(ChatColor.GREEN + "Set mirroring to disabled!");
            return true;
        }
        else if (command.getName().equalsIgnoreCase("mirrorkill")) {
            if (this.getDiscordMessageMirror() == null) {
                sender.sendMessage(ChatColor.RED + "Already disconnected.");
            }
            else {
                this.getDiscordMessageMirror().close();
                this.MessageBinding = null;
                sender.sendMessage(ChatColor.GREEN + "Disconnected.");
            }

            return true;
        }
        else if (command.getName().equalsIgnoreCase("mirrorconnect")) {
            if (this.getDiscordMessageMirror() != null) {
                sender.sendMessage(ChatColor.RED + "Already connected or reconnecting.");
            }
            else {
                this.MessageBinding = new DiscordMessageMirror(this);
                this.MessageBinding.bindAndBroadcast();
                sender.sendMessage(ChatColor.GREEN + "Reconnecting!");
            }
            return true;
        }
        else if (command.getName().equalsIgnoreCase("mirrorstatus")) {
            if (this.getDiscordMessageMirror() == null) {
                sender.sendMessage("The Discord mirror isn't initialised.");
            }
            else {
                sender.sendMessage(this.getDiscordMessageMirror().getStatus());
            }
            return true;
        }
        return false;
    }
}
