package com.tf2center.discordbot.parser.dto.tf2classes;

public enum SixesClasses {

    SCOUT1("scout"),
    SCOUT2("scout"),
    SOLDIER_ROAMER("soldier_roamer"),
    SOLDIER_POCKET("soldier_pocket"),
    DEMOMAN("demoman"),
    MEDIC("medic");

    private final String value;

    SixesClasses(String value) {
        this.value = value;
    }
}
