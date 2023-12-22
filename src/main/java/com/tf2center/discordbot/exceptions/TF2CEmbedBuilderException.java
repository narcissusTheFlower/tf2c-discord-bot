package com.tf2center.discordbot.exceptions;

public class TF2CEmbedBuilderException extends RuntimeException {
    public TF2CEmbedBuilderException(String message) {
        super(message);
    }

    public TF2CEmbedBuilderException(Throwable cause) {
        super(cause);
    }

    public TF2CEmbedBuilderException(String message, Throwable cause) {
        super(message, cause);
    }
}
