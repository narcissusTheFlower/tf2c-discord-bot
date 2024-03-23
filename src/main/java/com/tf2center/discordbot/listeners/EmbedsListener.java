package com.tf2center.discordbot.listeners;

import discord4j.core.GatewayDiscordClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Deprecated
public class EmbedsListener {

    @Autowired
    public EmbedsListener(GatewayDiscordClient client) {
        //client.on(MessageCreateEvent.class).flatMap(this::recordNewEmbeds).subscribe();
    }

//    private Mono<Void> recordNewEmbeds(MessageCreateEvent message) {
//        String title;
//        List<Embed> embeds = message.getMessage().getEmbeds();
//        if (!embeds.isEmpty()) {
//            title = embeds.get(0).getTitle().get();
//            Snowflake id = message.getMessage().getId();
//
//            if (!title.toLowerCase().contains("lobby")) {
//                return Mono.just(message.getMessage())
//                        .flatMap(lobbyEmbed -> SubstituteSlotsPublishable.extractInformation(id));
//            }
//
//            return Mono.just(message.getMessage())
//                    .flatMap(lobbyEmbed -> LobbyPublishable.extractInformation(title, id));
//        } else {
//
//            String path = "/home/discord/bots/tf2c-discord-bot/dump";
//            File dump = new File(path);
//            try {
//                FileWriter fileWriter = new FileWriter(dump);
//                fileWriter.write(message.getMessage().toString());
//                fileWriter.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            // todo: if this else is reached the bot will spam the same message. cos for some reason embed mesasges are not recognised as embeds
//
//            System.exit(1);
//            throw new RuntimeException();
//        }
//    }
}
