package com.tf2center.discordbot.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tf2center.discordbot.parser.dto.*;
import com.tf2center.discordbot.parser.dto.tf2classes.*;
import com.tf2center.discordbot.parser.exceptions.TF2CParsingException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

    //Where each node is a sub spot
    private static Set<MainPageObject> extractSubs(JsonNode jsonNode) {
        Set<MainPageObject> result = new HashSet<>();
        for (JsonNode node : jsonNode) {
            result.add(
                new SubsDTO.Builder()
                    .lobbyId(
                        Integer.parseInt(node.findValue("lobbyNo").toString().replaceAll("\"", ""))
                    )
                    .region(
                        node.findValue("region").toString().replaceAll("\"", "")
                    )
                    .vcRequired(
                        Boolean.parseBoolean(node.findValue("mumbleRequired").toString().replaceAll("\"", ""))
                    )
                    .map(
                        node.findValue("map").toString().replaceAll("\"", "")
                    )
                    .team(
                        TF2Team.fromString(node.findValue("team").toString().replaceAll("\"", ""))
                    )
                    .roleInTeam(
                        node.findValue("tf2Class").toString().replaceAll("\"", "")
                    )
                    .className(
                        node.findValue("className").toString().replaceAll("\"", "")
                    )
                    .joinLink(
                        node.findValue("joinLink").toString().replaceAll("\"", "")
                    )
                    .gameType(
                        GameType.fromString(node.findValue("gameType").toString().replaceAll("\"", ""))
                    )
                    .regionLock(
                        Boolean.parseBoolean(node.findValue("regionLock").toString().replaceAll("\"", ""))
                    )
                    .advanced(
                        Boolean.parseBoolean(node.findValue("advanced").toString().replaceAll("\"", ""))
                    )
                    .build()
            );
        }
        return Set.copyOf(result);
    }

    public Map<String, Collection<MainPageObject>> parse() {
        final Map<String, Collection<MainPageObject>> result = new HashMap<>();

        try {
            tf2cWebSite = Jsoup.connect(TF2C_URL).userAgent("Mozilla").get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //Get the lobbies information
        Optional<Element> any = tf2cWebSite.getElementsByTag("script").stream().filter(e -> e.toString().contains("replaceAllLobbies")).findAny();

        //Parse out lobbies from HTML
        String lobbiesJson = any.get().toString().substring(49);
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

//        For local testing with a html file
//        try {
//            tf2cWebSite = Jsoup.parse(new File("/home/user/IdeaProjects/new-discord-bot/json-state-examples/subsFullPage"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        //Put subs in the result Map
        if (jsonNodeSubs.isEmpty()) {
            result.put("Subs", Collections.emptySet());
        } else {
            result.put("Subs", extractSubs(jsonNodeSubs));
        }

        //Put player numbers in the result Map
        result.put(
            "Players",
            List.of(PlayerCount.of(Integer.parseInt(tf2cWebSite.getElementById("euPlayersOnline").text()) +
                Integer.parseInt(tf2cWebSite.getElementById("naPlayersOnline").text()) +
                Integer.parseInt(tf2cWebSite.getElementById("playersOnline").text()) +
                Integer.parseInt(tf2cWebSite.getElementById("otherPlayersOnline").text() +
                    Integer.parseInt(tf2cWebSite.getElementById("auPlayersOnline").text())))
            ));
        return Map.copyOf(result);
    }

    private Map<String, Collection<SlotDTO>> extractPlayers(Document page, GameType gameType) {
        Map<String, Collection<SlotDTO>> result = new HashMap<>();
        Elements lobbySlots = page.getElementsByClass("lobbySlot");
        List<SlotDTO> blu = new ArrayList<>();
        List<SlotDTO> red = new ArrayList<>();

        for (int i = 0; i < lobbySlots.size(); i++) {
            String playerName = lobbySlots.get(i).children().get(1).children().get(0).text();
            String steamIdProfile = lobbySlots.get(i).children().get(1).children().get(0).attributes().get("href");
            //Decide if add to blu team or red. Start with blu then red
            if (i < lobbySlots.size() / 2) {
                //Decide if slot is empty
                if (lobbySlots.get(i).attributes().toString().contains("filled")) {
                    blu.add(SlotDTO.of(playerName, steamIdProfile, false, TF2Team.BLU));
                } else {
                    blu.add(SlotDTO.of(true, TF2Team.BLU));
                }
            } else {
                //Decide if slot is empty
                if (lobbySlots.get(i).attributes().toString().contains("filled")) {
                    red.add(SlotDTO.of(playerName, steamIdProfile, false, TF2Team.RED));
                } else {
                    red.add(SlotDTO.of(true, TF2Team.RED));
                }
            }
        }
        Iterator<SlotDTO> bluIterator = blu.iterator();
        Iterator<SlotDTO> redIterator = red.iterator();
        switch (gameType) {
            case SIXES -> {
                for (SixesClasses clazz : SixesClasses.values()) {
                    if (bluIterator.hasNext() && redIterator.hasNext()) {
                        bluIterator.next().setTf2Class(Optional.of(clazz));
                        redIterator.next().setTf2Class(Optional.of(clazz));
                    }
                }
            }
            case HIGHLANDER -> {
                for (HighlanderClasses clazz : HighlanderClasses.values()) {
                    if (bluIterator.hasNext() && redIterator.hasNext()) {
                        bluIterator.next().setTf2Class(Optional.of(clazz));
                        redIterator.next().setTf2Class(Optional.of(clazz));
                    }
                }
            }
            case BBALL -> {
                for (BBallClasses clazz : BBallClasses.values()) {
                    if (bluIterator.hasNext() && redIterator.hasNext()) {
                        bluIterator.next().setTf2Class(Optional.of(clazz));
                        redIterator.next().setTf2Class(Optional.of(clazz));
                    }
                }
            }
            case ULTIDUO -> {
                for (UltiduoClasses clazz : UltiduoClasses.values()) {
                    if (bluIterator.hasNext() && redIterator.hasNext()) {
                        bluIterator.next().setTf2Class(Optional.of(clazz));
                        redIterator.next().setTf2Class(Optional.of(clazz));
                    }
                }
            }
            case FOURVSFOUR -> {
                for (FoursClasses clazz : FoursClasses.values()) {
                    if (bluIterator.hasNext() && redIterator.hasNext()) {
                        bluIterator.next().setTf2Class(Optional.of(clazz));
                        redIterator.next().setTf2Class(Optional.of(clazz));
                    }
                }
            }
            default -> throw new TF2CParsingException("Bad enum value. Sanity check.");
        }
        result.put("Blu", blu);
        result.put("Red", red);
        return Map.copyOf(result);
    }

    private Set<MainPageObject> extractLobbies(JsonNode jsonNode) {
        Document innerLobby;
        Map<String, Collection<SlotDTO>> teams;
        Set<LobbyDTO> result = new HashSet<>();

        //For each node where node is a single lobby.
        for (JsonNode node : jsonNode) {
            try {
                String lobbyId = node.fields().next().toString().substring(3);
                innerLobby = Jsoup.connect(TF2C_URL + "/" + lobbyId).userAgent("Mozilla").get();
            } catch (IOException e) {
                throw new RuntimeException("Encountered an issue while connecting to an inner lobby.");
            }

            teams = extractPlayers(
                innerLobby,
                GameType.fromString(node.findValue("gameType").toString().replaceAll("\"", ""))
            );

            Map<String, String> innerLobbyInfo = extractInnerLobbyInformation(innerLobby);

            result.add(new LobbyDTO.Builder()
                .id(
                    Integer.parseInt(node.findValue("no").toString().replaceAll("\"", ""))
                )
                .leaderSteamId(
                    Long.parseLong(node.findValue("leaderSteamId").toString().replaceAll("\"", ""))
                )
                .region(
                    node.findValue("region").toString().replaceAll("\"", "")
                )
                .vcRequired(
                    Boolean.parseBoolean(node.findValue("mumbleRequired").toString().replaceAll("\"", ""))
                )
                .gameType(
                    GameType.fromString(node.findValue("gameType").toString().replaceAll("\"", ""))
                )
                .isReady(
                    Boolean.parseBoolean(node.findValue("inReadyUpMode").toString().replaceAll("\"", ""))
                )
                .map(
                    node.findValue("map").toString().replaceAll("\"", "")
                )
                .thumbnailURL(
                    node.findValue("thumbnailUrl").toString().replaceAll("\"", "")
                )
                .playersInLobby(
                    Integer.parseInt(node.findValue("playersInLobby").toString().replaceAll("\"", ""))
                )
                .playersForGame(
                    Integer.parseInt(node.findValue("playersForGame").toString().replaceAll("\"", ""))
                )
                //TODO: implement restrictions for embeds as follows: Join [10/1000] where 10 = lobbies and 1000 = hours
                .restrictionsText(
                    node.findValue("restrictionsText").toString().replaceAll("\"", "")
                )
                .regionLock(
                    Boolean.parseBoolean(node.findValue("regionLock").toString().replaceAll("\"", ""))
                )
                .balancedLobby(
                    Boolean.parseBoolean(node.findValue("useBalancer").toString().replaceAll("\"", ""))
                )
                .advanced(
                    Boolean.parseBoolean(node.findValue("advanced").toString().replaceAll("\"", ""))
                )
                .teams(
                    teams
                )
                .offclassingAllowed(
                    Boolean.parseBoolean(innerLobbyInfo.get("Offclassing"))
                )
                .config(
                    innerLobbyInfo.get("Config")
                )
                .server(
                    innerLobbyInfo.get("Server")
                )
                .leaderName(
                    innerLobbyInfo.get("LeaderName")
                )
                .build()
            );
        }
        return Set.copyOf(result);
    }

    private Map<String, String> extractInnerLobbyInformation(Document page) {
        Elements headers = page.getElementsByClass("lobbyHeaderOptions");
        Map<String, String> result = new HashMap<>();
        if (headers.isEmpty()) {
            result.put("Offclassing", "false");
        } else {
            try {
                result.put(
                    "Offclassing", Boolean.valueOf(!headers.get(1).select("span").get(4).attributes().toString().contains("cross")).toString()
                );
            } catch (Exception e) {
                String string = e.fillInStackTrace().toString();
                throw new TF2CParsingException("Bad Offclassing index, again!");
            }
        }

        result.put(
            "Config", headers.get(0).select("td").get(3).text()
        );
        result.put("Server",
            (headers.get(0).select("tr").get(2).text().isBlank() || headers.get(0).select("tr").get(2).text().equals("Server")) ? "" :
                headers.get(0).select("tr").get(2).text().substring(7)
        );
        result.put(
            "LeaderName", headers.get(0).select("td").get(7).text()
        );
        return Map.copyOf(result);
    }

}
