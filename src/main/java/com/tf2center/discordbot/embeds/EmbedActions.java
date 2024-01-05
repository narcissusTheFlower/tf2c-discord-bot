package com.tf2center.discordbot.embeds;

import discord4j.common.util.Snowflake;
import discord4j.core.spec.EmbedCreateSpec;

public interface EmbedActions {

    void publishEmbed(EmbedCreateSpec embed);

    void deleteEmbed(Snowflake snowflake);

    void editEmbed(EmbedCreateSpec embed, Snowflake snowflake);
}
