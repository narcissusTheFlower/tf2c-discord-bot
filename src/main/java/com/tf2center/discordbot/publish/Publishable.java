package com.tf2center.discordbot.publish;

/**
 * Class that implements Publishable describes an entity that is posted to chosen Discord text-channels,<br>
 * For example, LobbyPublishable is responsible for updating embeded lobbies in the "lobbies" Discord channel.
 */
public interface Publishable {

    void publish();

}
