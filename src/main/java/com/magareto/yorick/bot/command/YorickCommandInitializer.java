package com.magareto.yorick.bot.command;

import com.magareto.yorick.bot.command.annotations.Command;
import com.magareto.yorick.bot.constants.ErrorMessages;
import discord4j.core.object.entity.Message;
import org.apache.log4j.Logger;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class YorickCommandInitializer {

    private static Logger logger = Logger.getLogger(YorickCommandInitializer.class);

    public static Map<String, InternalCommand> initializeCommands() throws Exception {
        Map<String, InternalCommand> commands = new HashMap<>();

        Reflections reflections = new Reflections("com.magareto.yorick");

        Set<Class<?>> reflectedCommands = reflections.getTypesAnnotatedWith(Command.class);

        for (Class<?> reflectedCommand : reflectedCommands) {
            Constructor<?> constructor = reflectedCommand.getConstructor();


            Class<?>[] interfaces = reflectedCommand.getInterfaces();

            if (interfaces.length == 0) {
                throw new Exception(ErrorMessages.COMMAND_CLASS_NOT_VALID);
            }

            for (Class<?> anInterface : interfaces) {
                String iName = anInterface.getSimpleName();
                logger.info("Name -> " + iName);

                if (!iName.equals(YorickCommand.class.getSimpleName())) {
                    throw new Exception(ErrorMessages.COMMAND_CLASS_NOT_VALID);
                }
            }

            Command annotation = reflectedCommand.getAnnotation(Command.class);

            Object object = constructor.newInstance();
            YorickCommand command = (YorickCommand) object;

            String commandName = annotation.name();
            InternalCommand internalCommand = new InternalCommand(annotation.name(), annotation.description(), command);

            commands.put(commandName, internalCommand);
        }

        return commands;

    }
}
