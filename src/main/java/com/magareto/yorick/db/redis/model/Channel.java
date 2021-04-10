package com.magareto.yorick.db.redis.model;

import java.util.HashMap;
import java.util.Map;

public enum Channel {
    OSU("osu_channel");

    String channel;

    Channel(String channel) {
        this.channel = channel;
    }


}
