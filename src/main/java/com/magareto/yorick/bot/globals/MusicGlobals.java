package com.magareto.yorick.bot.globals;

import com.magareto.yorick.bot.music.MusicPlayer;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MusicGlobals {
    public static ConcurrentMap<String, MusicPlayer> botQueue = new ConcurrentHashMap<>();
}
