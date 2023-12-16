package org.tf2center.discordbot.publish;

import org.springframework.stereotype.Component;

@Component
public class LobbyPublishable implements Publishable {

    //private embedLobbies

    //    default void publish(GatewayDiscordClient client, Snowflake id, EmbedCreateSpec embed){
//        client.getChannelById(id)
//                .ofType(GuildMessageChannel.class)
//                .flatMap(channel -> channel.createMessage(MessageCreateSpec.builder()
//                        .addEmbed(embed)
//                        .build()
//                )).block();
//    };
    @Override
    public void publish() {
        System.out.println("test");
//        client.getChannelById(id)
//                .ofType(GuildMessageChannel.class)
//                .flatMap(channel -> channel.createMessage(MessageCreateSpec.builder()
//                        .addEmbed(embed)
//                        .build()
//                )).block();
    }


}
