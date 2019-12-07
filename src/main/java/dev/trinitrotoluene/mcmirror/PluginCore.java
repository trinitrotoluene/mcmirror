package dev.trinitrotoluene.mcmirror;

import club.minnced.discord.webhook.WebhookClient;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginCore extends JavaPlugin {
    private WebhookClient Webhook;
    private DiscordMessageBinding MessageBinding;
    private MessageListener MessageListener;

    public WebhookClient getWebhook() {
        return this.Webhook;
    }

    public DiscordMessageBinding getDiscordBinding() {
        return this.MessageBinding;
    }

    public MessageListener getListener() {
        return this.MessageListener;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        var url = getConfig().getString("webhook");
        this.Webhook = WebhookClient.withUrl(url);
        getLogger().info("Loaded webhook");

        this.MessageListener = new MessageListener(this);
        getServer().getPluginManager().registerEvents(this.MessageListener, this);

        this.MessageBinding = new DiscordMessageBinding(this);
        this.MessageBinding.bindAndBroadcast();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        this.Webhook.close();
        getLogger().info("Unloaded webhook");
        this.MessageBinding.close();

        this.MessageBinding = null;
        this.Webhook = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mcenable")) {
            this.getDiscordBinding().setEnabled(true);
            this.getListener().setEnabled(true);
            return true;
        }
        else if (command.getName().equalsIgnoreCase("mcdisable")) {
            this.getDiscordBinding().setEnabled(false);
            this.getListener().setEnabled(false);
            return true;
        }

        return false;
    }
}
