package com.bibliotheque.service;

import com.bibliotheque.dto.LivreRequest;
import com.bibliotheque.dto.LivreResponse;

import java.util.List;

public interface LivreService {

    LivreResponse ajouterLivre(LivreRequest request);

    LivreResponse obtenirLivre(Long id);

    List<LivreResponse> obtenirTousLesLivres();

    List<LivreResponse> obtenirLivresDisponibles();

    List<LivreResponse> rechercherParTitre(String titre);

    List<LivreResponse> rechercherParAuteur(String auteur);

    List<LivreResponse> rechercherParCategorie(String categorie);

    LivreResponse modifierLivre(Long id, LivreRequest request);

    void supprimerLivre(Long id);

    LivreResponse emprunterLivre(Long id);

    LivreResponse retournerLivre(Long id);
}
