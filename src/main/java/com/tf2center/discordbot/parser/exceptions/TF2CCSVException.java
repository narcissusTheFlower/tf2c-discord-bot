package com.tf2center.discordbot.parser.exceptions;

public class TF2CCSVException extends RuntimeException {

    public TF2CCSVException(String message) {
        super(message);
    }

    public TF2CCSVException(String message, Throwable cause) {
        super(message, cause);
    }
}
