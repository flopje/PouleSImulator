package com.bonenkamp.PouleSimulator.exceptions;

/**
 * Created by Floris Bonenkamp on 14-7-2015.
 *
 * Custom MatchNotPlayedException
 */
public class MatchNotPlayedException extends Exception {

    // Constructor
    public MatchNotPlayedException() {}

    // Constructor that accepts a message
    public MatchNotPlayedException(String message) {
        super(message);
    }

    // Constructor that accepts a throwable
    public MatchNotPlayedException(Throwable cause) {
        super(cause);
    }

    // Constructor that accepts a message and a throwable
    public MatchNotPlayedException(String message, Throwable cause) {
        super(message, cause);
    }

}
