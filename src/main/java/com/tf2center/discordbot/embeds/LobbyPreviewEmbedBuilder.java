package com.tf2center.discordbot.embeds;

import com.tf2center.discordbot.dto.json.tf2cpreview.TF2CLobbyPreview;
import com.tf2center.discordbot.dto.json.tf2cpreview.TF2CSlot;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
        //Not using array form the start cos its easier to operate with collections and then transform it to an array
        byte teamSize = (byte) slots.length;

        Team teamsize = switch (teamSize) {
            case 9 -> new Hightlander();
            case 7 -> new Prolander();
            case 6 -> new Sixes();
            case 4 -> {
                boolean demomanPresent = Arrays.stream(slots)
                        .anyMatch(slot -> slot.getTf2Class().contains("demo"));
                if (demomanPresent) {
                    yield new FourVFour();
                } else {
                    yield new Bbal();
                }
            }
            case 3 -> new ThreeVThree();
            case 2 -> new Ultiduo();
            default -> throw new IllegalStateException("Could not define team type: " + teamSize);
        };


        EmbedCreateFields.Field[] fields = {new EmbedCreateFields.Field() {
            @Override
            public String name() {
                return "null";
            }

            @Override
            public String value() {
                return "null";
            }

            @Override
            public boolean inline() {
                return false;
            }
        }};

        return fields;
    }

    private interface Team {
    }

    private class FieldComposer {

        private TF2CLobbyPreview lobby;

        //private EmbedCreateFields.Field teamsHeader;

        private FieldComposer(TF2CLobbyPreview jsonDto) {
            this.lobby = jsonDto;
        }

        private List<EmbedCreateFields.Field> build() {
            return List.of(

            );
        }
    }

    private class Hightlander implements Team {
    }

    private class Prolander implements Team {
    }

    private class Sixes implements Team {
    }

    private class FourVFour implements Team {
    }

    private class Bbal implements Team {
    }

    private class ThreeVThree implements Team {
    }

    private class Ultiduo implements Team {
    }




}
