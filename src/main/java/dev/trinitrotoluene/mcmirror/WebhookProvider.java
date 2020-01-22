package dev.trinitrotoluene.mcmirror;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessage;
import dev.trinitrotoluene.mcmirror.util.SteppedTicker;
import dev.trinitrotoluene.mcmirror.util.Ticker;
import org.bukkit.block.structure.Mirror;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class WebhookProvider {
    private final List<WebhookClient> _clients;
    private final SteppedTicker _ticker;
    private final Logger _logger;
    private final int _webhookRatelimit = 5;
    public WebhookProvider(FileConfiguration config, Logger logger) {
        this._clients = new ArrayList<>();
        this._logger = logger;

        var webhookUrls = config.getStringList("webhook.urls");
        try {
            for (var url : webhookUrls)
                this._clients.add(WebhookClient.withUrl(url));
        }
        catch (Exception e) {
            _logger.warning("Invalid webhook URL specified in configuration!");
        }

        this._ticker = new SteppedTicker(this._clients.size(), _webhookRatelimit);
    }

    public void execute(WebhookMessage message) {
        try {
            this._clients.get(this._ticker.getTick()).send(message);
            this._ticker.steppedTick();
        }
        catch (Exception e) {
            this._logger.warning(String.format("An exception was thrown while executing a webhook:\r\n%s", e.toString()));
        }
    }

    public void close() {
        for (var client : this._clients) {
            client.close();
        }
    }
}
