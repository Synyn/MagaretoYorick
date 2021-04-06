package com.magareto.yorick.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magareto.yorick.bot.command.utils.CommandUtils;
import com.magareto.yorick.bot.constants.Constants;
import com.magareto.yorick.bot.constants.ErrorMessages;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.service.WaifuService;
import discord4j.core.object.entity.channel.MessageChannel;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WaifuServiceImpl implements WaifuService {

    private final Set<String> sfwGenres = new HashSet<>(Arrays.asList("waifu",
            "neko",
            "shinobu",
            "bully",
            "cry",
            "hug",
            "kiss",
            "lick",
            "pat",
            "smug",
            "highfive",
            "nom",
            "bite",
            "slap",
            "wink",
            "poke",
            "dance",
            "cringe",
            "blush",
            "random"));

    private final ObjectMapper mapper = new ObjectMapper();

    private final Set<String> nsfwGenre = new HashSet<>(Arrays.asList("waifu", "neko", "trap", "blowjob", "random"));

    private final String BASE_URL_FORMATTABLE = "http://waifu.pics/api/%s/%s";
    private static final String DEFAULT_TAG = "waifu";
    private final String NSFW_TYPE = "nsfw";
    private final String SFW_TYPE = "sfw";

    private static final String URL_KEY = "url";


    CloseableHttpClient client = HttpClients.createDefault();


    @Override
    public String getContent(String tag, boolean nsfw) throws IOException, YorickException {

        if (tag == null) {
            tag = DEFAULT_TAG;
        } else if ((!sfwGenres.contains(tag) && !nsfw) || (!nsfwGenre.contains(tag) && nsfw)) {
            throw new YorickException(ErrorMessages.INVALID_GENRE);
        }

        String type = nsfw ? NSFW_TYPE : SFW_TYPE;

        String url = String.format(BASE_URL_FORMATTABLE, type, tag);
        HttpGet get = new HttpGet(url);

        CloseableHttpResponse response = client.execute(get);

        if (response.getStatusLine().getStatusCode() == Constants.STATUS_NOT_FOUND) {
            throw new YorickException(ErrorMessages.WAIFU_NOT_FOUND);
        }

        JsonNode jsonNode = mapper.readTree(response.getEntity().getContent());

        return jsonNode.get(URL_KEY).asText();

    }
}
