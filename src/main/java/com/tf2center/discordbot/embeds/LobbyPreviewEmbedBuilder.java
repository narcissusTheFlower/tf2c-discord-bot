package com.tf2center.discordbot.embeds;

import com.tf2center.discordbot.dto.json.tf2cpreview.TF2CLobbyPreview;
import com.tf2center.discordbot.dto.teams.TF2CPlayerSlot;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.apache.commons.lang3.ArrayUtils;

import java.time.Instant;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
                    .url("https://tf2center.com/lobbies/" /*+ json.getLobbyId()*/)
                    .description(
                            buildDescription(
                                    json.isVoiceCommunicationRequired(),
                                    json.isRegionLocked(),
                                    json.isBalancedLobby(),
                                    json.isOffclassingAllowed(),
                                    teamType)
                    )
                    .thumbnail(
                            buildThumbnail(json.getRegion())
                    )
                    .addFields(
                            composeTeams(json.getPlayerSlotList())
                    )
//                    .image(
//                            "https://tf2center.com" + json.getMap()
//                    )
                    .timestamp(Instant.now())
                    //.footer("Lobby opened", "🕖")
                    .build();

            result.add(lobby);
        });

        return result;
    }

    private String buildDescription(boolean isVoiceRequired, boolean isRegionLocked, boolean isBalanced, boolean offclassingAllowed, String teamType) {
        String blueX = "\uD83c\uDDFD";
        String greenCheck = "✅";
        //TODO change this to stringBuilder as it is faster and less memory consuming
        String voice = "Mumble required: " + (isVoiceRequired ? greenCheck : blueX) + "\n";
        String region = "Region lock: " + (isRegionLocked ? greenCheck : blueX) + "\n";
        String balancing = "Balanced lobby: " + (isBalanced ? greenCheck : blueX);
        if (teamType.equals("6v6")) {
            String offclassing = "Offclassing allowed: " + (offclassingAllowed ? greenCheck : blueX) + "\n";
            return voice + region + offclassing + balancing;
        }
        //String advancedLobby; TODO
        return voice + region + balancing;
    }

    private String buildThumbnail(String region) {
        //TODO change resource origins and fix their sizes to be the same dimensions
        if (region.equals("EU")) {
            return "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b7/Flag_of_Europe.svg/1280px-Flag_of_Europe.svg.png";
        } else {
            return "https://upload.wikimedia.org/wikipedia/commons/thumb/8/80/Flag_of_the_United_States_%281877%E2%80%931890%29.svg/2560px-Flag_of_the_United_States_%281877%E2%80%931890%29.svg.png";
        }
    }


    private EmbedCreateFields.Field[] composeTeams(List<TF2CPlayerSlot> slots) {
        byte teamSize = (byte) slots.size();

        List<TF2CPlayerSlot> blu = assignClasses(slots.subList(0, teamSize / 2)); //First half of the list
        List<TF2CPlayerSlot> red = assignClasses(slots.subList(teamSize / 2, teamSize)); //Second half of the list

        EmbedCreateFields.Field[] teamBlu = new EmbedCreateFields.Field[(teamSize / 2) + 2];
        teamBlu[0] = EmbedCreateFields.Field.of("BLU TEAM", "", false);
        for (int i = 1; i < teamBlu.length; i++) {
            if (i == teamBlu.length - 1) {
                teamBlu[i] = EmbedCreateFields.Field.of("\u200B", "\u200B", false);
                break;
            }
            teamBlu[i] = EmbedCreateFields.Field.of(
                    blu.get(i - 1).getTf2Class(),
                    blu.get(i - 1).getPlayerName(),
                    true);
        }

        EmbedCreateFields.Field[] teamRed = new EmbedCreateFields.Field[(teamSize / 2) + 1];
        teamRed[0] = EmbedCreateFields.Field.of("RED TEAM", "", false);
        for (int i = 1; i < teamRed.length; i++) {
            teamRed[i] = EmbedCreateFields.Field.of(
                    red.get(i - 1).getTf2Class(),
                    red.get(i - 1).getPlayerName(),
                    true);
        }
        return ArrayUtils.addAll(teamBlu, teamRed);
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
                Iterator<TF2CPlayerSlot> iterator = singleTeam.iterator();
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
                Iterator<TF2CPlayerSlot> iterator = singleTeam.iterator();
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
            case "Bbal" -> {
                Iterator<TF2CPlayerSlot> iterator = singleTeam.iterator();
                Bbal bbal = new Bbal();
                for (int i = 0; i < singleTeam.size(); i++) {
                    if (iterator.hasNext()) {
                        singleTeam.get(i).setTf2Class(
                                bbal.assignClass()
                        );
                    }
                }
                yield List.copyOf(singleTeam);
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
