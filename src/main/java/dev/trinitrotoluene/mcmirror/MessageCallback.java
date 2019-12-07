package dev.trinitrotoluene.mcmirror;

import discord4j.core.event.domain.message.MessageCreateEvent;

public interface MessageCallback {
    void onMessage(MessageCreateEvent message);
}
