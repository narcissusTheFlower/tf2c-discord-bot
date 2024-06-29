package com.tf2center.discordbot.parser.html;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tf2center.discordbot.parser.dto.GameType;
import com.tf2center.discordbot.parser.dto.LobbyDTO;
import com.tf2center.discordbot.parser.dto.MainPageObject;
import com.tf2center.discordbot.parser.dto.SlotDTO;
import com.tf2center.discordbot.parser.exceptions.TF2CParsingException;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class MainPageParser {

    private static final Logger logger = LoggerFactory.getLogger(MainPageParser.class);
    private static final String TF2C_URL = "https://tf2center.com/lobbies";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static Document tf2cWebSite;

    private MainPageParser() {
    }

    public static MainPageParser getInstance() {
        return new MainPageParser();
    }

    public Map<String, Collection<MainPageObject>> parse() {
        final Map<String, Collection<MainPageObject>> result = new HashMap<>();

        try {
            tf2cWebSite = Jsoup.connect(TF2C_URL).userAgent("Mozilla").get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Get the lobbies information
        String json = tf2cWebSite.getElementById("idc")
            .getElementsByTag("script")
            .toString();
        if (!json.contains("replaceAllLobbies")) {
            throw new TF2CParsingException("Parsed wrong HTML tag. Looking for tag with lobbies, but found something else.");
        }

        //Parse out lobbies from HTML
        String lobbiesJson = json.substring(49);
        lobbiesJson = lobbiesJson.substring(0, lobbiesJson.length() - 11);

        JsonNode jsonNodeLobbies = null;
        try {
            jsonNodeLobbies = OBJECT_MAPPER.readTree(lobbiesJson);
        } catch (JsonProcessingException e) {
            throw new TF2CParsingException("Failed to parse lobbies json. Probably invalid json tree/structure.");
        }

        //Put lobbies in the result Map
        if (jsonNodeLobbies.isEmpty()) {
            result.put("Lobbies", Collections.emptySet());
        } else {
            result.put("Lobbies", extractLobbies(jsonNodeLobbies));
        }

//        try {tf2cWebSite = Jsoup.parse(new File("/home/user/IdeaProjects/new-discord-bot/json-state-examples/subsFullPage"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        //Parse out substitutes from HTML
        String substring = tf2cWebSite.select("script").stream()
            .filter(node -> node.toString().contains("refresh-substitutes"))
            .findAny()
            .get()
            .toString().substring(172);
        substring = substring.substring(0, substring.indexOf(";;") - 2);

        JsonNode jsonNodeSubs = null;
        try {
            jsonNodeSubs = OBJECT_MAPPER.readTree(substring);
        } catch (JsonProcessingException e) {
            throw new TF2CParsingException("Failed to parse subs json. Probably invalid json tree/structure.");
        }

        //Put subs in the result Map
        if (jsonNodeSubs.isEmpty()) {
            result.put("Subs", Collections.emptySet());
        } else {
            result.put("Subs", extractSubs(jsonNodeSubs));
        }

        return Map.copyOf(result);
    }

    private Set<MainPageObject> extractLobbies(JsonNode jsonNode) {
        Document innerLobby;
        Set<SlotDTO> slots;
        Set<LobbyDTO> result = new HashSet<>();

        //For each node where node is a single lobby.
        for (JsonNode node : jsonNode) {
            try {
                String lobbyId = node.fields().next().toString().substring(3);
                innerLobby = Jsoup.connect(TF2C_URL + "/" + lobbyId).userAgent("Mozilla").get();
            } catch (IOException e) {
                throw new RuntimeException("Encountered an issue while connecting to an inner lobby.");
            }

            slots = extractPlayers(
                innerLobby,
                GameType.valueOf(node.findValue("gameType").toString())
            );

            //Map<String, String> innerLobbyInfo = extractInnerLobbyInformation(innerLobby);

            result.add(new LobbyDTO.Builder()
                    .id(
                        Integer.parseInt(node.findValue("no").toString())
                    )
                    .leaderSteamId(
                        Long.parseLong(node.findValue("leaderSteamId").toString())
                    )
                    .region(
                        node.findValue("region").toString()
                    )
                    .vcRequired(
                        Boolean.parseBoolean(node.findValue("mumbleRequired").toString())
                    )
                    .gameType(GameType.valueOf(node.findValue("gameType").toString()))
//                    .isReady()
//                    .map()
//                    .thumbnailURL()
//                    .playersInLobby()
//                    .playersForGame()
//                    .restrictionsText()
//                    .regionLock()
//                    .balancedLobby()
//                    .advanced()
//                    .slots(slots)
//                    .playerSlotList()
//                    .offclassingAllowed(
//                            innerLobbyInfo.get("Offclassing")
//                    )
//                    .config(
//                            innerLobbyInfo.get("Config")
//                    )
//                    .server(
//                            innerLobbyInfo.get("Server")
//                    )
//                    .leaderName(
//                            innerLobbyInfo.get("LeaderName")
//                    )
                    .build()
            );
        }

        return Set.copyOf(result);
    }

    private Set<SlotDTO> extractPlayers(Document page, GameType gameType) {
        Set<SlotDTO> result = new HashSet<>();
        Elements lobbySlots = page.getElementsByClass("lobbySlot");

        switch (gameType) {
            case SIXES -> {
                lobbySlots.forEach(element -> {
                    if (element.attributes().toString().contains("filled")) {
                        String playerName = element.children().get(1).children().get(0).text();
                        String steamIdProfile = element.children().get(1).children().get(0).attributes().get("href");
                        Pair<>
                        result.add(SlotDTO.of(playerName, steamIdProfile, false, ));
                    } else {
                        result.add(SlotDTO.of(true));
                    }

                });
            }
            case HIGHLANDER -> {
            }
            case BBALL -> {
            }
            case ULTIDUO -> {
            }
            case FOURVSFOUR -> {
            }
            default -> throw new TF2CParsingException("Bad enum value. Sanity check.");
        }

        lobbySlots.forEach(element -> {
                if (element.attributes().toString().contains("filled")) {
                    String playerName = element.children().get(1).children().get(0).text();
                    String steamIdProfile = element.children().get(1).children().get(0).attributes().get("href");

                    result.add(SlotDTO.of(playerName, steamIdProfile, false, null, null));
                } else {
                    result.add(SlotDTO.of(true));
                }
            }
        );


        return Set.copyOf(result);
    }

    private Map<String, String> extractInnerLobbyInformation(Document page) {
        Elements headers = page.getElementsByClass("lobbyHeaderOptions");
        Map<String, String> result = new HashMap<>();
        //
        //Wrapper boolean because I need toString() method.
        result.put("Offclassing", Boolean.valueOf(!headers.get(1).select("span").get(4).attributes().toString().contains("cross")).toString());
        result.put("Config", headers.get(0).select("td").get(3).text());
        result.put("Server",
            (headers.get(0).select("tr").get(2).text().isBlank() || headers.get(0).select("tr").get(2).text().equals("Server")) ? "" :
                headers.get(0).select("tr").get(2).text().substring(7)
        );
        result.put("LeaderName", headers.get(0).select("td").get(7).text());
        return Map.copyOf(result);
    }


    private static Set<MainPageObject> extractSubs(JsonNode jsonNode) {
        Set<MainPageObject> result = new HashSet<>();

        return Set.copyOf(result);
    }

    private class infoExtractor {


    }

}
