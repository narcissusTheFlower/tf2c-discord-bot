package com.tf2center.discordbot.publish;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class LobbyPublishable implements Publishable {

    //    EmbedCreateSpec sixes = EmbedCreateSpec.builder()
//            .color(Color.GREEN)
//            .author("Leader: Leroy", "https://tf2center.com/lobbies", "https://wiki.teamfortress.com/w/images/f/f2/Soldierava.jpg")
//
//            .title("Lobby #1252026")
//            .url("https://tf2center.com/lobbies")
//
//            .description("Discord/Mumble required: ✅ \n" +
//                    "Region lock: da\n" +
//                    "Offclassing:  da\n" +
//                    "Balancing:  net")
//            .thumbnail("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b7/Flag_of_Europe.svg/800px-Flag_of_Europe.svg.png")
//
//            .addField("RED TEAM", "", false)
//            .addField("Roamer scout", "[Join](https://tf2center.com/lobbies)", true)
//            .addField("Poket scout", "clockwork", true)
//            .addField("Roamer soldier ", "blaze", true)
//            .addField("Poket soldier ", "rando", true)
//            .addField("Demoman", "kaidus", true)
//            .addField("Medic", "LOLGUY", true)
//            //-----------------------------------------------------
//            //.addField("\u200B", "\u200B", false)
//            .addField("BLU TEAM", "", false)
//            .addField("Roamer scout", "arek", true)
//            .addField("Poket scout", "sandblast", true)
//            .addField("Roamer soldier ", "marmaloo", true)
//            .addField("Poket soldier ", "b4nny", true)
//            .addField("Demoman", "[Join](https://tf2center.com/lobbies)", true)
//            .addField("Medic", "skeez", true)
//            //.addField("\u200B", "\u200B", false)
//            .addField("Spectators", "specNam1, specNam2, specNam3...", false)
//            //-----------------------------------------------------
//            .image("https://wiki.teamfortress.com/w/images/8/82/Cp_process_middle_point.jpeg")
//            .timestamp(Instant.now())
//            .footer("Lobby opened", "https://upload.wikimedia.org/wikipedia/commons/thumb/4/48/Team_Fortress_2_style_logo.svg/1024px-Team_Fortress_2_style_logo.svg.png")
//            .build();
    private GatewayDiscordClient client;
    private Snowflake channelId;
    private Set<EmbedCreateSpec> embeds;

//    @Scheduled
//    private void getLobbies(){
//        if (!EmbedsPool.getPreviews().isEmpty()){
//            embeds.addAll(EmbedsPool.getPreviews());
//        }
//
//    }

    @Override
    public void publish() {
        client.getChannelById(channelId)
                .ofType(GuildMessageChannel.class)
                .flatMap(channel -> channel.createMessage(MessageCreateSpec.builder()
                        //.addEmbed()
                        .build()
                )).block();
    }


}
