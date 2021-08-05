package com.magareto.yorick.commands;

import com.magareto.yorick.bot.command.CommandModel;
import com.magareto.yorick.bot.command.YorickCommand;
import com.magareto.yorick.bot.command.annotations.Command;
import com.magareto.yorick.bot.constants.ErrorMessages;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.models.manhwa.Manhwa;
import com.magareto.yorick.models.manhwa.ManhwaFilter;
import com.magareto.yorick.service.ManhwaService;
import discord4j.core.object.entity.Message;
import org.apache.commons.lang3.RandomUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Command(name = "manhwa", description = "Recommends manhwa and it can be used as a read list.")
public class ManhwaCommand extends YorickCommand {

    private ManhwaService manhwaService = Globals.injector.getInstance(ManhwaService.class);

    Logger logger = Logger.getLogger(ManhwaCommand.class);

    @Override
    public void execute(Message message, CommandModel commandModel) throws YorickException, Exception {
        Map<String, String> args = commandModel.getArgs();
        boolean rec = args.containsKey("rec");

        if (rec) {
            ManhwaFilter manhwaFilter = new ManhwaFilter();
            String genres = args.get("genres");
            String title = args.get("title");
            boolean top = args.containsKey("top");

            if (genres != null) {
                String trimmed = genres.trim();
                List<String> filterGenres = Arrays.asList(trimmed.split(","));
                manhwaFilter.setGenres(filterGenres);
            }

            if (title != null) {
                manhwaFilter.setTitle(title);
            }

            manhwaFilter.setTop(top);

            List<Manhwa> manhwas = manhwaService.recommendManhwas(manhwaFilter);

            if (manhwas == null || manhwas.isEmpty()) {
                throw new YorickException(ErrorMessages.NOT_FOUND);
            }

            // TODO: Make this actually not random
            int random = (int) (Math.random() * ((manhwas.size()) + 1));

            if (random > 0 && manhwas.size() == random) {
                random -= 1;
            } else if (random < 0) {
                random = 0;
            }

            Manhwa manhwa = manhwas.get(random);
            message.getChannel().subscribe(c ->
                    c.createEmbed(e ->
                            e.setTitle(manhwa.getTitle())
                                    .setImage(manhwa.getImageLink())
                                    .setUrl(manhwa.getSeriesLink())
                                    .setDescription(manhwa.getSummary())
                                    .addField("Rank", manhwa.getRank(), true)
                                    .addField("Views", manhwa.getTotalViews(), true)
                                    .addField("Monthly Views", manhwa.getMonthlyViews(), true))
                            .subscribe());

        }

//        manhwaService.test();

    }

    @Override
    public List<String> getArguments() {
        return null;
    }

    @Override
    public String getHelp() {
        return null;
    }
}
