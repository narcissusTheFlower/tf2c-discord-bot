package com.tf2center.discordbot.publish.publishables;

import com.tf2center.discordbot.publish.Publishable;
import discord4j.common.util.Snowflake;
import reactor.core.publisher.Mono;

//@Component
public class StatsPublishable implements Publishable {
    @Override
    public void publish() {
        System.out.println("test");
    }


    public Mono<Void> extractInformation(String title, Snowflake snowflake) {
        return Mono.empty();
    }


}
