package com.tf2center.discordbot.observer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tf2center.discordbot.domain.TF2CWebSite;
import com.tf2center.discordbot.dto.TF2CLobbyDTO;
import com.tf2center.discordbot.dto.TF2CPlayerCountDTO;
import com.tf2center.discordbot.dto.TF2CPlayerSlotDTO;
import com.tf2center.discordbot.dto.json.TF2CSubstituteSlotContainer;
import com.tf2center.discordbot.dto.json.TF2CSubstituteSlotDTO;
import com.tf2center.discordbot.dto.json.tf2cpreview.TF2CLobbyPreviewDTO;
import com.tf2center.discordbot.utils.TF2CCollectionsUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * This class parses pure HTML from the website and transforms JSON into a POJO
 */
@EnableScheduling
@Component("htmlParserFile")
public final class TF2CObserverFromFile {
    private static final File preview = new File("/home/user/test-case/preview");
    private static final File inner = new File("/home/user/test-case/inner");
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static Document tf2cWebSite;

    @Scheduled(fixedRate = 10_000, initialDelay = 500)
    private static void parseFromFileTest() {
        try {
            tf2cWebSite = Jsoup.parse(preview, "UTF-8", "http://example.com/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TF2CWebSite.update(getPlayerCount(), getLobbies(), getSubstituteSlots());
    }

    private static Set<TF2CLobbyDTO> getLobbies() {
        //We cut away unnecessary characters and leave pure JSON
        String parsedJson = new TF2CCollectionsUtils().getLastFromList(tf2cWebSite.getElementsByTag("script"))
                .toString().substring(49);
        parsedJson = parsedJson.substring(0, parsedJson.length() - 11);


        Set<TF2CLobbyPreviewDTO> lobbies = null;

        try {
            lobbies = objectMapper.readValue(parsedJson, new TypeReference<Set<TF2CLobbyPreviewDTO>>() {
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
            ArrayList<TF2CPlayerSlotDTO> players;
                    try {
                        tf2cWebSiteLocal = Jsoup.parse(inner, "UTF-8", "http://example.com/");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    players = extractPlayers(tf2cWebSiteLocal.getElementsByClass("lobbySlot"));
                    preview.setPlayerSlotList(players);

                    List<?> headers = extractLobbyHeaders(tf2cWebSiteLocal.getElementsByClass("lobbyHeaderOptions"));
                    preview.setOffclassingAllowed((boolean) headers.get(0));
                    preview.setConfig((String) headers.get(1));
                    preview.setServer((String) headers.get(2));
            preview.setLeaderName((String) headers.get(3));

                }
        );
        return Set.copyOf(
                lobbies.stream().map(lobby -> new TF2CLobbyDTO(lobby)).collect(Collectors.toSet())
        );
    }

    private static ArrayList<TF2CPlayerSlotDTO> extractPlayers(Elements playerSlots) {
        ArrayList<TF2CPlayerSlotDTO> result = new ArrayList<>();
        playerSlots.forEach(element -> {
            if (element.attributes().toString().contains("filled")) {
                String playerName = element.children().get(1).children().get(0).text();
                String steamIdProfile = element.children().get(1).children().get(0).attributes().get("href");
                result.add(new TF2CPlayerSlotDTO(playerName, steamIdProfile, "filled"));
            } else {
                result.add(new TF2CPlayerSlotDTO("empty", "empty", "empty"));
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
        String leaderName = headers.get(0).select("td").get(7).text();
        return List.of(offclassingAllowed, config, server, leaderName);

//        return List.of(
//                headers.get(1).select("span").get(4).attributes().toString().contains("cross"), //Offclassing allowed
//                headers.get(0).select("td").get(3).text(), //Config
//                headers.get(0).select("tr").get(2).text().isBlank() ? "" : headers.get(0).select("tr").get(2).text().substring(7) //Server
//        );
    }

    private static Set<TF2CSubstituteSlotDTO> getSubstituteSlots() {
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

    private static TF2CPlayerCountDTO getPlayerCount() {
        return TF2CPlayerCountDTO.of(
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
