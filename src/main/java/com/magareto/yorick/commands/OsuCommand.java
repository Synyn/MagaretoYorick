package com.magareto.yorick.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.magareto.yorick.bot.command.CommandModel;
import com.magareto.yorick.bot.command.YorickCommand;
import com.magareto.yorick.bot.command.annotations.Command;
import com.magareto.yorick.bot.command.utils.CommandUtils;
import com.magareto.yorick.bot.constants.ErrorMessages;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.db.redis.model.settings.GuildSettings;
import com.magareto.yorick.db.redis.model.settings.OsuTrackingSettings;
import com.magareto.yorick.service.SettingsService;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;

import java.util.Iterator;
import java.util.List;

@Command(name = "osu", description = "Configure osu settings for this server.")
public class OsuCommand implements YorickCommand {

    private final String trackingCommand = "track";
    private final String changeTrackingChannelCommand = "track-channel";

    private final SettingsService settingsService = Globals.injector.getInstance(SettingsService.class);

    @Override
    public void execute(Message message, CommandModel commandModel) throws YorickException, Exception {
        if (commandModel.getArgs() == null || commandModel.getArgs().isEmpty()) {
            throw new YorickException("You need to specify an argument");
        }

        String flag = commandModel.getArgs().get(0);

        Snowflake guildId = message.getGuildId().get();
        if (flag.equals(trackingCommand)) {

            //TODO: Check if author has admin priveleges.

            Member block = message.getAuthor().get().asMember(guildId).block();
            PermissionSet permissions = block.getBasePermissions().block();
            Iterator<Permission> iterator = permissions.stream().iterator();

            while (iterator.hasNext()) {
                Permission permission = iterator.next();
                if(permission != Permission.ADMINISTRATOR) {
                    continue;
                }
            }

            boolean tracking = toggleTracking(guildId);
            CommandUtils.sendMessage(message.getChannel(), "Tracking has been toggled, currently server is being `" +
                    (tracking ? "tracked" : "untracked") + "`.");
        } else if (flag.equals(changeTrackingChannelCommand)) {
            changeDefaultTrackingChannel(guildId, message.getChannelId());
            CommandUtils.sendMessage(message.getChannel(), "The channel has been updated. This is the new place where the osu scores will be for now on...");
        }
    }

    private void changeDefaultTrackingChannel(Snowflake guildId, Snowflake channelId) throws JsonProcessingException {
        GuildSettings settings = settingsService.get(guildId);
        settings.getOsuTrackingSettings().setDefaultTrackingChannel(channelId.asString());
        settingsService.persist(guildId, settings);
    }

    private boolean toggleTracking(Snowflake guildId) throws JsonProcessingException {
        GuildSettings settings = settingsService.get(guildId);
        if (settings == null) {
            settings = settingsService.create(guildId);
        }

        OsuTrackingSettings trackingSettings = settings.getOsuTrackingSettings();

        boolean tracking = trackingSettings.isTracking();
        trackingSettings.setTracking(!tracking);

        settingsService.persist(guildId, settings);

        return !tracking;
    }

    @Override
    public List<String> getArguments() {
        return null;
    }
}
