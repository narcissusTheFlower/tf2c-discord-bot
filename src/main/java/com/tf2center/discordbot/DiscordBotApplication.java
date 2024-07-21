package com.tf2center.discordbot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.rest.RestClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DiscordBotApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(DiscordBotApplication.class)
				.build()
				.run(args);
	}

	@Bean
	public <T extends Event> GatewayDiscordClient gatewayDiscordClient() {
		GatewayDiscordClient client = DiscordClientBuilder.create(System.getenv("DISCORD_TOKEN"))
				.build()
				.login()
				.block();
		return client;
	}

	@Bean
	public RestClient discordRestClient(GatewayDiscordClient client) {
		return client.getRestClient();
	}

}
