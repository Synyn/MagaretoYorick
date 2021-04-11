package com.magareto.yorick.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.magareto.yorick.db.redis.model.settings.GuildSettings;
import discord4j.common.util.Snowflake;

public interface SettingsService {
    /**
     * Check if osu tracking is enabled in this server.
     * @param guildId
     * @return
     */
    Boolean checkIfTracked(Snowflake guildId) throws JsonProcessingException;
    GuildSettings create(Snowflake guildId) throws JsonProcessingException;

    void persist(Snowflake guildId, GuildSettings guildSettings) throws JsonProcessingException;

    GuildSettings get(Snowflake guildId) throws JsonProcessingException;
}
