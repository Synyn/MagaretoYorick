package com.magareto.yorick.bot.globals;

import com.google.inject.Injector;
import com.magareto.yorick.bot.command.InternalCommand;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

public class Globals {

    /**
     * All the globals are created before the bot is even started, so there should not be any need to be careful about null pointers.
     *
     * If there are null pointers that means that something is terribly wrong and it should`ve stopped execution at the starter/initializer classes.
     *
     */


    /**
     * This is a global command map.
     * This should NOT BE used anywhere, this is for yorick`s low level work
     * If you want to add a command, you need to annotate the class with @Command
     */
    public static Map<String, InternalCommand> commands;

    /**
     * This is used for the DI managing
     *
     * If you look under the bot.injector package - there is a config file.
     * You can add there the DI beans.
     *
     * If you are not familiar with the DI concept.
     * Basically all the classes you register there are kept inside a container and all are singleton by default.
     * Whenever you ask them from the injector - the injector will return a reference to that instance.
     *
     * This is useful, because all the instances are created on compile time and they live inside this container until
     * the app is stopped.
     *
     * Globals.injector.getInstance(Something.class);
     */
    public static Injector injector;

    /**
     * This is used for global access to the redis db.
     * If you want to use it in the commands/services - go ahead.
     *
     * Globals.redisConnection
     */
    public static Jedis redisConnection;

    /**
     * Jedis Pool, this is used for pooling. This is used for redis search queries
     */
    public static JedisPool jedisPool;

    /**
     * This is a flag which is set as true if the bancho credentials are provided to the bot.
     *
     * This is used to disable a performance heavy functionality of the bot.
     *
     */
    public static boolean bancho;

}
