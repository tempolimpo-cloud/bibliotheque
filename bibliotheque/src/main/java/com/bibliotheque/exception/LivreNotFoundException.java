package com.bibliotheque.exception;

public class LivreNotFoundException extends RuntimeException {

    public LivreNotFoundException(Long id) {
        super("Livre introuvable avec l'id : " + id);
    }

    public LivreNotFoundException(String isbn) {
        super("Livre introuvable avec l'ISBN : " + isbn);
    }
}
