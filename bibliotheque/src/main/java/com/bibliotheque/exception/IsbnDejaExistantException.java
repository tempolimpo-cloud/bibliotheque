package com.bibliotheque.exception;

public class IsbnDejaExistantException extends RuntimeException {

    public IsbnDejaExistantException(String isbn) {
        super("Un livre avec l'ISBN " + isbn + " existe déjà.");
    }
}
