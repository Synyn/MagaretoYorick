package com.magareto.yorick.bot.injector;

import com.google.inject.AbstractModule;
import com.magareto.yorick.service.*;
import com.magareto.yorick.service.impl.*;

public class YorickInjectorConfig extends AbstractModule {
    @Override
    protected void configure() {
        bind(AnimeService.class).to(AnimeServiceImpl.class);
        bind(WaifuService.class).to(WaifuServiceImpl.class);
        bind(MemeService.class).to(MemeServiceImpl.class);
        bind(HelpService.class).to(HelpServiceImpl.class);
        bind(CommandService.class).to(CommandServiceImpl.class);
    }
}
