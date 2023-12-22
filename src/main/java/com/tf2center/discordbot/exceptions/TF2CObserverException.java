package com.tf2center.discordbot.exceptions;

public class TF2CObserverException extends RuntimeException {
    public TF2CObserverException(Throwable cause) {
        super(cause);
    }

    public TF2CObserverException(String message, Throwable cause) {
        super(message, cause);
    }
}
