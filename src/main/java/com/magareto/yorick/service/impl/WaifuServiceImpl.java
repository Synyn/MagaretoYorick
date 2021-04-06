package com.magareto.yorick.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magareto.yorick.service.WaifuService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
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

    @Override
    public String getNsfw(String tag) {
        return null;
    }

    @Override
    public String getSfw(String tag) throws IOException {
        if (tag == null) {
            tag = DEFAULT_TAG;
        }

        String url = String.format(BASE_URL_FORMATTABLE, SFW_TYPE, tag);
        HttpGet get = new HttpGet(url);


        CloseableHttpClient aDefault = HttpClients.createDefault();
        CloseableHttpResponse response = aDefault.execute(get);


        JsonNode jsonNode = mapper.readTree(response.getEntity().getContent());

        return jsonNode.get(URL_KEY).asText();


    }
}
