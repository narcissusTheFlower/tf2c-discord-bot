package com.tf2center.discordbot.steamapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class SteamApiCaller {

    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final static String STEAM_API_KEY = System.getenv("STEAM_API_KEY");

    //private static URL url = new URL("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key="+STEAM_API_KEY+"&steamids=");
    private static String url = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" + STEAM_API_KEY + "&steamids=";

    public static String getPlayerAvatar(long steamId) {
        String json = null;
        String avatarUrl = null;
        try {
            json = httpCall(url + steamId);
            avatarUrl = objectMapper.readValue(json, SteamResponseDTO.class).avatarFull;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return avatarUrl;
    }

    private static String httpCall(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private class SteamResponseDTO {
        private String avatarFull;

        public String getAvatarFull() {
            return avatarFull;
        }

        public void setAvatarFull(String avatarFull) {
            this.avatarFull = avatarFull;
        }
//        private long steamid;
//        communityvisibilitystate
//
//                profilestate
//        personaname
//                profileurl
//        avatar
//                avatarmedium

//                avatarhash
//        personastate
//                realname
//        {
//            "steamid": "76561197960435530",
//                "communityvisibilitystate": 3,
//                "profilestate": 1,
//                "personaname": "Robin",
//                "profileurl": "https://steamcommunity.com/id/robinwalker/",
//                "avatar": "https://avatars.steamstatic.com/81b5478529dce13bf24b55ac42c1af7058aaf7a9.jpg",
//                "avatarmedium": "https://avatars.steamstatic.com/81b5478529dce13bf24b55ac42c1af7058aaf7a9_medium.jpg",
//                "avatarfull": "https://avatars.steamstatic.com/81b5478529dce13bf24b55ac42c1af7058aaf7a9_full.jpg",
//                "avatarhash": "81b5478529dce13bf24b55ac42c1af7058aaf7a9",
//                "personastate": 0,
//                "realname": "Robin Walker",
//                "primaryclanid": "103582791429521412",
//                "timecreated": 1063407589,
//                "personastateflags": 0,
//                "loccountrycode": "US",
//                "locstatecode": "WA",
//                "loccityid": 3961
//        }
    }
}
