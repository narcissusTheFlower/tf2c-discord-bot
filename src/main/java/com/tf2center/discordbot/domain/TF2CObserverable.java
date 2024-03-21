package com.tf2center.discordbot.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tf2center.discordbot.dto.TF2CLobbyDTO;
import com.tf2center.discordbot.dto.TF2CPlayerCountDTO;
import com.tf2center.discordbot.dto.TF2CPlayerSlotDTO;
import com.tf2center.discordbot.dto.json.TF2CSubstituteSlotContainer;
import com.tf2center.discordbot.dto.json.TF2CSubstituteSlotDTO;
import com.tf2center.discordbot.dto.json.tf2clobby.TF2CLobbyPreviewDTO;
import com.tf2center.discordbot.exceptions.TF2CObserverException;
import com.tf2center.discordbot.utils.TF2CCollectionsUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * This class parses pure HTML from the website and transforms JSON from this HTML into a POJO.
 */
@EnableScheduling
@Scope("singleton")
@Component("htmlParser")
public final class TF2CObserverable {

    private static final Logger logger = LoggerFactory.getLogger(TF2CObserverable.class);
    private static final String TF2C_URL = "https://tf2center.com/lobbies";
    private static Document tf2cWebSite;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Scheduled(fixedRate = 10_000)// Order of scheduled events: 1
    private static void parseTF2C() throws IOException {
        //Default timeout is 30 seconds
        tf2cWebSite = Jsoup.connect(TF2C_URL).userAgent("Mozilla").get();
        TF2CWebSite.update(getPlayerCount(), getLobbiesFromTF2Center(), getSubstituteSlots());
    }

    private static Set<TF2CLobbyDTO> getLobbiesFromTF2Center() {
        //We cut away unnecessary characters and leave pure JSON
        String parsedJson = TF2CCollectionsUtils.getLastFromList(tf2cWebSite.getElementsByTag("script"))
                .toString().substring(49);
        parsedJson = parsedJson.substring(0, parsedJson.length() - 11);

        Set<TF2CLobbyPreviewDTO> lobbies = null;
        try {
            lobbies = OBJECT_MAPPER.readValue(parsedJson, new TypeReference<Set<TF2CLobbyPreviewDTO>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to map the parsed JSON to a POJO.", e);
        }

        //If there are no lobbies open we do not parse inner lobbies.
        if (lobbies.isEmpty()) {
            logger.debug("No lobbies, returning empty Set.");
            return Collections.emptySet();
        }

        lobbies.forEach(lobby -> {
            //TODO Redo with reactive streams
            Document tf2cInnerLobby;
            List<TF2CPlayerSlotDTO> players;
                    try {
                        tf2cInnerLobby = Jsoup.connect(TF2C_URL + "/" + lobby.getLobbyId()).userAgent("Mozilla").get();
                    } catch (IOException e) {
                        throw new TF2CObserverException("Failed to parse inner lobby. Possibly a connection timeout.", e);
                    }
            players = extractPlayers(tf2cInnerLobby.getElementsByClass("lobbySlot"));
            lobby.setPlayerSlotList(players);

            List<?> headers = extractLobbyHeaders(tf2cInnerLobby.getElementsByClass("lobbyHeaderOptions"));
            lobby.setOffclassingAllowed((boolean) headers.get(0));
            lobby.setConfig((String) headers.get(1));
            lobby.setServer((String) headers.get(2));
            lobby.setLeaderName((String) headers.get(3));
                }
        );
        return Set.copyOf(
                lobbies.stream().map(TF2CLobbyDTO::new).collect(Collectors.toSet())
        );
    }

    private static Set<TF2CSubstituteSlotDTO> getSubstituteSlots() {
        String parsedJson = tf2cWebSite.select("script").get(29)
                .toString().substring(164);
        parsedJson = parsedJson.substring(0, parsedJson.indexOf(";;") - 1);

        TF2CSubstituteSlotContainer substituteSpots = null;

        try {
            substituteSpots = OBJECT_MAPPER.readValue(parsedJson, TF2CSubstituteSlotContainer.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (!substituteSpots.getSubstitutionSlot().isEmpty()) {
            return substituteSpots.getSubstitutionSlot();
        }

        logger.debug("No substitute spots, returning empty Set.");
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

    private static List<TF2CPlayerSlotDTO> extractPlayers(Elements playerSlots) {
        List<TF2CPlayerSlotDTO> result = new ArrayList<>();
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
        //TODO fixme
        //java.lang.IndexOutOfBoundsException: Index 1 out of bounds for length 0
        boolean offclassingAllowed = !headers.get(1).select("span").get(4).attributes().toString().contains("cross");
        String config = headers.get(0).select("td").get(3).text();
        String server;
        if (headers.get(0).select("tr").get(2).text().isBlank() || headers.get(0).select("tr").get(2).text().equals("Server")) {
            server = "";
        } else {
            server = headers.get(0).select("tr").get(2).text().substring(7);
        }
        String leaderName = headers.get(0).select("td").get(7).text();
        return List.of(
                offclassingAllowed,
                config,
                server,
                leaderName
        );
    }
}
