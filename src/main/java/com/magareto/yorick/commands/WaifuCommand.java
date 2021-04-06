package com.magareto.yorick.commands;

import com.magareto.yorick.bot.command.CommandModel;
import com.magareto.yorick.bot.command.YorickCommand;
import com.magareto.yorick.bot.command.annotations.Command;
import com.magareto.yorick.bot.command.utils.CommandUtils;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.service.WaifuService;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import org.apache.log4j.Logger;
import reactor.core.publisher.Mono;

import java.io.InputStream;

@Command(name = "waifu")
public class WaifuCommand implements YorickCommand {

    private WaifuService waifuService = Globals.injector.getInstance(WaifuService.class);

    private Logger logger = Logger.getLogger(WaifuCommand.class);

    @Override
    public void execute(Message message, CommandModel commandModel) throws YorickException, Exception {

        Mono<MessageChannel> channel = message.getChannel();
        String picture = null;


        if (commandModel.getArgs() == null) {
            picture = waifuService.getSfw(null);
        }else {
            logger.info("Flag -> " + commandModel.getArgs().get(0));
            picture = waifuService.getSfw(commandModel.getArgs().get(0));
        }


        String finalPicture = picture;
        channel.subscribe(c -> c.createEmbed(e -> e.setImage(finalPicture)).block());

    }
}
