package com.tf2center.discordbot.parser.discord.embeds;

import com.tf2center.discordbot.parser.dto.*;
import com.tf2center.discordbot.parser.dto.tf2classes.TF2Class;
import com.tf2center.discordbot.steamapi.SteamApiCaller;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.stream.Collectors;

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
        allEmbeds.put("EmbedSubs", Set.of(buildSubs(subs)));
        return allEmbeds;
    }

    private Set<EmbedCreateSpec> buildLobbies(Collection<MainPageObject> mainPageObjectsLobbies) {
        return mainPageObjectsLobbies.stream()
            .map(obj -> (LobbyDTO) obj)
            .map(lobbyDTO -> EmbedCreateSpec.builder()
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
                    buildDescription(lobbyDTO)
                )
                .thumbnail(
                    buildThumbnail(lobbyDTO.getRegion())
                )
                .addFields(
                    buildTeams(lobbyDTO)
                )
                .image(
                    "https://tf2center.com" + lobbyDTO.getThumbnailURL()
                )
                .build())
            .collect(Collectors.toSet());
    }

    private EmbedCreateFields.Author buildAuthor(String leaderName, long leaderSteamId) {
        String avatarUrl = SteamApiCaller.getPlayerAvatar(leaderSteamId);
        String fullSteamUrl = "https://steamcommunity.com/profiles/" + leaderSteamId;
        return EmbedCreateFields.Author.of(
            String.format("Leader: %s", leaderName),
            fullSteamUrl,
            avatarUrl);
    }

    private String buildDescription(LobbyDTO lobbyDTO) {
        String blueX = "\uD83c\uDDFD";
        String greenCheck = "✅";
        String offclassing = "Offclassing allowed: " + (lobbyDTO.isOffclassingAllowed() ? greenCheck : blueX) + "\n";
        String voice = "Mumble required: " + (lobbyDTO.isVcRequired() ? greenCheck : blueX) + "\n";
        String advanced = "Advanced lobby: " + (lobbyDTO.isAdvanced() ? greenCheck : blueX) + "\n";
        String balancing = "Balanced lobby: " + (lobbyDTO.isBalancedLobby() ? greenCheck : blueX) + "\n";
        String region = "Region lock: " + (lobbyDTO.isRegionLocked() ? greenCheck : blueX);
        if (lobbyDTO.getGameType().equals(GameType.SIXES)) {
            return offclassing + voice + advanced + balancing + region;
        }
        return voice + advanced + balancing + region;
    }

    private String buildThumbnail(String region) {
        if (region.equals("EU")) {
            return "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b7/Flag_of_Europe.svg/1280px-Flag_of_Europe.svg.png";
        } else {
            return "https://upload.wikimedia.org/wikipedia/commons/thumb/8/80/Flag_of_the_United_States_%281877%E2%80%931890%29.svg/2560px-Flag_of_the_United_States_%281877%E2%80%931890%29.svg.png";
        }
    }

    private String buildTitle(LobbyDTO lobbyDTO) {
        String readyState = lobbyDTO.isReady() ? "Ready UP 🔥\n" : "Has not started yet 🕜\n";
        return String.format("Lobby #%d | %s\n%s", lobbyDTO.getId(), lobbyDTO.getMap(), readyState);
    }

    private EmbedCreateFields.Field[] buildTeams(LobbyDTO lobbyDTO) {
        Map<String, Collection<SlotDTO>> slots = lobbyDTO.getTeams();

        //Blu team
        List<EmbedCreateFields.Field> tempBLU = new ArrayList<>();
        slots.get("Blu").forEach(bluSlot -> tempBLU.add(
                EmbedCreateFields.Field.of(
                    bluSlot.getTf2Class().getDiscordClassValue(),
                    bluSlot.isEmpty() ? buildLobbyJoinLink(String.valueOf(lobbyDTO.getId()), bluSlot) : bluSlot.getPlayerName(),
                    true)
        ));

        tempBLU.add(EmbedCreateFields.Field.of("\u200B", "\u200B", false));

        EmbedCreateFields.Field[] teamBlu = new EmbedCreateFields.Field[(slots.get("Blu").size()) + 2]; //+2 cuz header with "BLU TEAM" and "\u200B"
        teamBlu[0] = EmbedCreateFields.Field.of("📘BLU TEAM", "", false);
        for (int i = 1; i < teamBlu.length; i++) {
            teamBlu[i] = tempBLU.get(i - 1);
        }

        //Red team
        List<EmbedCreateFields.Field> tempRED = new ArrayList<>();
        slots.get("Red").forEach(redSlot -> tempRED.add(
                EmbedCreateFields.Field.of(
                    redSlot.getTf2Class().getDiscordClassValue(),
                    redSlot.isEmpty() ? buildLobbyJoinLink(String.valueOf(lobbyDTO.getId()), redSlot) : redSlot.getPlayerName(),
                    true)
        ));

        EmbedCreateFields.Field[] teamRed = new EmbedCreateFields.Field[(slots.get("Red").size()) + 1];//+1 cuz header with "RED TEAM"
        teamRed[0] = EmbedCreateFields.Field.of("📕RED TEAM", "", false);
        for (int i = 1; i < teamRed.length; i++) {
            teamRed[i] = tempRED.get(i - 1);
        }
        return ArrayUtils.addAll(teamBlu, teamRed);
    }

    private String buildLobbyJoinLink(String lobbyId, SlotDTO bluSlot) {
        TF2Class tf2Class = bluSlot.getTf2Class();
        return new StringBuilder()
            .append("[Join](https://tf2center.com/join/lobby/")
            .append(lobbyId)
            .append("/")
            .append(bluSlot.getTeam().getTeamString())
            .append("/")
            .append(tf2Class.getDiscordClassKey())
            .append(")")
            .toString();
    }

    private EmbedCreateSpec buildSubs(Collection<MainPageObject> mainPageObjectsSubs) {
        return EmbedCreateSpec.builder()
            .color(Color.of(133, 63, 63)) //From tf2c colour pallet. Redish
            .title("Substitute slots to join.")
            .description(mainPageObjectsSubs.isEmpty() ? "No substitute slots at this time" : "")
            .addFields(
                buildFields(mainPageObjectsSubs)
            )
            //TODO change source of the image
            .image("https://raw.githubusercontent.com/narcissusTheFlower/tf2-stats-browser-extension/c13a5552564c58fd06b1dcaa19357b419f4761b9/discord-bot-firestarter.png")
            .build();
    }

    private EmbedCreateFields.Field[] buildFields(Collection<MainPageObject> mainPageObjectsSubs) {
        EmbedCreateFields.Field[] subs = new EmbedCreateFields.Field[mainPageObjectsSubs.size()];
        int counter = 0;
        for (MainPageObject mainPageObjectsSub : mainPageObjectsSubs) {
            SubsDTO subsDTO = (SubsDTO) mainPageObjectsSub;
            subs[counter] = EmbedCreateFields.Field.of(
                buildName(subsDTO),
                buildSubsJoinLink(subsDTO),
                false);
            counter++;
        }
        return ArrayUtils.addAll(subs);
    }

    private String buildName(SubsDTO subSlot) {
        return subSlot.getClassName() + " | " + subSlot.getRegion() + " | " + subSlot.getGameType() + " | " + subSlot.getMap();
    }

    private String buildSubsJoinLink(SubsDTO subSlot) {
        return "[Join](https://tf2center.com" + subSlot.getJoinLink() + ")";
    }


}
