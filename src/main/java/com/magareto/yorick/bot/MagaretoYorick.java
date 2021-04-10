package com.magareto.yorick.bot;

import com.google.inject.Guice;
import com.magareto.yorick.bot.command.YorickCommandInitializer;
import com.magareto.yorick.bot.constants.Constants;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.bot.injector.YorickInjectorConfig;
import com.magareto.yorick.cron.CronInitializer;
import com.magareto.yorick.db.redis.RedisInitalizer;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.User;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Status;
import discord4j.discordjson.json.ActivityUpdateRequest;
import discord4j.discordjson.json.gateway.ImmutableStatusUpdate;
import discord4j.discordjson.json.gateway.StatusUpdate;
import org.apache.log4j.Logger;

import java.util.Optional;

public class MagaretoYorick {
    private GatewayDiscordClient client;

    private final Logger logger = Logger.getLogger(MagaretoYorick.class);

    public void runBot() {

        Globals.injector = Guice.createInjector(new YorickInjectorConfig());

        try {
            Globals.commands = YorickCommandInitializer.initializeCommands();
            Globals.commands.forEach((k, v) -> logger.info("Command Name -> " + k));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Globals.redisConnection = RedisInitalizer.createConnection(Constants.REDIS_HOSTNAME);

        final String BOT_TOKEN = System.getenv(Constants.BOT_TOKEN_ENV);

        client = DiscordClientBuilder.create(BOT_TOKEN)
                .build()
                .login()
                .block();


        StatusUpdate statusUpdate = ImmutableStatusUpdate.builder().afk(true).status("y!help").game(Activity.listening("to y!help")).build();
        client.updatePresence(statusUpdate).subscribe();

        EventDispatcher.dispatchEvents(client);

//        CronInitializer.registerCronJobs(client, RedisInitalizer.createConnection(Constants.REDIS_HOSTNAME));
//        RedisInitalizer.registerSubscribers(client);


        client.onDisconnect().block();
    }

}
