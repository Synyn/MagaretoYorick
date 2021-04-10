package com.magareto.yorick.osu.bancho;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magareto.yorick.osu.BaseScoreModel;
import com.magareto.yorick.osu.Osu;
import com.magareto.yorick.osu.OsuServer;
import com.magareto.yorick.osu.bancho.model.BanchoScore;
import com.magareto.yorick.osu.model.ScoreModel;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BanchoStrategy implements Osu {

    String header = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI2NDM0IiwianRpIjoiMjkwMjFjZjM2MzdhNGVlM2RlZWIwZDQ4MGNkZDg2OGQwM2RjZjMwOTRkNzE3NGUyNDJlZWMxZjE2YmM4ZWZlMzFhMjAxZTAyMWI0NjUxZGMiLCJpYXQiOjE2MTgwNzMxODksIm5iZiI6MTYxODA3MzE4OSwiZXhwIjoxNjE4MTU5NTg5LCJzdWIiOiIiLCJzY29wZXMiOlsicHVibGljIl19.VPficVB9L41MiV-mU7dBM39Uwoe8TorS4RljEwgqMMJWFAP2v-BoIyBH5UePm5N6Jv-yETqCuggCZEIqiIpDp_TD-w1lNKhvnNxUExT884FoHq_r2DQ_infNPVg1jYjkmKjiwqRVWMl0mxM_1OY01Ffm3U-oQE4n--Ln-LxGIwZfLASs5lR93vdzq8lUbFKmHm9LyaCkX-Yfav1tYgZ8_pPpe_kPb6YmIpTOcOoeREL9ARPM6RlKGOkt7d7E8HFpvo5GYDvgYLeKgtjH_kFLo6UDmX5BZAuuSXnEsv2eNiOUkYKMbS_5yU8HV_0_hkLlXPjd4MBavxE8L1wIwgGYMEyiG-e-_-luvPMWY3Ht5UlgNSk6_5Zm6JPKxRSwK8lI9wZNfZZCBigHDaITUpgtQAc_GTv6gDOSrViayMWkttOlaT735FliZuaRJxrRbVQt1kV4f2Djrm5OCiKiqBzjPltHCxbCz_kqxtcizy4lSABH--WQAh99aA3TuqbR8iyj-Y0LVx2An9JohSGU5S6HniI5su59gOBuTxTgo68CcccvY4162uCHf8OdL84-OuG07SbWUyKlkRJlf2vnfKqwLW96gBzmpZ30D6tofuswE0sY942e0f7DnmBp5MERN7Ntv-u0ZCFX8P4lC_RPg8nFq5RQxI0Psb1FHg9UpD3SlN4\n";

    private ObjectMapper mapper = new ObjectMapper();

    Logger logger = Logger.getLogger(BanchoStrategy.class);

    @Override
    public List<ScoreModel> getRecentScoresForUser(String userId) {
        HttpGet get = new HttpGet(String.format("https://osu.ppy.sh/api/v2/users/%s/scores/recent?include_fails=0&limit=5", userId));
        get.setHeader(HttpHeaders.AUTHORIZATION, header);

        CloseableHttpClient aDefault = HttpClients.createDefault();

        try {
            CloseableHttpResponse execute = aDefault.execute(get);

            int statusCode = execute.getStatusLine().getStatusCode();
            logger.info("Status -> " + statusCode);

            String response = new String(execute.getEntity().getContent().readAllBytes());
            logger.info("Bancho response -> " + response);

            BanchoScore[] banchoScores = mapper.readValue(response, BanchoScore[].class);

            ArrayList<ScoreModel> scoreModels = new ArrayList<>();

            for (BanchoScore score : banchoScores) {
                ScoreModel model = createScoreModelFromBanchoScore(score);
                scoreModels.add(model);
            }

            return scoreModels;


        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    private ScoreModel createScoreModelFromBanchoScore(BanchoScore score) {
        ScoreModel model = new ScoreModel();

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
        model.setStarRating(String.valueOf(score.getBeatmap().getDiff()));
        model.setServer(OsuServer.BANCHO);

        return model;
    }

    @Override
    public String getUserIdFromLink(String link) {
        return null;
    }
}
