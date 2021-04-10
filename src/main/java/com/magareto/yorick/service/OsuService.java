package com.magareto.yorick.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.magareto.yorick.bot.exception.YorickException;
import discord4j.core.object.entity.Message;

public interface OsuService {
    void trackNewUser(Message message) throws JsonProcessingException, YorickException;
}
