package com.magareto.yorick.db.redis.model.settings;

public class OsuTrackingSettings {
    private boolean tracking;
    private String defaultTrackingChannel;

    public boolean isTracking() {
        return tracking;
    }

    public void setTracking(boolean tracking) {
        this.tracking = tracking;
    }

    public String getDefaultTrackingChannel() {
        return defaultTrackingChannel;
    }

    public void setDefaultTrackingChannel(String defaultTrackingChannel) {
        this.defaultTrackingChannel = defaultTrackingChannel;
    }
}
