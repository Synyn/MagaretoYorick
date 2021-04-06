package com.magareto.yorick.service;

import discord4j.core.object.entity.channel.MessageChannel;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

public interface WaifuService {
    String getNsfw(String tag);
    String getSfw(String tag) throws IOException;
    void getSfwAsync(Mono<MessageChannel> channel, List<String> tag);
}
