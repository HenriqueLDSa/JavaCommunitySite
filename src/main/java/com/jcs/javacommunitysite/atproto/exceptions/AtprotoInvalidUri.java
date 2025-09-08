package com.jcs.javacommunitysite.atproto.exceptions;

public class AtprotoInvalidUri extends RuntimeException {
    public AtprotoInvalidUri(String message) {
        super(message);
    }
    public AtprotoInvalidUri() { super(); }
}
