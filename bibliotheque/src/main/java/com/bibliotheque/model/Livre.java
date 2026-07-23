package com.bibliotheque.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "livres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false)
    private String auteur;

    @Column(unique = true, nullable = false)
    private String isbn;

    private String categorie;

    private Integer anneePublication;

    @Column(nullable = false)
    @Builder.Default
    private boolean disponible = true;
}
