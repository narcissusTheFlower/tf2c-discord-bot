package com.tf2center.discordbot.parser.discord.listeners;

import com.tf2center.discordbot.parser.discord.notifications.csv.CSVActions;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Deprecated
public class NotificationsSwitchListener {

    private GatewayDiscordClient client;

    private static final String THIS_BOT_ID = "1254833691965001768";
    @Autowired
    public NotificationsSwitchListener(GatewayDiscordClient client) {
        client.on(MessageCreateEvent.class, this::handle).subscribe();
        this.client = client;
    }

    public Mono<Void> handle(MessageCreateEvent event) {
        if (
            event.getMessage().getContent().toLowerCase().contains("notifs on") &&
                !event.getClient().getSelf().block().getId().equals(THIS_BOT_ID)
        ) {

            CSVActions.appendToSubscribers(
                event.getMessage().getAuthor().get().getId(),
                event.getMessage().getChannelId()
            );

            client.getChannelById(event.getMessage().getChannelId())
                .ofType(MessageChannel.class)
                .flatMap(channel -> channel.createMessage("You are subscribed to notifications ✅"))
                .subscribe();
        } else if (
            event.getMessage().getContent().toLowerCase().contains("notifs off") &&
                !event.getClient().getSelf().block().getId().equals(THIS_BOT_ID)
        ) {
            CSVActions.deleteFromSubscribers(event.getMessage().getAuthor().get().getId());
            client.getChannelById(event.getMessage().getChannelId())
                .ofType(MessageChannel.class)
                .flatMap(channel -> channel.createMessage("You are unsubscribed from notifications ❗"))
                .subscribe();
        }
        return Mono.empty();
    }

}
