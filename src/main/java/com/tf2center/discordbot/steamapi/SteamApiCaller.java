package com.tf2center.discordbot.steamapi;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SteamApiCaller {

    private static final OkHttpClient CLIENT = new OkHttpClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String STEAM_API_KEY = System.getenv("STEAM_API_KEY");
    private static String url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" + STEAM_API_KEY + "&steamids=";

    public static String getPlayerAvatar(long steamId) {
        String json = null;
        String avatarUrl = null;
        try {
            json = httpCall(url + steamId);
            json = json.substring(24);
            json = json.substring(0, json.length() - 3);
            avatarUrl = OBJECT_MAPPER.readTree(json).path("avatarfull").asText();
        } catch (IOException e) {
            throw new RuntimeException("STEAM_API exception!",e);
        }
        return avatarUrl;
    }

    private static String httpCall(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = CLIENT.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
