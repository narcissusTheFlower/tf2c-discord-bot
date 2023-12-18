package com.tf2center.discordbot.embeds;

import com.tf2center.discordbot.dto.json.tf2cpreview.TF2CLobbyPreview;
import com.tf2center.discordbot.dto.teams.TF2CPlayerSlot;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;

import java.time.Instant;
import java.util.HashSet;
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
                            composeTeams(json.getPlayerSlotList(), teamType)
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

    private EmbedCreateFields.Field[] composeTeams(List<TF2CPlayerSlot> slots, String teamType) {
        byte teamSize = (byte) slots.size();

        List<TF2CPlayerSlot> blu = slots.subList(0, teamSize / 2);
        List<TF2CPlayerSlot> red = slots.subList(teamSize / 2, teamSize);
        return null;
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
//    private class Prolander extends TeamFields implements Team {
//
//        public Prolander(TF2CSlot[] slots) {
//            super.slots = slots;
//        }
//
//
//        @Override
//        public EmbedCreateFields.Field[] assignPlayers() {
//            return new EmbedCreateFields.Field[0];
//        }
//
//        @Override
//        public String determineSlotState(TF2CSlot slot, String team) {
//            return null;
//        }
//
//
//        @Override
//        public String determineSlotClass(TF2CSlot slot) {
//            return null;
//        }
//    }
//
//    private class Highlander extends TeamFields implements Team {
//
//        public Highlander(TF2CSlot[] slots) {
//            super.slots = slots;
//        }
//
//        @Override
//        public EmbedCreateFields.Field[] assignPlayers() {
//            return new EmbedCreateFields.Field[0];
//        }
//
//        @Override
//        public String determineSlotState(TF2CSlot slot, String team) {
//            return null;
//        }
//
//        @Override
//        public String determineSlotClass(TF2CSlot slot) {
//            return null;
//        }
//    }
//
//    private class FourVFour extends TeamFields implements Team {
//
//        public FourVFour(TF2CSlot[] slots) {
//            super.slots = slots;
//        }
//
//        @Override
//        public EmbedCreateFields.Field[] assignPlayers() {
//            return new EmbedCreateFields.Field[0];
//        }
//
//        @Override
//        public String determineSlotState(TF2CSlot slot, String team) {
//            return null;
//        }
//
//        @Override
//        public String determineSlotClass(TF2CSlot slot) {
//            return null;
//        }
//    }
//
//    private class Bbal extends TeamFields implements Team {
//
//        public Bbal(TF2CSlot[] slots) {
//            super.slots = slots;
//        }
//
//        @Override
//        public EmbedCreateFields.Field[] assignPlayers() {
//            return new EmbedCreateFields.Field[0];
//        }
//
//        @Override
//        public String determineSlotState(TF2CSlot slot, String team) {
//            return null;
//        }
//
//        @Override
//        public String determineSlotClass(TF2CSlot slot) {
//            return null;
//        }
//    }
//
//    private class ThreeVThree extends TeamFields implements Team {
//
//        public ThreeVThree(TF2CSlot[] slots) {
//            super.slots = slots;
//        }
//
//        @Override
//        public EmbedCreateFields.Field[] assignPlayers() {
//            return new EmbedCreateFields.Field[0];
//        }
//
//        @Override
//        public String determineSlotState(TF2CSlot slot, String team) {
//            return null;
//        }
//
//        @Override
//        public String determineSlotClass(TF2CSlot slot) {
//            return null;
//        }
//    }
//
//    private class Ultiduo extends TeamFields implements Team {
//
//        public Ultiduo(TF2CSlot[] slots) {
//            super.slots = slots;
//        }
//
//        @Override
//        public EmbedCreateFields.Field[] assignPlayers() {
//            return new EmbedCreateFields.Field[0];
//        }
//
//        @Override
//        public String determineSlotState(TF2CSlot slot, String team) {
//            return null;
//        }
//
//        @Override
//        public String determineSlotClass(TF2CSlot slot) {
//            return null;
//        }
//    }
}
