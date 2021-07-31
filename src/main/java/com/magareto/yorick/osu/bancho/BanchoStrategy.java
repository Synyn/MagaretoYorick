package com.magareto.yorick.osu.bancho;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magareto.yorick.bot.constants.RedisConstants;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.osu.BaseScoreModel;
import com.magareto.yorick.osu.Osu;
import com.magareto.yorick.osu.OsuServer;
import com.magareto.yorick.osu.bancho.model.BanchoScore;
import com.magareto.yorick.osu.model.ScoreModel;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BanchoStrategy implements Osu {

    private static final String BANCHO_CLIENT_ID = "BANCHO_CLIENT_ID";
    private static final String BANCHO_CLIENT_SECRET = "BANCHO_CLIENT_SECRET";

    private static final String TOKEN_GENERATION_URL = "https://osu.ppy.sh/oauth/token";
    private static final int STATUS_UNAUTHORIZED = 401;
    private String URL = "https://osu.ppy.sh/api/v2/users/%s/scores/recent?include_fails=0&limit=5";

    private ObjectMapper mapper = new ObjectMapper();

    DecimalFormat df = new DecimalFormat("##.00");

    Logger logger = Logger.getLogger(BanchoStrategy.class);

    @Override
    public List<ScoreModel> getRecentScoresForUser(String userId) throws IOException {

        // If bancho is not enabled, we do not need to handle the logic.
        if(!Globals.bancho) {
            return null;
        }

        String token = Globals.redisConnection.get(RedisConstants.BANCHO_ACCESS_TOKEN_KEY);
        if (token == null) {
            token = generateNewToken();
            Globals.redisConnection.set(RedisConstants.BANCHO_ACCESS_TOKEN_KEY, token);
        }

        try {
            CloseableHttpResponse response = sendRequest(userId, token);
            if (response.getStatusLine().getStatusCode() == STATUS_UNAUTHORIZED) {
                token = generateNewToken();

                Globals.redisConnection.set(RedisConstants.BANCHO_ACCESS_TOKEN_KEY, token);

                response = sendRequest(userId, token);
            }

            logger.info(response.getEntity().getContent().toString());

            String json = new String(response.getEntity().getContent().readAllBytes());
            BanchoScore[] scores = mapper.readValue(json, BanchoScore[].class);

            List<ScoreModel> scoreModels = new ArrayList<>();
            for (BanchoScore score : scores) {
                scoreModels.add(createScoreModelFromBanchoScore(score));
            }

            response.close();

            return scoreModels;

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    private CloseableHttpResponse sendRequest(String userId, String token) throws IOException {
        HttpGet get = new HttpGet(String.format(URL, userId));
        get.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        CloseableHttpClient client = HttpClients.createDefault();

        return client.execute(get);

    }

    private String generateNewToken() throws IOException {
        HttpPost httpPost = new HttpPost(TOKEN_GENERATION_URL);

        Map<String, String> params = new HashMap<>();
        params.put("client_id", System.getenv(BANCHO_CLIENT_ID));
        params.put("client_secret", System.getenv(BANCHO_CLIENT_SECRET));
        params.put("grant_type", "client_credentials");
        params.put("scope", "public");

        StringEntity entity = new StringEntity(mapper.writeValueAsString(params));
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpClient client = HttpClients.createDefault();

        CloseableHttpResponse response = client.execute(httpPost);

        InputStream content = response.getEntity().getContent();
        String json = new String(content.readAllBytes());

        JsonNode jsonNode = mapper.readTree(json);
        client.close();

        return jsonNode.get("access_token").asText();

    }

    private ScoreModel createScoreModelFromBanchoScore(BanchoScore score) {
        ScoreModel model = new ScoreModel();

        model.setAccuracy(df.format(score.getAccuracy() * 100.0));
        model.setCreatedAt(score.getCreatedAt());
        model.setGameMode(score.getMode());
        model.setBeatMapUrl(score.getBeatmap().getUrl());
        model.setBpm(String.valueOf(score.getBeatmap().getBpm()));
        model.setMods(score.getMods());
        model.setScore(score.getScore());
        model.setId(String.valueOf(score.getId()));
        model.setPp(String.valueOf(score.getPp()));
        model.setUserId(String.valueOf(score.getUserId()));
        model.setUsername(score.getUser().getUsername());
        model.setRank(score.getRank());
        model.setMaxCombo(score.getMaxCombo());
        model.setDifficulty(score.getBeatmap().getVersion());
        model.setBeatMapName(score.getBeatmapSet().getTitle());
        model.setUserAvatarUrl(score.getUser().getAvatar());
        model.setStarRating(String.valueOf(score.getBeatmap().getDiff()));
        model.setMissCount(String.valueOf(score.getStatistics().get("count_miss")));
        model.setServer(OsuServer.BANCHO);
        model.setBeatMapArtist(score.getBeatmapSet().getArtist());

        return model;
    }

    @Override
    public String getUserIdFromLink(String link) {
        return null;
    }
}
