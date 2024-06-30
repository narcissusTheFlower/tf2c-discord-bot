package com.tf2center.discordbot.parser.discord.embeds;

import com.tf2center.discordbot.dto.json.TF2CSubstituteSlotDTO;
import com.tf2center.discordbot.parser.dto.GameType;
import com.tf2center.discordbot.parser.dto.LobbyDTO;
import com.tf2center.discordbot.parser.dto.MainPageObject;
import com.tf2center.discordbot.steamapi.SteamApiCaller;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;

import java.util.*;

public class EmbedsBuilder {


    private EmbedsBuilder() {
    }

    public static EmbedsBuilder getInstance() {
        return new EmbedsBuilder();
    }

    public Map<String, Set<EmbedCreateSpec>> build(Map<String, Collection<MainPageObject>> mainPageObjects) {
        Map<String, Set<EmbedCreateSpec>> allEmbeds = new HashMap<>();
        Collection<MainPageObject> lobbies = mainPageObjects.get("Lobbies");
        Collection<MainPageObject> subs = mainPageObjects.get("Subs");

        allEmbeds.put("EmbedLobbies", buildLobbies(lobbies));
//        allEmbeds.put("EmbedSubs", buildSubs(subs));
        return allEmbeds;
    }

    private Set<EmbedCreateSpec> buildLobbies(Collection<MainPageObject> mainPageObjectsLobbies) {
        Set<EmbedCreateSpec> embedLobbies = new HashSet<>();
        for (MainPageObject mainPageObject : mainPageObjectsLobbies) {
            LobbyDTO lobbyDTO = (LobbyDTO) mainPageObject;
            embedLobbies.add(
                EmbedCreateSpec.builder()
                    .author(
                        buildAuthor(lobbyDTO.getLeaderName(), lobbyDTO.getLeaderSteamId())
                    )
                    .color(
                        Color.GREEN
                    )
                    .title(
                        buildTitle(lobbyDTO)
                    )
                    .url(
                        "https://tf2center.com/lobbies/" + lobbyDTO.getId()
                    )
                    .description(
                        String.valueOf(
                            buildDescription(lobbyDTO)
                        )
                    )
                    .thumbnail(
                        buildThumbnail(lobbyDTO.getRegion())
                    )
//                    .addFields(
//                        buildTeams(json.getPlayerSlotList())
//                    )
                    .image(
                        "https://tf2center.com" + lobbyDTO.getThumbnailURL()
                    )
                    .build()
            );
        }
        return embedLobbies;
    }

    private EmbedCreateFields.Author buildAuthor(String leaderName, long leaderSteamId) {
        String avatarUrl = SteamApiCaller.getPlayerAvatar(leaderSteamId);
        String fullSteamUrl = "https://steamcommunity.com/profiles/" + leaderSteamId;
        return EmbedCreateFields.Author.of(
            String.format("Leader: %s", leaderName),
            fullSteamUrl,
            avatarUrl);
    }

    private StringBuffer buildDescription(LobbyDTO lobbyDTO) {
        String blueX = "\uD83c\uDDFD";
        String greenCheck = "✅";
        String offclassing = "Offclassing allowed: " + (lobbyDTO.isOffclassingAllowed() ? greenCheck : blueX) + "\n";
        String voice = "Mumble required: " + (lobbyDTO.isVcRequired() ? greenCheck : blueX) + "\n";
        String advanced = "Advanced lobby: " + (lobbyDTO.isAdvanced() ? greenCheck : blueX) + "\n";
        String balancing = "Balanced lobby: " + (lobbyDTO.isBalancedLobby() ? greenCheck : blueX) + "\n";
        String region = "Region lock: " + (lobbyDTO.isRegionLocked() ? greenCheck : blueX);
        if (lobbyDTO.getGameType().equals(GameType.SIXES)) {
            return new StringBuffer().append(offclassing).append(voice).append(advanced).append(balancing).append(region);
        }
        return new StringBuffer().append(voice).append(advanced).append(balancing).append(region);
    }

    private String buildThumbnail(String region) {
        if (region.equals("EU")) {
            return "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b7/Flag_of_Europe.svg/1280px-Flag_of_Europe.svg.png";
        } else {
            return "https://upload.wikimedia.org/wikipedia/commons/thumb/8/80/Flag_of_the_United_States_%281877%E2%80%931890%29.svg/2560px-Flag_of_the_United_States_%281877%E2%80%931890%29.svg.png";
        }
    }

    private String buildTitle(LobbyDTO lobbyDTO) {
        String readyState = lobbyDTO.isVcRequired() ? "Ready UP 🔥\n" : "Has not started yet 🕜\n";
        return String.format("Lobby #%d | %s\n%s", lobbyDTO.getId(), lobbyDTO.getMap(), readyState);
    }


    public EmbedCreateSpec buildSubs(Collection<MainPageObject> mainPageObjectsSubs) {
        EmbedCreateSpec test = EmbedCreateSpec.builder()
            .color(Color.of(133, 63, 63)) //From tf2c colour pallet. Redish
            .title("Substitute slots to join.")
//            .description(substituteSlots.isEmpty() ? "No substitute slots at this moment" : "")
//            .addFields(buildFields())
            //TODO chacge source of the image
            .image("https://raw.githubusercontent.com/narcissusTheFlower/tf2-stats-browser-extension/c13a5552564c58fd06b1dcaa19357b419f4761b9/discord-bot-firestarter.png")
            .build();
        return test;
    }

//    private EmbedCreateFields.Field[] buildFields() {
//        EmbedCreateFields.Field[] subs = new EmbedCreateFields.Field[substituteSlots.size()];
//        int counter = 0;
//        for (TF2CSubstituteSlotDTO substituteSlot : substituteSlots) {
//            subs[counter] = EmbedCreateFields.Field.of(
//                buildName(substituteSlot),
//                buildJoinLink(substituteSlot),
//                false);
//            counter++;
//        }
//        return ArrayUtils.addAll(subs);
//    }

    private String buildName(TF2CSubstituteSlotDTO slot) {
        return new StringBuilder()
            .append(slot.getClassName())
            .append(" | ")
            .append(slot.getRegion())
            .append(" | ")
            .append(slot.getGameType())
            .append(" | ")
            .append(slot.getMap())
            .toString();
    }

    private String buildJoinLink(TF2CSubstituteSlotDTO slot) {
        return new StringBuilder()
            .append("[Join](https://tf2center.com")
            .append(slot.getJoinLink())
            .append(")")
            .toString();
    }


}
