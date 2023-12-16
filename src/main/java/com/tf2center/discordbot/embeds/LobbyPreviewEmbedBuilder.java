package com.tf2center.discordbot.embeds;

import com.tf2center.discordbot.dto.json.tf2cpreview.TF2CLobbyPreview;
import com.tf2center.discordbot.dto.json.tf2cpreview.TF2CSlot;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LobbyPreviewEmbedBuilder {

    private final Set<TF2CLobbyPreview> jsonParsedPreviews;

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
            EmbedCreateSpec lobby = EmbedCreateSpec.builder()
                    .color(Color.GREEN)
                    .title("Lobby #" + json.getLobbyId())
                    .url("https://tf2center.com/lobbies/" + json.getLobbyId())
                    .description(
                            buildDescription(
                                    json.isVoiceCommunicationRequired(),
                                    json.isRegionLocked(),
                                    json.isBalancedLobby())
                    )
                    .thumbnail(
                            buildThumbnail(json.getRegion())
                    )
                    .addFields(
                            composeTeams(json.getSlots())
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

    private String buildDescription(boolean isVoiceRequired, boolean isRegionLocked, boolean isBalanced) {
        String blueX = "\uD83c\uDDFD";
        String greenCheck = "✅";

        String voice = "Mumble required: " + (isVoiceRequired ? blueX : greenCheck) + "\n";
        String region = "Region lock: " + (isRegionLocked ? blueX : greenCheck) + "\n";
        //String offclassing = "Offclassing allowed: " + (offclassingAllowed ? blueX : greenCheck) + "\n"; <- TODO
        String balancing = "Balanced lobby: " + (isBalanced ? blueX : greenCheck);

        return voice + region + balancing;
    }

    private String buildThumbnail(String region) {
        if (region.equals("EU")) {
            return "https://github.com/narcissusTheFlower/tf2c-discord-bot/blob/master/eu_flag.png";
        } else {
            return "https://github.com/narcissusTheFlower/tf2c-discord-bot/blob/master/na_flag.png";
        }
    }

    private EmbedCreateFields.Field[] composeTeams(TF2CSlot[] slots) {
        //Try Flux later
        byte teamSize = (byte) slots.length;

        Team teamType = switch (teamSize) {
            case 9 -> new Highlander(slots);
            case 7 -> new Prolander(slots);
            case 6 -> new Sixes(slots);
            case 4 -> {
                boolean demomanPresent = Arrays.stream(slots)
                        .anyMatch(slot -> slot.getTf2Class().contains("demo"));

                if (demomanPresent) {
                    yield new FourVFour(slots);
                }

                yield new Bbal(slots);
            }
            case 3 -> new ThreeVThree(slots);
            case 2 -> new Ultiduo(slots);
            default -> throw new IllegalStateException("Could not define team type: " + teamSize);
        };

        return teamType.assignPlayers();
    }

    private interface Team {
        EmbedCreateFields.Field[] assignPlayers();

        void determineSlotState();
    }

    private class TeamFields {
        private TF2CSlot[] slots;

        public TF2CSlot[] getSlots() {
            return slots;
        }
    }

    private class Highlander extends TeamFields implements Team {

        public Highlander(TF2CSlot[] slots) {
            super.slots = slots;
        }

        @Override
        public EmbedCreateFields.Field[] assignPlayers() {
            //EmbedCreateFields.Field voiceHeader = EmbedCreateFields.Field.of("test", "test", false);
            EmbedCreateFields.Field[] fields = Arrays.stream(super.getSlots())
                    .map(slot -> EmbedCreateFields.Field.of("RED TEAM", "", false))
                    .;


            return new EmbedCreateFields.Field[]{
                    EmbedCreateFields.Field.of("RED TEAM", "", false),
                    EmbedCreateFields.Field.of("Scout", "", true),
                    EmbedCreateFields.Field.of("Scout", "", true),
                    EmbedCreateFields.Field.of("Roamer soldier", "", true),
                    EmbedCreateFields.Field.of("Poket soldier", "", true),
                    EmbedCreateFields.Field.of("Demoman", "", true),
                    EmbedCreateFields.Field.of("Medic", "", true),
                    EmbedCreateFields.Field.of("\u200B", "", false),
                    EmbedCreateFields.Field.of("BLUE TEAM", "", false),
                    EmbedCreateFields.Field.of("Scout", "", true),
                    EmbedCreateFields.Field.of("Scout", "", true),
                    EmbedCreateFields.Field.of("Roamer soldier", "", true),
                    EmbedCreateFields.Field.of("Poket soldier", "", true),
                    EmbedCreateFields.Field.of("Demoman", "", true),
                    EmbedCreateFields.Field.of("Medic", "", true),
                    EmbedCreateFields.Field.of("\u200B", "", false),
            };
        }

        @Override
        public void determineSlotState() {

        }

        public void determineSlotClass(String className) {

        }
    }

    private class Prolander implements Team {

        TF2CSlot[] slots;

        public Prolander(TF2CSlot[] slots) {
            this.slots = slots;
        }

        @Override
        public EmbedCreateFields.Field[] assignPlayers() {
            return new EmbedCreateFields.Field[0];
        }

        @Override
        public void determineSlotState() {

        }
    }

    private class Sixes implements Team {

        TF2CSlot[] slots;

        public Sixes(TF2CSlot[] slots) {
            this.slots = slots;
        }

        @Override
        public EmbedCreateFields.Field[] assignPlayers() {
            return new EmbedCreateFields.Field[0];
        }

        @Override
        public void determineSlotState() {

        }
    }

    private class FourVFour implements Team {
        @Override
        public EmbedCreateFields.Field[] assignPlayers() {
            return new EmbedCreateFields.Field[0];
        }
    }

    private class Bbal implements Team {
        @Override
        public EmbedCreateFields.Field[] assignPlayers() {
            return new EmbedCreateFields.Field[0];
        }
    }

    private class ThreeVThree implements Team {
        @Override
        public EmbedCreateFields.Field[] assignPlayers() {
            return new EmbedCreateFields.Field[0];
        }
    }

    private class Ultiduo implements Team {
        @Override
        public EmbedCreateFields.Field[] assignPlayers() {
            return new EmbedCreateFields.Field[0];
        }
    }




}
