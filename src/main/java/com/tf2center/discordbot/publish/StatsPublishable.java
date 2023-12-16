package com.tf2center.discordbot.publish;

import org.springframework.stereotype.Component;

@Component
public class StatsPublishable implements Publishable{
    @Override
    public void publish() {
        System.out.println("test");
    }

}
