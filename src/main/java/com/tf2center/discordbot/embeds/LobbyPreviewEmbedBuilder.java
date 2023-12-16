package com.tf2center.discordbot.embeds;

import com.tf2center.discordbot.dto.json.tf2cpreview.TF2CLobbyPreview;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;

import java.time.Instant;
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
                            // addLobby(json)
                    )
                    .image(
                            buildImage(json.getMap())
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

    private String buildImage(String map) {
        return "map";
    }

//    private EmbedCreateFields.Field[] addLobby(TF2CLobbyPreview jsonParsedPreviews) {
//        //Try Flux later
//        //Not using array form the start cos its easier to operate with collections and then transform it to an array
//        LinkedList<EmbedCreateFields.Field> lobby =
//                new LinkedList<>()
//                        .addAll(
//                                new FieldComposer(jsonParsedLobby).build()
//                        );
//        return lobby.toArray(new EmbedCreateFields.Field[0]);
//    }

    private class FieldComposer {

        /**
         * This class takes 1 preview object and transforms it into a number of fields that are later added to the main
         * list of fields that compose the whole embed message.<br>
         * So this class is the representation of a "lobby" represented
         * by a number of fields.
         */
        private TF2CLobbyPreview lobby;
        private EmbedCreateFields.Field voiceHeader;
        private EmbedCreateFields.Field regionHeader;
        private EmbedCreateFields.Field offclassingHeader;
        private EmbedCreateFields.Field descriptionHeader;
        private EmbedCreateFields.Field balancingHeader;
        //private EmbedCreateFields.Field teamsHeader;

        private FieldComposer(TF2CLobbyPreview jsonDto) {
            this.lobby = jsonDto;
            voiceHeader = EmbedCreateFields.Field.of(lobby.getMap(), "test", false);
//            composeLobbyField()
//                    .descriptionHeader()
//                    .voiceHeader()
//                    .regionHeader()
//                    .offclassingHeader();
        }

        private List<EmbedCreateFields.Field> build() {
            return List.of(
                    voiceHeader
//                    descriptionHeader,
//                    voiceHeader,
//                    regionHeader,
//                    offclassingHeader,
//                    balancingHeader
            );
        }
    }
}
