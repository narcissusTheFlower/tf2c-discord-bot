package com.tf2center.discordbot;

import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import discord4j.rest.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("singleton")
public class GlobalCommandRegistrar implements ApplicationRunner {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final RestClient client;

    @Autowired
    public GlobalCommandRegistrar(RestClient client) {
        this.client = client;
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {
//        JacksonResources d4jMapper = JacksonResources.create();
//
//        PathMatchingResourcePatternResolver matcher = new PathMatchingResourcePatternResolver();
        ApplicationService applicationService = client.getApplicationService();
        long applicationId = client.getApplicationId().block();

        //Get our commands json from resources as command data
        ApplicationCommandRequest notificationsOff =
            ApplicationCommandRequest.builder()
                .name("notifications_off")
                .description("Turn off lobby notifications.")
                .dmPermission(true)
                .build();

        ApplicationCommandRequest notificationsOn =
            ApplicationCommandRequest.builder()
                .name("notifications_on")
                .description("Turn on lobby notifications.")
                .dmPermission(true)
                .build();

        List<ApplicationCommandRequest> commands = new ArrayList<>();
        commands.add(notificationsOff);
        commands.add(notificationsOn);
//        for (Resource resource : matcher.getResources("commands/*.json")) {
//            ApplicationCommandRequest request = d4jMapper.getObjectMapper()
//                    .readValue(resource.getInputStream(), ApplicationCommandRequest.class);
//            commands.add(request);
//        }
        //User guild commands for dev iteration purposes

//        applicationService.modifyGlobalApplicationCommand()
//        applicationService.bulkOverwriteGlobalApplicationCommand(applicationId,commands)
//            .doOnNext(ignore -> LOGGER.info("Successfully registered Global Commands"))
//            .doOnError(error -> LOGGER.error("Failed to register global commands", error))
//            .subscribe();

//        applicationService.bulkOverwriteGuildApplicationCommand(
//                        applicationId,
//                        Long.parseLong(System.getenv("TF2CENTER_GUILD")),
//                        commands)
//                .doOnNext(ignore -> LOGGER.info("Successfully registered Guild Commands"))
//                .doOnError(error -> LOGGER.error("Failed to register global commands", error))
//                .subscribe();

//        applicationService.bulkOverwriteGlobalApplicationCommand(applicationId, commands)
//            .doOnNext(ignore -> LOGGER.info("Successfully registered Guild Commands"))
//            .doOnError(error -> LOGGER.error("Failed to register global commands", error))
//            .subscribe();


//        applicationService.deleteGuildApplicationCommand(
//                applicationId, Long.parseLong(System.getenv("TF2CENTER_GUILD")),1259151610421055600l
//                )
//            .doOnNext(ignore -> LOGGER.info("Successfully deleted Guild Command"))
//            .doOnError(error -> LOGGER.error("Failed to delete command", error))
//            .subscribe();


//                applicationService.deleteGlobalApplicationCommand(
//                    applicationId, 1265355749127622677l
//                )
//            .doOnNext(ignore -> LOGGER.info("Successfully deleted Guild Command"))
//            .doOnError(error -> LOGGER.error("Failed to delete command", error))
//            .subscribe();
//
//        applicationService.deleteGlobalApplicationCommand(
//                applicationId, 1265391670837645483l
//            )
//            .doOnNext(ignore -> LOGGER.info("Successfully deleted Guild Command"))
//            .doOnError(error -> LOGGER.error("Failed to delete command", error))
//            .subscribe();

    }
}

