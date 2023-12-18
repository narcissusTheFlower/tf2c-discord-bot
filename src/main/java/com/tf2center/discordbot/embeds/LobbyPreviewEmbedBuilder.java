package com.tf2center.discordbot.embeds;

import com.tf2center.discordbot.dto.json.tf2cpreview.TF2CLobbyPreview;
import com.tf2center.discordbot.dto.teams.TF2CPlayerSlot;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;

import java.time.Instant;
import java.util.*;

public class LobbyPreviewEmbedBuilder {

    private final Set<TF2CLobbyPreview> jsonParsedPreviews;
    private long lobbyId;
    private String teamType;

    private LobbyPreviewEmbedBuilder(Set<TF2CLobbyPreview> jsonParsedPreviews) {
        this.jsonParsedPreviews = jsonParsedPreviews;
    }

    public static LobbyPreviewEmbedBuilder of(Set<TF2CLobbyPreview> previews) {
        return new LobbyPreviewEmbedBuilder(previews);
    }

    public Set<EmbedCreateSpec> build() {
        //Flux<TF2CLobbyPreview>
        HashSet<EmbedCreateSpec> result = new HashSet<>();

        jsonParsedPreviews.forEach(json -> {
            lobbyId = json.getLobbyId();
            teamType = json.getGameType();
            EmbedCreateSpec lobby = EmbedCreateSpec.builder()
                    .color(Color.GREEN)
                    .title("Lobby #" + json.getLobbyId())
                    .url("https://tf2center.com/lobbies/" + json.getLobbyId())
                    .description(
                            buildDescription(
                                    json.isVoiceCommunicationRequired(),
                                    json.isRegionLocked(),
                                    json.isBalancedLobby(),
                                    json.isOffclassingAllowed())
                    )
                    .thumbnail(
                            buildThumbnail(json.getRegion())
                    )
                    .addFields(
                            composeTeams(json.getPlayerSlotList())
                    )
                    .image(
                            "https://tf2center.com" + json.getMap()
                    )
                    .timestamp(Instant.now())
                    .footer("Lobby opened", "🕖")
                    .build();

            result.add(lobby);
        });

        return result;
    }

    private String buildDescription(boolean isVoiceRequired, boolean isRegionLocked, boolean isBalanced, boolean offclassingAllowed) {
        String blueX = "\uD83c\uDDFD";
        String greenCheck = "✅";
        //TODO change this to stringBuilder as it is faster and less memory consuming
        String voice = "Mumble required: " + (isVoiceRequired ? blueX : greenCheck) + "\n";
        String region = "Region lock: " + (isRegionLocked ? blueX : greenCheck) + "\n";
        String offclassing = "Offclassing allowed: " + (offclassingAllowed ? blueX : greenCheck) + "\n";
        String balancing = "Balanced lobby: " + (isBalanced ? blueX : greenCheck);
        //String advancedLobby; TODO
        return voice + region + offclassing + balancing;
    }

    private String buildThumbnail(String region) {
        if (region.equals("EU")) {
            return "https://github.com/narcissusTheFlower/tf2c-discord-bot/blob/master/eu_flag.png";
        } else {
            return "https://github.com/narcissusTheFlower/tf2c-discord-bot/blob/master/na_flag.png";
        }
    }


    private EmbedCreateFields.Field[] composeTeams(List<TF2CPlayerSlot> slots) {
        EmbedCreateFields.Field emptySpace = EmbedCreateFields.Field.of("\u200B", "\u200B", false);
        EmbedCreateFields.Field[] lobbyFields = new EmbedCreateFields.Field[25];
        byte teamSize = (byte) slots.size();

        List<TF2CPlayerSlot> blu = assignClasses(slots.subList(0, teamSize / 2)); //First half of the list
        List<TF2CPlayerSlot> red = assignClasses(slots.subList(teamSize / 2, teamSize)); //Second half of the list


        EmbedCreateFields.Field[] teamBlu = new EmbedCreateFields.Field[25];
        short i = -1;
        for (; i < blu.size(); i++) {
            if (i == -1) {
                teamBlu[i + 1] = EmbedCreateFields.Field.of("BLU TEAM", "", false);
                continue;
            }
//            else if(i == blu.size()){
//                lobbyFields[i] = emptySpace;
//                break;
//            }
            teamBlu[i + 1] = EmbedCreateFields.Field.of(blu.get(i).getTf2Class(), blu.get(i).getPlayerName(), false);
        }

        EmbedCreateFields.Field[] teamRed = new EmbedCreateFields.Field[25];
        short k = -1;
        for (; k < red.size(); k++) {
            if (k == -1) {
                teamRed[k + 1] = EmbedCreateFields.Field.of("RED TEAM", "", false);
                continue;
            }
            teamRed[k] = EmbedCreateFields.Field.of(red.get(k).getTf2Class(), red.get(k).getPlayerName(), false);
        }
        return Arrays.copyOf(lobbyFields, 25);
    }

    private List<TF2CPlayerSlot> assignClasses(List<TF2CPlayerSlot> singleTeam) {
        return switch (teamType) {
            case "Highlander" -> {

                Iterator<TF2CPlayerSlot> iterator = singleTeam.iterator();
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
                Iterator<TF2CPlayerSlot> iterator = singleTeam.iterator();
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
                //TODO

                yield null;
            }
            case "Ultiduo" -> {
                //TODO

                yield null;
            }
            case "Bbal" -> {
                //TODO

                yield null;
            }
            default -> throw new IllegalStateException("Failed to determine team type: " + teamType);
        };
    }

    private abstract class TeamType {
        public List<String> classes;
        Iterator<String> iterator;

        public void configure(List<String> classes) {
            this.classes = classes;
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
                    "Scout",
                    "Soldier",
                    "Pyro",
                    "Demo",
                    "Engineer",
                    "Heavy",
                    "Medic",
                    "Sniper",
                    "Spy"
            ));
        }
    }

    private class Sixes extends TeamType {
        public Sixes() {
            super.configure(List.of(
                    "Scout",
                    "Scout",
                    "Roamer",
                    "Pocket",
                    "Demo",
                    "Medic"
            ));
        }
    }

    private class Fours extends TeamType {
        public Fours() {
            super.configure(List.of(
                    "Scout",
                    "Soldier",
                    "Demo",
                    "Medic"
            ));
        }
    }

    private class Ultiduo extends TeamType {
        public Ultiduo() {
            super.configure(List.of(
                    "Soldier",
                    "Medic"
            ));
        }
    }

    private class Bbal extends TeamType {
        public Bbal() {
            super.configure(List.of(
                    "Soldier",
                    "Soldier",
                    "Soldier",
                    "Soldier"
            ));
        }
    }
}
