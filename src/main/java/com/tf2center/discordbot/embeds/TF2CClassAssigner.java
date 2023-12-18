package com.tf2center.discordbot.embeds;

import com.tf2center.discordbot.dto.teams.TF2CPlayerSlot;

import java.util.List;

public interface TF2CClassAssigner {

    default List<TF2CPlayerSlot> assignClasses(List<TF2CPlayerSlot> slots, String teamType) {
        return switch (teamType) {
            case "Hightlander" -> {


                yield null;
            }
            case "6v6" -> {


                yield null;
            }
            case "4v4" -> {


                yield null;
            }
            case "Ultiduo" -> {


                yield null;
            }
            case "Bbal" -> {


                yield null;
            }
            default -> throw new IllegalStateException("Failed to determine team type: " + teamType);
        };
    }
}
