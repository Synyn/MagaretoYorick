package com.magareto.yorick.bot;

import com.google.inject.Guice;
import com.magareto.yorick.bot.command.YorickCommandInitializer;
import com.magareto.yorick.bot.constants.Constants;
import com.magareto.yorick.bot.constants.ErrorMessages;
import com.magareto.yorick.bot.constants.RedisConstants;
import com.magareto.yorick.bot.exception.YorickException;
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
import org.codehaus.plexus.util.StringUtils;

import java.util.Optional;

public class MagaretoYorick {

    private static final Logger logger = Logger.getLogger(MagaretoYorick.class);


    public static void runBot() {

        Globals.injector = Guice.createInjector(new YorickInjectorConfig());

        try {
            Globals.commands = YorickCommandInitializer.initializeCommands();
            Globals.commands.forEach((k, v) -> logger.info("Command Name -> " + k));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Globals.redisConnection = RedisInitalizer.createConnection(RedisConstants.HOSTNAME);
        Globals.jedisPool = RedisInitalizer.createJedisPool(RedisConstants.HOSTNAME);

        final String BOT_TOKEN = System.getenv(Constants.BOT_TOKEN_ENV);
        final String BANCHO_CLIENT_SECRET = System.getenv(Constants.BANCHO_CLIENT_SECRET);

        logger.info("BOT_TOKEN -> " + BOT_TOKEN);
        logger.info("BANCHO_CLIENT_SECRET -> " + BANCHO_CLIENT_SECRET);

        if(BOT_TOKEN == null) {
            throw new RuntimeException(ErrorMessages.BOT_COULD_NOT_START);
        }

        if(StringUtils.isEmpty(BANCHO_CLIENT_SECRET)) {
            Globals.bancho = false;
        }else {
            Globals.bancho = true;
        }

        GatewayDiscordClient client = DiscordClientBuilder.create(BOT_TOKEN)
                .build()
                .login()
                .block();

        if(client == null) {
            throw new RuntimeException(ErrorMessages.BOT_COULD_NOT_START);
        }


        StatusUpdate statusUpdate = ImmutableStatusUpdate.builder().afk(true).status("y!help").game(Activity.listening("to y!help")).build();
        client.updatePresence(statusUpdate).subscribe();

        EventDispatcher.dispatchEvents(client);


        CronInitializer.registerCronJobs();
        RedisInitalizer.registerSubscribers(client);


        client.onDisconnect().block();
    }

}
