package com.magareto.yorick.service.impl;

import com.magareto.yorick.bot.constants.RedisConstants;
import com.magareto.yorick.bot.globals.Globals;
import com.magareto.yorick.models.manhwa.Manhwa;
import com.magareto.yorick.models.manhwa.ManhwaFilter;
import com.magareto.yorick.service.ManhwaService;
import io.redisearch.Query;
import io.redisearch.SearchResult;
import io.redisearch.client.Client;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ManhwaServiceImpl implements ManhwaService {
    private String testLink = "https://bato.to/browse?genres=mature,adult&langs=en&sort=views_m.za&page=";

    Logger logger = Logger.getLogger(ManhwaServiceImpl.class);

    @Override
    public List<Manhwa> recommendManhwas(ManhwaFilter filter) {
        List<Manhwa> manhwas = new ArrayList<>();

        List<io.redisearch.Document> foundManhwas = findManhwas(filter);
        if (foundManhwas.isEmpty()) {
            return manhwas;
        }

        List<io.redisearch.Document> listDocs = new ArrayList<>(foundManhwas);
        for (io.redisearch.Document manhwaDoc : listDocs) {
            Manhwa manhwa = createManhwaFromDocument(manhwaDoc);
            manhwas.add(manhwa);
        }


        return manhwas;
    }

    private Manhwa createManhwaFromDocument(io.redisearch.Document manhwaDoc) {
        String rank = (String) manhwaDoc.get("rank");
        String totalViews = (String) manhwaDoc.get("totalViews");
        String releaseStatus = (String) manhwaDoc.get("releaseStatus");
        String summary = (String) manhwaDoc.get("summary");
        String title = (String) manhwaDoc.get("title");
        String authors = (String) manhwaDoc.get("authors");
        String id = (String) manhwaDoc.get("id");
        String alias = (String) manhwaDoc.get("alias");
        String imageLink = (String) manhwaDoc.get("imageLink");
        String genres = (String) manhwaDoc.get("genres");
        String monthly = (String) manhwaDoc.get("monthlyViews");
        String seriesLink = (String) manhwaDoc.get("seriesLink");

        Manhwa manhwa = new Manhwa();
        manhwa.setAlias(alias);
        manhwa.setGenres(genres);
        manhwa.setAuthors(authors);
        manhwa.setTotalViews(totalViews);
        manhwa.setReleaseStatus(releaseStatus);
        manhwa.setSummary(summary);
        manhwa.setTitle(title);
        manhwa.setId(id);
        manhwa.setImageLink(imageLink);
        manhwa.setMonthlyViews(monthly);
        manhwa.setSeriesLink(seriesLink);
        manhwa.setRank(rank);

        return manhwa;
    }

    private List<io.redisearch.Document> findManhwas(ManhwaFilter filter) {
        logger.info("Inside recommends");
        Client client = new Client(RedisConstants.MANHWA_INDEX_NAME, Globals.jedisPool);

        List<String> genres = filter.getGenres();
        String title = filter.getTitle();

        StringBuilder fullText = new StringBuilder();
        if (genres != null && !genres.isEmpty()) {
            for (String genre : genres) {
                fullText.append(" ");
                fullText.append(genre);
            }
        }

        if (!StringUtils.isEmpty(title)) {
            fullText.append(" ");
            fullText.append(title);
        }


//        if (StringUtils.isEmpty(fullText.toString())) {
//            fullText.append("mature,ecchi");
//        }

        fullText.append(" -yaoi");

        int limit = 406;
        logger.info("istop -> " + filter.isTop());
        if (filter.isTop()) {
            limit = 100;
        }

        logger.info("fulltext -> " + fullText.toString());

        Query query = new Query(fullText.toString()).limit(0, limit);

        if (filter.isTop() || StringUtils.isEmpty(fullText.toString())) {
            query.setSortBy("rank", false);

        }

        SearchResult search = client.search(query);

        logger.info("Found results -> " + search.totalResults);

        return search.docs;
    }

    @Override
    public void test() {
        logger.info("Inside test");

        List<Manhwa> manhwas = new ArrayList<>();

        for (int i = 1; i <= 9; i++) {
            List<Manhwa> allManhwas = getManhwas(testLink + i);
            manhwas.addAll(allManhwas);
        }

        fillRedis(manhwas);
    }

    private void fillRedis(List<Manhwa> allManhwas) {
        String baseKey = "yorick:manhwa:";

        for (Manhwa manhwa : allManhwas) {
            String key = baseKey + manhwa.getId();
            Map<String, String> value = new HashMap<>();
            value.put("id", manhwa.getId());
            value.put("title", manhwa.getTitle());
            value.put("summary", manhwa.getSummary());
            value.put("alias", manhwa.getAlias());
            value.put("seriesLink", manhwa.getSeriesLink());
            value.put("imageLink", manhwa.getImageLink());
            logger.info("Image link ->  " + manhwa.getImageLink());
            value.put("rank", manhwa.getRank());
            value.put("monthlyViews", manhwa.getMonthlyViews());
            value.put("totalViews", manhwa.getTotalViews());
            value.put("authors", manhwa.getAuthors());
            value.put("genres", manhwa.getGenres());
            value.put("releaseStatus", manhwa.getReleaseStatus());

            Globals.redisConnection.hset(key, value);

        }

        logger.info("redis is full");
    }

    private List<Manhwa> getManhwas(String link) {

        List<Manhwa> manhwas = new ArrayList<>();

        try {
            Document document = Jsoup.connect(link).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com").get();

            Element series = document.select("#series-list").first();
            logger.info("series -> " + series.data());

            Elements items = series.select(".item");

            for (Element item : items) {
                Element itemCover = item.select(".item-cover").first();


                String seriesLink = itemCover.attr("abs:href");
                String itemName = item.select(".item-title").first().text();

                Document itemInfo = Jsoup.connect(seriesLink).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com").get();

                Element mainer = itemInfo.select("#mainer").first();
                String imageLink = mainer.select(".attr-cover").first().select("img").attr("abs:src");

                Element alias = mainer.select(".alias-set").first();
                Element summary = mainer.select("pre").first();
                Elements additionalInfo = mainer.select(".attr-item");

                String aliasStr = "";
                String summaryStr = "";

                if (alias != null) {
                    aliasStr = alias.text();
                }

                if (summary != null) {
                    summaryStr = summary.text();
                }

                Map<String, String> infoList = new HashMap<>();

                for (Element info : additionalInfo) {
                    Element key = info.select("b").first();
                    Element value = info.select("span").first();

                    String name = key.text().replace(":", "");
                    String desc = value.text().replace("\n", "");
                    infoList.put(name, desc);
                }

                List<String> splitLink = Arrays.asList(seriesLink.split("/"));

                String id = splitLink.get(4);

                String rank = infoList.get("Rank");

                Pattern pattern = Pattern.compile("[a-z ,./]");
                List<String> ranks = Arrays.stream(pattern.split(rank)).filter(s -> !s.isEmpty()).collect(Collectors.toList());

                Manhwa manhwa = new Manhwa();

                manhwa.setId(id);
                manhwa.setTitle(itemName);
                manhwa.setSummary(summaryStr);
                manhwa.setAlias(aliasStr);
                manhwa.setSeriesLink(seriesLink);
                manhwa.setImageLink(imageLink);
                manhwa.setRank(ranks.get(0));
                manhwa.setMonthlyViews(ranks.get(1));
                manhwa.setTotalViews(ranks.get(2));
                manhwa.setAuthors(infoList.get("Authors"));
                manhwa.setGenres(infoList.get("Genres"));
                manhwa.setReleaseStatus(infoList.get("Release status"));

                manhwas.add(manhwa);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return manhwas;
    }

}
