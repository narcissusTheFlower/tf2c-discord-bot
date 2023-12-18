package com.tf2center.discordbot.embeds;

import com.tf2center.discordbot.dto.json.tf2cpreview.TF2CLobbyPreview;
import com.tf2center.discordbot.dto.teams.TF2CPlayerSlot;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;

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
        EmbedCreateFields.Field[] lobbyFields = new EmbedCreateFields.Field[0];
        byte teamSize = (byte) slots.size();


        List<TF2CPlayerSlot> blu = assignClasses(slots.subList(0, teamSize / 2)); //First half of the list
        List<TF2CPlayerSlot> red = assignClasses(slots.subList(teamSize / 2, teamSize)); //Second half of the list

        //Но перед этим надо распределить классы
        for (int i = 0; i < slots.size(); i++) {
            //if (первая часть команды){
            //  lobbyFields[i] =  EmbedCreateFields.Field.of(slots.get(i).getClass(), slots.get(i).getPayerName() , false);
            // } else { //вторая часть команды
            //  lobbyFields[i] =  EmbedCreateFields.Field.of(slots.get(i).getClass(), slots.get(i).getPayerName() , false);
            // }
        }
        return lobbyFields;
    }

    private List<TF2CPlayerSlot> assignClasses(List<TF2CPlayerSlot> singleTeam) {
        return switch (teamType) {
            case "Hightlander" -> {

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
//                yield singleTeam.stream().map(player -> {
//
//                    return player;
//                }).toList();
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


                yield null;
            }
            case "Ultiduo" -> {


                yield null;
            }
            case "Bbal" -> {


                yield null;
            }
            default -> throw new IllegalStateException("Failed to determine team type: " + teamType);
        };
    }

    private interface TeamType {
        String assignClass();
    }

    private class Highlander implements TeamType {

        List<String> classes = List.of(
                "Scout",
                "Soldier",
                "Pyro",
                "Demo",
                "Engineer",
                "Heavy",
                "Medic",
                "Sniper",
                "Spy"
        );
        Iterator<String> iterator = classes.listIterator();

//        private final String scout = "Scout";
//        private final String soldier = "Soldier";
//        private final String pyro = "Pyro";
//        private final String demo = "Demo";
//        private final String engi = "Engineer";
//        private final String heavy= "Heavy";
//        private final String medic = "Medic";
//        private final String sniper = "Sniper";
//        private final String spy = "Spy";

        @Override
        public String assignClass() {
            if (iterator.hasNext()) {
                return iterator.next();
            }
            return "eof";
        }
    }

    private class Sixes implements TeamType {

        List<String> classes = List.of(
                "Scout",
                "Scout",
                "Roamer",
                "Pocket",
                "Demo",
                "Medic"
        );
        Iterator<String> iterator = classes.listIterator();

//        private final String scout = "Scout";
//        private final String soldier = "Soldier";
//        private final String pyro = "Pyro";
//        private final String demo = "Demo";
//        private final String engi = "Engineer";
//        private final String heavy= "Heavy";
//        private final String medic = "Medic";
//        private final String sniper = "Sniper";
//        private final String spy = "Spy";

        @Override
        public String assignClass() {
            if (iterator.hasNext()) {
                return iterator.next();
            }
            return "eof";
        }
    }
//    private interface Team {
//        EmbedCreateFields.Field[] assignPlayers();
//
//        String determineSlotState(TF2CSlot slot, String team);
//
//        String determineSlotClass(TF2CSlot slot);
//    }
//
//    private class TeamFields {
//        private TF2CSlot[] slots;
//
//        public TF2CSlot[] getSlots() {
//            return slots;
//        }
//    }
//
//    public class Sixes extends TeamFields implements Team {
//
//        Map<String, String> redTeam =
//                Map.of("Scout1", "[Join](https://tf2center.com/lobbies/" + lobbyId + ")",
//                        "Scout2", "[Join](https://tf2center.com/lobbies/" + lobbyId + ")",
//                        "Roamer", "[Join](https://tf2center.com/lobbies/" + lobbyId + ")",
//                        "Pocket", "[Join](https://tf2center.com/lobbies/" + lobbyId + ")",
//                        "Demo", "[Join](https://tf2center.com/lobbies/" + lobbyId + ")",
//                        "Medic", "[Join](https://tf2center.com/lobbies/" + lobbyId + ")");
//        Map<String, String> bluTeam =
//                Map.of("Scout1", "[Join](https://tf2center.com/lobbies/" + lobbyId + ")",
//                        "Scout2", "[Join](https://tf2center.com/lobbies/" + lobbyId + ")",
//                        "Roamer", "[Join](https://tf2center.com/lobbies/" + lobbyId + ")",
//                        "Pocket", "[Join](https://tf2center.com/lobbies/" + lobbyId + ")",
//                        "Demo", "[Join](https://tf2center.com/lobbies/" + lobbyId + ")",
//                        "Medic", "[Join](https://tf2center.com/lobbies/" + lobbyId + ")");
//
//        public Sixes(TF2CSlot[] slots) {
//            super.slots = slots;
//        }
//
//        @Override
//        public EmbedCreateFields.Field[] assignPlayers() {
//            final String red = "red";
//            final String blu = "blue";
//            //EmbedCreateFields.Field voiceHeader = EmbedCreateFields.Field.of("test", "test", false);
//            List<EmbedCreateFields.Field> fieldsREDTeam = Arrays.stream(super.getSlots())
//                    .map(slot -> EmbedCreateFields.Field.of(
//                            TF2CStringUtils.capitaliseFirstLetter(slot.getTf2Class()),
//                            determineSlotState(slot, red),
//                            false))
//                    .toList();
//
//            List<EmbedCreateFields.Field> fieldsBLUTeam = Arrays.stream(super.getSlots())
//                    .map(slot -> EmbedCreateFields.Field.of(
//                            TF2CStringUtils.capitaliseFirstLetter(slot.getTf2Class()),
//                            determineSlotState(slot, blu),
//                            false))
//                    .toList();
//
//            return new EmbedCreateFields.Field[]{
//                    EmbedCreateFields.Field.of("RED TEAM", "", false),
//                    EmbedCreateFields.Field.of("Scout", "", true),
//                    EmbedCreateFields.Field.of("Scout", "", true),
//                    EmbedCreateFields.Field.of("Roamer soldier", "", true),
//                    EmbedCreateFields.Field.of("Poket soldier", "", true),
//                    EmbedCreateFields.Field.of("Demoman", "", true),
//                    EmbedCreateFields.Field.of("Medic", "", true),
//                    EmbedCreateFields.Field.of("\u200B", "", false),
//                    EmbedCreateFields.Field.of("BLUE TEAM", "", false),
//                    EmbedCreateFields.Field.of("Scout", "", true),
//                    EmbedCreateFields.Field.of("Scout", "", true),
//                    EmbedCreateFields.Field.of("Roamer soldier", "", true),
//                    EmbedCreateFields.Field.of("Poket soldier", "", true),
//                    EmbedCreateFields.Field.of("Demoman", "", true),
//                    EmbedCreateFields.Field.of("Medic", "", true),
//                    EmbedCreateFields.Field.of("\u200B", "", false),
//            };
//        }
//
//        @Override
//        public String determineSlotState(TF2CSlot slot, String team) {
//            boolean slotIsFree = Arrays.stream(slot.getAvailableSlots())
//                    .anyMatch(tf2CAvailableSlot -> tf2CAvailableSlot.getTeam().equals(team));
//            if (slotIsFree) {
//                return "[Join](https://tf2center.com/lobbies/" + lobbyId + ")";
//            }
//            return "Slot if full";
//        }
//
//        @Override
//        public String determineSlotClass(TF2CSlot slot) {
//            //Just determine the class name ie scout = Scout
//            return null;
//        }
//
//
//    }
//
}
