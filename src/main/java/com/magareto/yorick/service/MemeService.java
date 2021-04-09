package com.magareto.yorick.service;

import com.magareto.yorick.bot.exception.YorickException;

import java.io.IOException;
import java.net.MalformedURLException;

public interface MemeService {
    String getMeme() throws IOException, YorickException;
    String getMeme(String subReddit);
}
