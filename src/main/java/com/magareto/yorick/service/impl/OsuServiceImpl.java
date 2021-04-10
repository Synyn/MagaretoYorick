package com.magareto.yorick.service.impl;

import com.magareto.yorick.service.OsuService;
import discord4j.core.object.entity.Message;

public class OsuServiceImpl implements OsuService {
    @Override
    public boolean trackNewUser(Message message) {

        return false;
    }
}
