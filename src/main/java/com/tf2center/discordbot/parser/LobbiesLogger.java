package com.tf2center.discordbot.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tf2center.discordbot.parser.dto.MainPageObject;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class LobbiesLogger {

    private StringBuilder sb = new StringBuilder();

    private Collection<?> lobbies;
    private Collection<?> subs;

    private LobbiesLogger(Map<String, Collection<MainPageObject>> mainPageObjects) {
        lobbies = mainPageObjects.get("Lobbies");
        subs = mainPageObjects.get("Subs");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("/home/user/lobbies.json"), lobbies);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static LobbiesLogger of(Map<String, Collection<MainPageObject>> mainPageObjects) {
        return new LobbiesLogger(mainPageObjects);
    }

    @Override
    public String toString() {
//        sb.append("All lobbies");
//        lobbies.forEach(lobby -> sb.append(lobby.toString() + '\n'));
//
//        sb.append("All subs");
//        subs.forEach(sub -> sb.append(sub.toString() + '\n'));
        return "test";
    }
}
