package com.tf2center.discordbot.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tf2center.discordbot.parser.dto.website.MainPageObject;
import com.tf2center.discordbot.parser.dto.website.SubsDTO;
import com.tf2center.discordbot.parser.exceptions.TF2CParsingException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

    public static Map<String, Collection<MainPageObject>> parse() {
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

        String lobbiesJson = json.substring(49);
        lobbiesJson = lobbiesJson.substring(0, lobbiesJson.length() - 11);

        JsonNode jsonNodeLobbies = null;
        try {
            jsonNodeLobbies = OBJECT_MAPPER.readTree(lobbiesJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //Put lobbies in the result Map
        if (jsonNodeLobbies.isEmpty()) {
            result.put("Lobbies", Collections.emptySet());
        } else {
            result.put("Lobbies", extractLobbies(jsonNodeLobbies));
        }

//        try {
//            tf2cWebSite = Jsoup.parse(new File("/home/user/IdeaProjects/new-discord-bot/json-state-examples/subsFullPage"));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        //Parse out substitutes from HTML
        String substring = tf2cWebSite.select("script").stream()
            .filter(node -> node.toString().contains("refresh-substitutes"))
            .findAny()
            .get()
            .toString().substring(164);
        substring = substring.substring(0, substring.indexOf(";;") - 1);
        JsonNode jsonNodeSubs = null;

        try {
            jsonNodeSubs = OBJECT_MAPPER.readTree(substring);
            System.out.println();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //Put subs in the result Map
        if (jsonNodeSubs.isEmpty()) {
            result.put("Subs", Collections.emptySet());
        } else {
            //result.put("Subs",extractSubs(jsonNode));
        }

        return Map.copyOf(result);
    }

    private static Set<MainPageObject> extractLobbies(JsonNode jsonNode) {


//        return Set.copyOf();
        return null;
    }


    private static Set<SubsDTO> extractSubs() {


//        return  Set.copyOf();
        return null;
    }


}
