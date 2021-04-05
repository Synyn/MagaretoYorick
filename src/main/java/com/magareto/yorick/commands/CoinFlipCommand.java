package com.magareto.yorick.commands;

import com.magareto.yorick.bot.command.YorickCommand;
import com.magareto.yorick.bot.command.annotations.Command;
import com.magareto.yorick.bot.command.utils.CommandUtils;
import com.magareto.yorick.bot.constants.ErrorMessages;
import com.magareto.yorick.bot.constants.Messages;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.models.coinflip.CoinFlip;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import org.apache.log4j.Logger;
import reactor.core.publisher.Mono;

import java.util.Random;

@Command(name = "flip")
public class CoinFlipCommand implements YorickCommand {

    Logger logger = Logger.getLogger(CoinFlipCommand.class);

    @Override
    public void execute(Message message) throws YorickException, Exception {

        CoinFlip bet = CoinFlip.getCoinFlip(message.getContent().split(" ")[1]);
        if (bet == null) {
            throw new YorickException(ErrorMessages.INVALID_BET);
        }

        CoinFlip flip = CoinFlip.EVEN;

        Random random = new Random();
        flip = random.nextInt(100) < 50 ? CoinFlip.TAILS : CoinFlip.HEADS;

        sendFlip(message, bet, flip);
    }

    private void sendFlip(Message message, CoinFlip bet, CoinFlip flip) {
        String result = null;
        User author = message.getAuthor().get();
        String username = author.getTag();
        logger.info("Bet -> " + bet.name() + " Result -> " + flip.name());

        if (bet.name().equals(flip.name())) {
            result = "WON";
        } else {
            result = "LOST";
        }

        CommandUtils.sendMessage(message.getChannel(),
                String.format(Messages.COINFLIP_MESSAGE, username, result, bet.name(), flip.name()));
    }
}
