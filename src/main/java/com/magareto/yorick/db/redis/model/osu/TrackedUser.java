package com.magareto.yorick.db.redis.model.osu;

import com.magareto.yorick.osu.OsuServer;

public class TrackedUser {
    private String userId;
    private OsuServer server;
    private DiscordData discordData;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public OsuServer getServer() {
        return server;
    }

    public void setServer(OsuServer server) {
        this.server = server;
    }

    public DiscordData getDiscordData() {
        return discordData;
    }

    public void setDiscordData(DiscordData discordData) {
        this.discordData = discordData;
    }
}
