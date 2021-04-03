package com.magareto.yorick.bot;

import com.google.inject.Injector;
import com.magareto.yorick.bot.event.OnMessageEvent;
import com.magareto.yorick.bot.event.OnReadyEvent;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;

public class EventDispatcher {

    /**
     * This is the entry point that handles the events.
     * @param client
     */
    public static void dispatchEvents(GatewayDiscordClient client) {
        client.getEventDispatcher().on(ReadyEvent.class).subscribe(new OnReadyEvent());
        client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(new OnMessageEvent());
    }
}
