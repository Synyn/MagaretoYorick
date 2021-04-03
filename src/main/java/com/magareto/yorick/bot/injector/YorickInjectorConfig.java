package com.magareto.yorick.bot.injector;

import com.google.inject.AbstractModule;
import com.magareto.yorick.service.AnimeService;
import com.magareto.yorick.service.impl.AnimeServiceImpl;

public class YorickInjectorConfig extends AbstractModule {
    @Override
    protected void configure(){
        bind(AnimeService.class).to(AnimeServiceImpl.class);
    }
}
