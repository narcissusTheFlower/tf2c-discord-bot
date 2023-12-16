package org.tf2center.discordbot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.rest.RestClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class DiscordBotApplication {


	public static void main(String[] args) {
		new SpringApplicationBuilder(DiscordBotApplication.class)
				.build()
				.run(args);
	}


	//public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> eventListeners) {
	@Bean
	public <T extends Event> GatewayDiscordClient gatewayDiscordClient() {
		//Right now the environment variable "DISCORD_TOKEN" is set in Intelij Idea.
		//On the deployment machine will be set with ~/.bashrc (add "export DISCORD_TOKEN=token_from_portal")
		GatewayDiscordClient client = DiscordClientBuilder.create(System.getenv("DISCORD_TOKEN"))
				.build()
				.login()
				.block();

//		for (EventListener<T> listener : eventListeners) {
//			client.on(listener.getEventType())
//					.flatMap(listener::execute)
//					.onErrorResume(listener::handleError)
//					.subscribe();
//		}

		return client;
	}
	@Bean
	public RestClient discordRestClient(GatewayDiscordClient client) {
		return client.getRestClient();
	}
}
