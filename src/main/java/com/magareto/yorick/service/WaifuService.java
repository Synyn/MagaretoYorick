package com.magareto.yorick.service;

import com.magareto.yorick.bot.exception.YorickException;
import discord4j.core.object.entity.channel.MessageChannel;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

public interface WaifuService {
    String getContent(String tag, boolean nsfw) throws IOException, YorickException;
}
