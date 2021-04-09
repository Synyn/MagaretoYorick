package com.magareto.yorick.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magareto.yorick.bot.constants.ErrorMessages;
import com.magareto.yorick.bot.exception.YorickException;
import com.magareto.yorick.service.MemeService;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public class MemeServiceImpl implements MemeService {

    private static final String BASE_URL = "https://meme-api.herokuapp.com/gimme/";
    private static final String URL_KEY = "url";

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final int SUCCESS_CODE = 200;

    Logger logger = Logger.getLogger(MemeServiceImpl.class);

    @Override
    public String getMeme() throws IOException, YorickException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(BASE_URL);

        CloseableHttpResponse response = client.execute(get);

        if (response.getStatusLine().getStatusCode() != SUCCESS_CODE) {
            throw new YorickException(ErrorMessages.NOT_FOUND);
        }

        client.close();
        return getLink(response.getEntity().getContent());
    }

    private String getLink(InputStream content) throws IOException {
//        logger.info(mapper.writeValueAsString(content));

        JsonNode jsonNode = mapper.readTree(content);
        return jsonNode.get(URL_KEY).asText();

    }

    @Override
    public String getMeme(String subReddit) throws IOException, YorickException {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(BASE_URL + subReddit);
        CloseableHttpResponse response = client.execute(get);

        if (response.getStatusLine().getStatusCode() != SUCCESS_CODE) {
            throw new YorickException(ErrorMessages.NOT_FOUND);
        }


        return getLink(response.getEntity().getContent());

    }
}
