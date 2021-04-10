package com.magareto.yorick.osu;

import com.magareto.yorick.bot.constants.ErrorMessages;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.osu.bancho.BanchoStrategy;
import com.magareto.yorick.osu.gatari.GatariStrategy;

public class OsuFactory {

    public static Osu createOsu(OsuServer server) throws YorickException {
        Osu osu;

        switch (server) {
            case BANCHO -> osu = new BanchoStrategy();
            case GATARI -> osu = new GatariStrategy();
            default -> throw new YorickException(ErrorMessages.INVALID_OSU_SERVER);
        }

        return osu;
    }

}
