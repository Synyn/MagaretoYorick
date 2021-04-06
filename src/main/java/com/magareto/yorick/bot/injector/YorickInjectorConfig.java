package com.magareto.yorick.bot.injector;

import com.google.inject.AbstractModule;
import com.magareto.yorick.service.AnimeService;
import com.magareto.yorick.service.WaifuService;
import com.magareto.yorick.service.impl.AnimeServiceImpl;
import com.magareto.yorick.service.impl.WaifuServiceImpl;

public class YorickInjectorConfig extends AbstractModule {
    @Override
    protected void configure(){
        bind(AnimeService.class).to(AnimeServiceImpl.class);
        bind(WaifuService.class).to(WaifuServiceImpl.class);
    }
}
