package com.magareto.yorick.bot.event;

import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.User;
import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;

import java.util.function.Consumer;
import java.util.logging.Logger;

public class OnReadyEvent implements Consumer<ReadyEvent> {

    private static final Logger logger = Logger.getLogger(OnReadyEvent.class.getName());

    @Override
    public void accept(ReadyEvent readyEvent) {
        User self = readyEvent.getSelf();

        logger.info("The bot has started successfully..");
        logger.info("Logged in as -> " + self.getUsername());
    }
}
