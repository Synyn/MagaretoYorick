package com.magareto.yorick.db.redis.model.osu;

import com.magareto.yorick.db.redis.model.settings.GuildData;

import java.util.List;

public class DiscordData {
    private String userId;
    private List<GuildData> guildList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public List<GuildData> getGuildList() {
        return guildList;
    }

    public void setGuildList(List<GuildData> guildId) {
        this.guildList = guildId;
    }
}
