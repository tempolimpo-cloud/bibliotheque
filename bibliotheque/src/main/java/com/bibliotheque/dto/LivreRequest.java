package com.bibliotheque.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LivreRequest {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @NotBlank(message = "L'auteur est obligatoire")
    private String auteur;

    @NotBlank(message = "L'ISBN est obligatoire")
    @Pattern(regexp = "^[0-9]{10}([0-9]{3})?$", message = "ISBN invalide (10 ou 13 chiffres)")
    private String isbn;

    private String categorie;

    @NotNull(message = "L'année de publication est obligatoire")
    private Integer anneePublication;
}
