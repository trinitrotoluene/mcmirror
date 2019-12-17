package dev.trinitrotoluene.mcmirror;

import discord4j.core.event.domain.message.MessageCreateEvent;

interface MessageCallback {
    void onMessage(MessageCreateEvent message);
}
