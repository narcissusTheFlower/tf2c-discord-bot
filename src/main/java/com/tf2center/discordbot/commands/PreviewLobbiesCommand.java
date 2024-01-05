package com.tf2center.discordbot.commands;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;

/*
    However, there are some limitations to embeds

    Titles are limited to 256 characters
    Descriptions are limited to 4096 characters
    Each embed can have up to 25 fields
    A field's name is limited to 256 characters and its value to 1024 characters
    The footer is limited to 2048 characters
    The author name is limited to 256 characters
    Up to 10 embeds can be included in a single message
    The total length (of the raw json) of all embeds in a message cannot exceed 6000 characters

    And the biggest thing to remember: Embeds are rendered client-side and as such, may look different screen-to-screen depending on screen size.

    It's always a good idea to plan out and LogicalDeleteParameter how your embeds look on desktop and mobile screens.


Mentions will only render correctly in the description and field values.
Embeds allow markdown. Field values and description support markdown links.

 */
@Component
public class PreviewLobbiesCommand implements SlashCommand {

    private static final Logger logger = LoggerFactory.getLogger(PreviewLobbiesCommand.class);

    private GatewayDiscordClient gatewayDiscordClient;
    private Snowflake snowflake = Snowflake.of(1174055847832334378L);


    @Autowired
    public PreviewLobbiesCommand(GatewayDiscordClient gatewayDiscordClient) {
        this.gatewayDiscordClient = gatewayDiscordClient;
    }

    String test = "\uD83c\uDDFD";
    EmbedCreateSpec sixes = EmbedCreateSpec.builder()
            .color(Color.GREEN)
            .author("Leader: Leroy", "https://tf2center.com/lobbies", "https://wiki.teamfortress.com/w/images/f/f2/Soldierava.jpg")

            .title("Lobby #1252026")
            .url("https://tf2center.com/lobbies")

            .description("Discord/Mumble required: ✅ \n" +
                    "Region lock: "+test+" \n" +
                    "Offclassing: "+test+" \n" +
                    "Balancing: "+test+" ")
            .thumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b7/Flag_of_Europe.svg/800px-Flag_of_Europe.svg.png")

            //.addField("\u200B", "\u200B", false)
            .addField("RED TEAM", "", false)
            .addField("Roamer scout", "[Join](https://tf2center.com/lobbies)", true)
            .addField("Poket scout", "clockwork", true)
            .addField("Roamer soldier ", "blaze", true)
            .addField("Poket soldier ", "rando", true)
            .addField("Demoman", "kaidus", true)
            .addField("Medic", "LOLGUY", true)
            //-----------------------------------------------------
            //.addField("\u200B", "\u200B", false)
            .addField("BLU TEAM", "", false)
            .addField("Roamer scout", "Taken", true)
            .addField("Poket scout", "Taken", true)
            .addField("Roamer soldier ", "Taken", true)
            .addField("Poket soldier ", "Taken", true)
            .addField("Demoman", "[Join](https://tf2center.com/lobbies)", true)
            .addField("Medic", "Taken", true)
            //.addField("\u200B", "\u200B", false)
            .addField("Spectators", "specNam1, specNam2, specNam3...", false)
            //-----------------------------------------------------
            .image("https://wiki.teamfortress.com/w/images/8/82/Cp_process_middle_point.jpeg")
            .timestamp(Instant.now())
            .footer("Lobby opened", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/48/Team_Fortress_2_style_logo.svg/1024px-Team_Fortress_2_style_logo.svg.png")
            .build();

//    EmbedCreateSpec sixes = EmbedCreateSpec.builder()
//            .addField("RED TEAM", "", false)
//            .addField("Roamer scout", "", true)
//            .build();

    @Override
    public String getName() {
        return "lobbies";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

//        gatewayDiscordClient.getChannelById(snowflake)
//                .ofType(GuildMessageChannel.class)
//                .flatMap(channel -> channel.createMessage(MessageCreateSpec.builder()
//
//                                //.addAllEmbeds(List.of(lobbyPreview))
//                        //.addEmbed(test)
//                        //.addEmbed(lobbyPreview)
//
//                                //.embeds(test,test)
//                        .build().withContent("with content")
//                )).subscribe();

//        gatewayDiscordClient.getChannelById(snowflake)
//                .ofType(GuildMessageChannel.class)
//                .flatMap(channel -> channel.getMessageById(Snowflake.of(1187449098387861597L)).block().delete()).subscribe();

//        GuildMessageChannel messageChannel = gatewayDiscordClient.getChannelById(snowflake)
//                .ofType(GuildMessageChannel.class).block();
//        messageChannel.getMessageById(Snowflake.of(1187476687018012773L)).block().edit().withEmbeds(sixes);


// THIS ONE WORKS FOR EDITS AND EVERYTHING ELSE       event.getClient().getChannelById(snowflake).ofType(GuildMessageChannel.class)
//                .flatMap(channel -> channel.getMessageById(Snowflake.of(1187373319884910623L)).block().edit().withContent("another change").withEmbeds(sixes)).subscribe();


        return event.reply()
//                .withEmbeds(
//                        EmbedsPool.retrieveFirst()
//                )
                //.withContent("test")
                .withEphemeral(false);

    }
}
