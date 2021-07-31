package com.magareto.yorick.commands;

import com.magareto.yorick.bot.command.CommandModel;
import com.magareto.yorick.bot.command.YorickCommand;
import com.magareto.yorick.bot.command.annotations.Command;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.bot.globals.MusicGlobals;
import com.magareto.yorick.bot.music.MusicPlayer;
import discord4j.common.util.Snowflake;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.VoiceChannel;

import java.util.List;
import java.util.Optional;
import java.util.Queue;

@Command(name = "player")
public class PlayerCommand extends YorickCommand {

    @Override
    public void execute(Message message, CommandModel commandModel) throws YorickException, Exception {
        Optional<Snowflake> guild = message.getGuildId();
    }

    @Override
    public List<String> getArguments() {
        return null;
    }



}
