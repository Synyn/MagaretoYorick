package com.magareto.yorick.db.redis.model.settings;

public class GuildSettings {
    private OsuTrackingSettings osuTrackingSettings;

    public OsuTrackingSettings getOsuTrackingSettings() {
        return osuTrackingSettings;
    }

    public void setOsuTrackingSettings(OsuTrackingSettings osuTrackingSettings) {
        this.osuTrackingSettings = osuTrackingSettings;
    }
}
