package com.bibliotheque.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LivreResponse {

    private Long id;
    private String titre;
    private String auteur;
    private String isbn;
    private String categorie;
    private Integer anneePublication;
    private boolean disponible;
}
