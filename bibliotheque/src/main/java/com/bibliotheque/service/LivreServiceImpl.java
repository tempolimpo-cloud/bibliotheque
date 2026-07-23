package com.bibliotheque.service;

import com.bibliotheque.dto.LivreRequest;
import com.bibliotheque.dto.LivreResponse;
import com.bibliotheque.exception.IsbnDejaExistantException;
import com.bibliotheque.exception.LivreNotFoundException;
import com.bibliotheque.model.Livre;
import com.bibliotheque.repository.LivreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LivreServiceImpl implements LivreService {

    private final LivreRepository livreRepository;

    @Override
    public LivreResponse ajouterLivre(LivreRequest request) {
        if (livreRepository.existsByIsbn(request.getIsbn())) {
            throw new IsbnDejaExistantException(request.getIsbn());
        }
        Livre livre = Livre.builder()
                .titre(request.getTitre())
                .auteur(request.getAuteur())
                .isbn(request.getIsbn())
                .categorie(request.getCategorie())
                .anneePublication(request.getAnneePublication())
                .disponible(true)
                .build();
        return toResponse(livreRepository.save(livre));
    }
     public LivreResponse ajouterLivre1(LivreRequest request) {
        if (livreRepository.existsByIsbn(request.getIsbn())) {
            throw new IsbnDejaExistantException(request.getIsbn());
        }
        Livre livre = Livre.builder()
                .titre(request.getTitre())
                .auteur(request.getAuteur())
                .isbn(request.getIsbn())
                .categorie(request.getCategorie())
                .anneePublication(request.getAnneePublication())
                .disponible(true)
                .build();
        return toResponse(livreRepository.save(livre));
    }

    @Override
    @Transactional(readOnly = true)
    public LivreResponse obtenirLivre(Long id) {
        return toResponse(findById(id));
    }
     public LivreResponse obtenirLivre2(Long id) {
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivreResponse> obtenirTousLesLivres() {
        return livreRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivreResponse> obtenirLivresDisponibles() {
        return livreRepository.findByDisponibleTrue().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivreResponse> rechercherParTitre(String titre) {
        return livreRepository.findByTitreContainingIgnoreCase(titre).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivreResponse> rechercherParAuteur(String auteur) {
        return livreRepository.findByAuteurContainingIgnoreCase(auteur).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LivreResponse> rechercherParCategorie(String categorie) {
        return livreRepository.findByCategorieIgnoreCase(categorie).stream().map(this::toResponse).toList();
    }

    @Override
    public LivreResponse modifierLivre(Long id, LivreRequest request) {
        Livre livre = findById(id);
        // Vérifie l'ISBN uniquement si celui-ci a changé
        if (!livre.getIsbn().equals(request.getIsbn()) && livreRepository.existsByIsbn(request.getIsbn())) {
            throw new IsbnDejaExistantException(request.getIsbn());
        }
        livre.setTitre(request.getTitre());
        livre.setAuteur(request.getAuteur());
        livre.setIsbn(request.getIsbn());
        livre.setCategorie(request.getCategorie());
        livre.setAnneePublication(request.getAnneePublication());
        return toResponse(livreRepository.save(livre));
    }

    @Override
    public void supprimerLivre(Long id) {
        Livre livre = findById(id);
        livreRepository.delete(livre);
    }

    @Override
    public LivreResponse emprunterLivre(Long id) {
        Livre livre = findById(id);
        if (!livre.isDisponible()) {
            throw new IllegalStateException("Le livre \"" + livre.getTitre() + "\" n'est pas disponible.");
        }
        livre.setDisponible(false);
        return toResponse(livreRepository.save(livre));
    }

    @Override
    public LivreResponse retournerLivre(Long id) {
        Livre livre = findById(id);
        if (livre.isDisponible()) {
            throw new IllegalStateException("Le livre \"" + livre.getTitre() + "\" est déjà disponible.");
        }
        livre.setDisponible(true);
        return toResponse(livreRepository.save(livre));
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Livre findById(Long id) {
        return livreRepository.findById(id).orElseThrow(() -> new LivreNotFoundException(id));
    }

    private LivreResponse toResponse(Livre livre) {
        return LivreResponse.builder()
                .id(livre.getId())
                .titre(livre.getTitre())
                .auteur(livre.getAuteur())
                .isbn(livre.getIsbn())
                .categorie(livre.getCategorie())
                .anneePublication(livre.getAnneePublication())
                .disponible(livre.isDisponible())
                .build();
    }
}
