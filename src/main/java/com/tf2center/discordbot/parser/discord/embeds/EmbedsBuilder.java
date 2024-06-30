package com.tf2center.discordbot.parser.discord.embeds;

import com.tf2center.discordbot.parser.dto.MainPageObject;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;

import java.util.Collection;
import java.util.Set;

public class EmbedsBuilder {


    private EmbedsBuilder() {
    }

    public static EmbedsBuilder getInstance() {
        return new EmbedsBuilder();
    }

    public Set<EmbedCreateSpec> build(Collection<MainPageObject> lobbies) {
        lobbies.forEach(

        );


        return null;
    }

    private Collection<EmbedCreateSpec> buildLobbies() {
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
            //TODO build proper creation time
            //.timestamp(Instant.now())
            //.footer("Lobby opened", "https://static-00.iconduck.com/assets.00/four-o-clock-emoji-2047x2048-dqpvucft.png")
            .build();
        return null;
    }

    private Collection<EmbedCreateSpec> buildSubs() {

        EmbedCreateSpec test = EmbedCreateSpec.builder()
            .color(Color.of(133, 63, 63)) //From tf2c colour pallet. Redish
            .title("Substitute slots to join.")
            .description(substituteSlots.isEmpty() ? "No substitute slots at this moment" : "")
            .addFields(buildFields())
            //TODO chacge source of the image
            .image("https://raw.githubusercontent.com/narcissusTheFlower/tf2-stats-browser-extension/c13a5552564c58fd06b1dcaa19357b419f4761b9/discord-bot-firestarter.png")
            .build();
        return null;
    }

}
