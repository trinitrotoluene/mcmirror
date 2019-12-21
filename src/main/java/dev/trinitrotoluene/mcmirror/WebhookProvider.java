package dev.trinitrotoluene.mcmirror;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessage;
import dev.trinitrotoluene.mcmirror.util.Ticker;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public final class WebhookProvider {
    private final List<WebhookClient> _clients;
    private final Ticker _ticker;

    public WebhookProvider(FileConfiguration config) {
        this._clients = new ArrayList<>();

        var webhookUrls = config.getStringList("webhooks");

        for (var url : webhookUrls)
            this._clients.add(WebhookClient.withUrl(url));

        this._ticker = new Ticker(this._clients.size());
    }

    public void execute(WebhookMessage message) {
        this._clients.get(this._ticker.getTick()).send(message);
        this._ticker.tickNext();
    }

    public void close() {
        for (var client : this._clients) {
            client.close();
        }
    }
}
