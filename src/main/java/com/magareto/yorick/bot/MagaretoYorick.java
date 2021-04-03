package com.magareto.yorick.bot;

import com.google.inject.Guice;
import com.magareto.yorick.bot.command.YorickCommandInitializer;
import com.magareto.yorick.bot.constants.Constants;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.bot.injector.YorickInjectorConfig;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import org.apache.log4j.Logger;

public class MagaretoYorick {
    private GatewayDiscordClient client;

    private final Logger logger = Logger.getLogger(MagaretoYorick.class);

    public void runBot() {

        Globals.injector = Guice.createInjector(new YorickInjectorConfig());

        try {
            Globals.commands = YorickCommandInitializer.initializeCommands();
            Globals.commands.forEach((k, v) -> logger.info("Command Name -> " + k));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        final String BOT_TOKEN = System.getenv(Constants.BOT_TOKEN_ENV);

        client = DiscordClientBuilder.create(BOT_TOKEN)
                .build()
                .login()
                .block();

        EventDispatcher.dispatchEvents(client);

        client.onDisconnect().block();
    }

}
