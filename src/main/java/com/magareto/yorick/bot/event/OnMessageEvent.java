package com.magareto.yorick.bot.event;

import com.magareto.yorick.bot.command.CommandDispatcher;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.User;
import org.apache.log4j.Logger;

import java.util.Optional;
import java.util.function.Consumer;

public class OnMessageEvent implements Consumer<MessageCreateEvent> {
    Logger logger = Logger.getLogger(OnMessageEvent.class);

    @Override
    public void accept(MessageCreateEvent e) {
        logger.info("New Message -> " + e.getMessage().getContent());
        Optional<User> author = e.getMessage().getAuthor();
        if (isUserBot(author)) {
            logger.info("User is bot");
            return;
        }

        CommandDispatcher.dispatch(e.getMessage());
    }

    private boolean isUserBot(Optional<User> user) {
        if (user.isPresent()) {
            User userF = user.get();
            return userF.isBot();
        }
        return false;
    }

}
