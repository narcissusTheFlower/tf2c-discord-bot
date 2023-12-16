package com.tf2center.discordbot.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tf2center.discordbot.domain.TF2CWebSite;
import com.tf2center.discordbot.dto.TF2CPlayerCount;
import com.tf2center.discordbot.dto.json.TF2CSubstituteSlot;
import com.tf2center.discordbot.dto.json.TF2CSubstituteSlotContainer;
import com.tf2center.discordbot.dto.json.tf2cpreview.TF2CLobbyPreview;
import com.tf2center.discordbot.dto.json.tf2lobby.TF2CLobby;
import com.tf2center.discordbot.utils.CollectionsUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
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

        TF2CWebSite.update(
                getPlayerCount(),
                getLobbies(),
                getLobbyPreview(),
                getSubstituteSlots()
        );

        /*
        TF2CWebSite
            .fetchPlayerCount()
            .fetchLobbies()
            .fetchLobbyPreviews()
            .fetchSubstituteSlots()
            .update();
         */

    }

    private static Set<TF2CLobbyPreview> getLobbyPreview() {
        //We cut away unnecessary characters and leave pure JSON
        String parsedJson = new CollectionsUtils().getLastFromList(tf2cWebSite.getElementsByTag("script"))
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
        //System.out.println();
        if (!substituteSpots.getSubstitutionSlot().isEmpty()) {
            return substituteSpots.getSubstitutionSlot();
        }

        return Collections.emptySet();
    }

    private static Set<TF2CLobby> getLobbies() {
        //We cut away unnecessary characters and leave pure JSON
//        String parsedJson = Iterables.getLast(tf2cWebSite.getElementsByTag("script"))
//                .toString().substring(49);
//        parsedJson = parsedJson.substring(0, parsedJson.length() - 11);
//
//        Set<TF2CLobbyPreview> lobbies = null;
//
//        try {
//            lobbies = objectMapper.readValue(parsedJson, new TypeReference<Set<TF2CLobbyPreview>>() {
//            });
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//
//        if (lobbies.isEmpty()) {
//            return Collections.emptySet();
//        }
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

}
