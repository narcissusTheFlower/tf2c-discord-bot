package com.tf2center.discordbot.exceptions;

public class TF2CPublishException extends RuntimeException{

    public TF2CPublishException(String message) {
        super(message);
    }

    public TF2CPublishException(String message, Throwable cause) {
        super(message, cause);
    }
}
