package com.magareto.yorick.bot.injector;

import com.google.inject.AbstractModule;
import com.magareto.yorick.service.*;
import com.magareto.yorick.service.impl.*;
import java.util.Set;

public class YorickInjectorConfig extends AbstractModule {
    @Override
    protected void configure() {
        bind(CommandService.class).to(CommandServiceImpl.class);
        bind(HelpService.class).to(HelpServiceImpl.class);

        bind(AnimeService.class).to(AnimeServiceImpl.class);
        bind(WaifuService.class).to(WaifuServiceImpl.class);
        bind(MemeService.class).to(MemeServiceImpl.class);
        bind(OsuService.class).to(OsuServiceImpl.class);
        bind(SettingsService.class).to(SettingsServiceImpl.class);
        bind(ManhwaService.class).to(ManhwaServiceImpl.class);
    }
}
