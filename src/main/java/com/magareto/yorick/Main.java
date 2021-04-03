package com.magareto.yorick;

import com.magareto.yorick.bot.MagaretoYorick;
import org.apache.log4j.Logger;


public class Main {

    private static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        MagaretoYorick magaretoYorick = new MagaretoYorick();
        magaretoYorick.runBot();
    }


}
