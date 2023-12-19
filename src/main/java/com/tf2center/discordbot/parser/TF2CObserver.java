package com.tf2center.discordbot.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tf2center.discordbot.domain.TF2CWebSite;
import com.tf2center.discordbot.dto.TF2CPlayerCount;
import com.tf2center.discordbot.dto.json.TF2CSubstituteSlot;
import com.tf2center.discordbot.dto.json.TF2CSubstituteSlotContainer;
import com.tf2center.discordbot.dto.json.tf2cpreview.TF2CLobbyPreview;
import com.tf2center.discordbot.dto.teams.TF2CPlayerSlot;
import com.tf2center.discordbot.utils.TF2CCollectionsUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class parses pure HTML from the website and transforms JSON into a POJO
 */
@EnableScheduling
@Component("htmlParser")
public final class TF2CObserver {

    private static final String TF2C_URL = "https://tf2center.com/lobbies";
    private static Document tf2cWebSite;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedRate = 10_000, initialDelay = 500)
    private static void parseTF2C() {
        try {
            tf2cWebSite = Jsoup.connect(TF2C_URL)
                    .userAgent("Mozilla")
                    .timeout(5000)
                    .get();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TF2CWebSite.update(getPlayerCount(), getLobbyPreview(), getSubstituteSlots());
    }

    private static Set<TF2CLobbyPreview> getLobbyPreview() {
        //We cut away unnecessary characters and leave pure JSON
        String parsedJson = new TF2CCollectionsUtils().getLastFromList(tf2cWebSite.getElementsByTag("script"))
                .toString().substring(49);
        parsedJson = parsedJson.substring(0, parsedJson.length() - 11);


        Set<TF2CLobbyPreview> lobbies = null;

        try {
            lobbies = objectMapper.readValue(parsedJson, new TypeReference<Set<TF2CLobbyPreview>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (lobbies.isEmpty()) {
            return Collections.emptySet();
        }
        //These iterations extract info from within the lobby
        lobbies.forEach(preview -> {
            //TODO Redo with reactive streams
            Document tf2cWebSiteLocal;
            ArrayList<TF2CPlayerSlot> players;
                    try {
                        tf2cWebSiteLocal = Jsoup.connect(TF2C_URL + "/" + preview.getLobbyId())
                                .userAgent("Mozilla")
                                .timeout(5000)
                                .get();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
            players = extractPlayers(tf2cWebSiteLocal.getElementsByClass("lobbySlot"));
            preview.setPlayerSlotList(players);


            List<?> headers = extractLobbyHeaders(tf2cWebSiteLocal.getElementsByClass("lobbyHeaderOptions"));
            preview.setOffclassingAllowed((boolean) headers.get(0));
            preview.setConfig((String) headers.get(1));
            preview.setServer((String) headers.get(2));

            //TODO map to new separate DTO
                }
        );
        return lobbies;
    }


    private static Set<TF2CSubstituteSlot> getSubstituteSlots() {
        String parsedJson = tf2cWebSite.select("script").get(29)
                .toString().substring(164);
        parsedJson = parsedJson.substring(0, parsedJson.indexOf(";;") - 1);

        TF2CSubstituteSlotContainer substituteSpots = null;

        try {
            substituteSpots = objectMapper.readValue(parsedJson, TF2CSubstituteSlotContainer.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (!substituteSpots.getSubstitutionSlot().isEmpty()) {
            return substituteSpots.getSubstitutionSlot();
        }

        return Collections.emptySet();
    }

    private static TF2CPlayerCount getPlayerCount() {
        return TF2CPlayerCount.of(
                new AtomicInteger(getPlayerCountTotal()),
                new AtomicInteger(getPlayerCountEU()),
                new AtomicInteger(getPlayerCountNA()),
                new AtomicInteger(getPlayerCountOther())
        );
    }

    private static Integer getPlayerCountOther() {
        //AU and "Other" players are combined cos there is barely anyone in AU playing nowdays.
        int other = Integer.parseInt(tf2cWebSite.getElementById("otherPlayersOnline").text());
        int au = Integer.parseInt(tf2cWebSite.getElementById("auPlayersOnline").text());
        return other + au;
    }

    private static Integer getPlayerCountEU() {
        return Integer.parseInt(tf2cWebSite.getElementById("euPlayersOnline").text());
    }

    private static Integer getPlayerCountNA() {
        return Integer.parseInt(tf2cWebSite.getElementById("naPlayersOnline").text());
    }

    private static Integer getPlayerCountTotal() {
        return Integer.parseInt(tf2cWebSite.getElementById("playersOnline").text());
    }

    private static ArrayList<TF2CPlayerSlot> extractPlayers(Elements playerSlots) {
        ArrayList<TF2CPlayerSlot> result = new ArrayList<>();
        playerSlots.forEach(element -> {
            if (element.attributes().toString().contains("filled")) {
                String playerName = element.children().get(1).children().get(0).text();
                String steamIdProfile = element.children().get(1).children().get(0).attributes().get("href");
                result.add(new TF2CPlayerSlot(playerName, steamIdProfile, "filled"));
            } else {
                result.add(new TF2CPlayerSlot("empty", "empty", "empty"));
            }
        });
        return result;
    }

    private static List<?> extractLobbyHeaders(Elements headers) {
        boolean offclassingAllowed = !headers.get(1).select("span").get(4).attributes().toString().contains("cross");
        String config = headers.get(0).select("td").get(3).text();
        String server;
        if (headers.get(0).select("tr").get(2).text().isBlank() || headers.get(0).select("tr").get(2).text().equals("Server")) {
            server = "";
        } else {
            server = headers.get(0).select("tr").get(2).text().substring(7);
        }
        return List.of(offclassingAllowed, config, server);

//        return List.of(
//                headers.get(1).select("span").get(4).attributes().toString().contains("cross"), //Offclassing allowed
//                headers.get(0).select("td").get(3).text(), //Config
//                headers.get(0).select("tr").get(2).text().isBlank() ? "" : headers.get(0).select("tr").get(2).text().substring(7) //Server
//        );
    }

}
