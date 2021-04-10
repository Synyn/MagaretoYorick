package com.magareto.yorick.service;

import discord4j.core.object.entity.Message;

public interface OsuService {
    boolean trackNewUser(Message message);
}
