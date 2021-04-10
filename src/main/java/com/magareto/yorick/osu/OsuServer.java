package com.magareto.yorick.osu;

import org.codehaus.plexus.util.Os;

public enum OsuServer {
    BANCHO("osu.ppy.sh"), GATARI("osu.gatari.pw");

    private String server;

    OsuServer(String server) {
        this.server = server;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }


    public static OsuServer getServerByName(String serverName) {
        for (OsuServer os : OsuServer.values()) {
            if (os.getServer().equals(serverName)) {
                return os;
            }
        }

        return null;
    }

}
