package com.tf2center.discordbot.embeds;

import com.tf2center.discordbot.dto.TF2CLobby;
import com.tf2center.discordbot.dto.TF2CPlayerSlotDTO;
import com.tf2center.discordbot.exceptions.TF2CEmbedBuilderException;
import com.tf2center.discordbot.steamapi.SteamApiCaller;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.apache.commons.lang3.ArrayUtils;

import java.time.Instant;
import java.util.*;

public class LobbyPreviewEmbedBuilder {

    private final Set<TF2CLobby> jsonParsedPreviews;
    private long lobbyId;
    private String teamType;

    private LobbyPreviewEmbedBuilder(Set<TF2CLobby> jsonParsedPreviews) {
        this.jsonParsedPreviews = jsonParsedPreviews;
    }

    public static LobbyPreviewEmbedBuilder of(Set<TF2CLobby> previews) {
        return new LobbyPreviewEmbedBuilder(previews);
    }

    public Map<TF2CLobbyId, EmbedCreateSpec> build() {
        //Flux<TF2CLobbyPreviewDTO>
        //Set<EmbedCreateSpec> result = new LinkedHashSet<>();
        Map<TF2CLobbyId, EmbedCreateSpec> result = new HashMap<>();

        jsonParsedPreviews.forEach(json -> {
            lobbyId = json.getLobbyId();
            teamType = json.getGameType();
            EmbedCreateSpec lobby = EmbedCreateSpec.builder()
                    .author(
                            buildAuthor(json.getLeaderName(), json.getLeaderSteamId())
                    )
                    .color(Color.GREEN)
                    .title(buildTitle(json))
                    .url("https://tf2center.com/lobbies/" + json.getLobbyId())
                    .description(
                            String.valueOf(
                                    buildDescription(json)
                            )
                    )
                    .thumbnail(
                            buildThumbnail(json.getRegion())
                    )
                    .addFields(
                            buildTeams(json.getPlayerSlotList())
                    )
                    .image(
                            "https://tf2center.com" + json.getThumbnailUrl()
                    )
                    //TODO build proper timings
                    .timestamp(Instant.now())
                    .footer("Lobby opened", "https://static-00.iconduck.com/assets.00/four-o-clock-emoji-2047x2048-dqpvucft.png")
                    .build();

            result.put(new TF2CLobbyId(json.getLobbyId()), lobby);
        });

        return Map.copyOf(result);
    }

    private String buildTitle(TF2CLobby json) {
        String readyState = json.isInReadyUpMode() ? "Ready UP 🔥\n" : "Has not started yet 🕜\n";
        return String.format("Lobby #%d | %s\n%s", json.getLobbyId(), json.getMap(), readyState);
    }

    private EmbedCreateFields.Author buildAuthor(String leaderName, long leaderSteamId) {
        String avatarUrl = SteamApiCaller.getPlayerAvatar(leaderSteamId);
        String fullSteamUrl = "https://steamcommunity.com/profiles/" + leaderSteamId;
        return EmbedCreateFields.Author.of(
                String.format("Leader: %s", leaderName),
                fullSteamUrl,
                avatarUrl);
    }

    private StringBuffer buildDescription(TF2CLobby json) {
        String blueX = "\uD83c\uDDFD";
        String greenCheck = "✅";
        String offclassing = "Offclassing allowed: " + (json.isOffclassingAllowed() ? greenCheck : blueX) + "\n";
        String voice = "Mumble required: " + (json.isVoiceCommunicationRequired() ? greenCheck : blueX) + "\n";
        String advanced = "Advanced lobby: " + (json.isAdvanced() ? greenCheck : blueX) + "\n";
        String balancing = "Balanced lobby: " + (json.isBalancedLobby() ? greenCheck : blueX) + "\n";
        String region = "Region lock: " + (json.isRegionLocked() ? greenCheck : blueX);
        if (teamType.equals("6v6")) {
            return new StringBuffer().append(offclassing).append(voice).append(advanced).append(balancing).append(region);
        }
        return new StringBuffer().append(voice).append(advanced).append(balancing).append(region);
    }

    private String buildThumbnail(String region) {
        //TODO change resource origins and fix their sizes to be the same dimensions
        if (region.equals("EU")) {
            return "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b7/Flag_of_Europe.svg/1280px-Flag_of_Europe.svg.png";
        } else {
            return "https://upload.wikimedia.org/wikipedia/commons/thumb/8/80/Flag_of_the_United_States_%281877%E2%80%931890%29.svg/2560px-Flag_of_the_United_States_%281877%E2%80%931890%29.svg.png";
        }
    }


    private EmbedCreateFields.Field[] buildTeams(List<TF2CPlayerSlotDTO> slots) {
        byte teamSize = (byte) slots.size();

        List<TF2CPlayerSlotDTO> blu = assignClasses(slots.subList(0, teamSize / 2)); //First half of the list
        List<TF2CPlayerSlotDTO> red = assignClasses(slots.subList(teamSize / 2, teamSize)); //Second half of the list

        EmbedCreateFields.Field[] teamBlu = new EmbedCreateFields.Field[(teamSize / 2) + 2];
        teamBlu[0] = EmbedCreateFields.Field.of("📘BLU TEAM", "", false);
        for (int i = 1; i < teamBlu.length; i++) {
            if (i == teamBlu.length - 1) {
                teamBlu[i] = EmbedCreateFields.Field.of("\u200B", "\u200B", false);
                break;
            }
            teamBlu[i] = EmbedCreateFields.Field.of(
                    blu.get(i - 1).getTf2Class(),
                    blu.get(i - 1).getPlayerName().equals("empty") ? "[Join](https://tf2center.com/lobbies/" + lobbyId + ")" : blu.get(i - 1).getPlayerName(),
                    true);
        }

        EmbedCreateFields.Field[] teamRed = new EmbedCreateFields.Field[(teamSize / 2) + 1];
        teamRed[0] = EmbedCreateFields.Field.of("📕RED TEAM", "", false);
        for (int i = 1; i < teamRed.length; i++) {
            teamRed[i] = EmbedCreateFields.Field.of(
                    red.get(i - 1).getTf2Class(),
                    red.get(i - 1).getPlayerName().equals("empty") ? "[Join](https://tf2center.com/lobbies/" + lobbyId + ")" : red.get(i - 1).getPlayerName(),
                    true);
        }
        return ArrayUtils.addAll(teamBlu, teamRed);
    }

    private List<TF2CPlayerSlotDTO> assignClasses(List<TF2CPlayerSlotDTO> singleTeam) {
        return switch (teamType) {
            case "Highlander" -> {

                Iterator<TF2CPlayerSlotDTO> iterator = singleTeam.iterator();
                Highlander highlander = new Highlander();
                for (int i = 0; i < singleTeam.size(); i++) {
                    if (iterator.hasNext()) {
                        singleTeam.get(i).setTf2Class(
                                highlander.assignClass()
                        );
                    }
                }
                yield List.copyOf(singleTeam);
            }
            case "6v6" -> {
                Iterator<TF2CPlayerSlotDTO> iterator = singleTeam.iterator();
                Sixes sixes = new Sixes();
                for (int i = 0; i < singleTeam.size(); i++) {
                    if (iterator.hasNext()) {
                        singleTeam.get(i).setTf2Class(
                                sixes.assignClass()
                        );
                    }
                }
                yield List.copyOf(singleTeam);
            }
            case "4v4" -> {
                Iterator<TF2CPlayerSlotDTO> iterator = singleTeam.iterator();
                Fours fours = new Fours();
                for (int i = 0; i < singleTeam.size(); i++) {
                    if (iterator.hasNext()) {
                        singleTeam.get(i).setTf2Class(
                                fours.assignClass()
                        );
                    }
                }
                yield List.copyOf(singleTeam);
            }
            case "Ultiduo" -> {
                Iterator<TF2CPlayerSlotDTO> iterator = singleTeam.iterator();
                Ultiduo ultiduo = new Ultiduo();
                for (int i = 0; i < singleTeam.size(); i++) {
                    if (iterator.hasNext()) {
                        singleTeam.get(i).setTf2Class(
                                ultiduo.assignClass()
                        );
                    }
                }
                yield List.copyOf(singleTeam);
            }
            case "BBall" -> {
                Iterator<TF2CPlayerSlotDTO> iterator = singleTeam.iterator();
                BBall BBall = new BBall();
                for (int i = 0; i < singleTeam.size(); i++) {
                    if (iterator.hasNext()) {
                        singleTeam.get(i).setTf2Class(
                                BBall.assignClass()
                        );
                    }
                }
                yield List.copyOf(singleTeam);
            }
            default -> throw new TF2CEmbedBuilderException("Failed to determine team type: " + teamType);
        };
    }

    private abstract class TeamType {

        private Iterator<String> iterator;

        public void configure(List<String> classes) {
            this.iterator = classes.iterator();
        }

        String assignClass() {
            if (iterator.hasNext()) {
                return iterator.next();
            }
            return "eof";
        }
    }

    private class Highlander extends TeamType {
        public Highlander() {
            super.configure(List.of(
                    "⚾Scout",
                    "\uD83D\uDE80Soldier",
                    "🔥Pyro",
                    "🧨Demo",
                    "\uD83D\uDD27Engineer",
                    "🐤Heavy",
                    "💊Medic",
                    "🎯Sniper",
                    "\uD83D\uDD2ASpy"
            ));
        }
    }

    private class Sixes extends TeamType {
        public Sixes() {
            super.configure(List.of(
                    "⚾Scout",
                    "⚾Scout",
                    "\uD83D\uDE80Roamer",
                    "\uD83D\uDE80Pocket",
                    "🧨Demo",
                    "💊Medic"
            ));
        }
    }

    private class Fours extends TeamType {
        public Fours() {
            super.configure(List.of(
                    "⚾Scout",
                    "\uD83D\uDE80Soldier",
                    "🧨Demo",
                    "💊Medic"
            ));
        }
    }

    private class Ultiduo extends TeamType {
        public Ultiduo() {
            super.configure(List.of(
                    "\uD83D\uDE80Soldier",
                    "💊Medic"
            ));
        }
    }

    private class BBall extends TeamType {
        public BBall() {
            super.configure(List.of(
                    "\uD83D\uDE80Soldier",
                    "\uD83D\uDE80Soldier",
                    "\uD83D\uDE80Soldier",
                    "\uD83D\uDE80Soldier"
            ));
        }
    }
}
