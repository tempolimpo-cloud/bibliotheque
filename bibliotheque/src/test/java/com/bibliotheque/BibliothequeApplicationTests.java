package com.bibliotheque;

import com.bibliotheque.dto.LivreRequest;
import com.bibliotheque.dto.LivreResponse;
import com.bibliotheque.exception.LivreNotFoundException;
import com.bibliotheque.service.LivreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class BibliothequeApplicationTests {

    @Autowired
    private LivreService livreService;

    private LivreRequest buildRequest(String isbn) {
        LivreRequest req = new LivreRequest();
        req.setTitre("Les Misérables");
        req.setAuteur("Victor Hugo");
        req.setIsbn(isbn);
        req.setCategorie("Roman");
        req.setAnneePublication(1862);
        return req;
    }

    @Test
    void contextLoads() {
        assertThat(livreService).isNotNull();
    }

    @Test
    void ajouterEtObtenirLivre() {
        LivreResponse response = livreService.ajouterLivre(buildRequest("9782070408504"));
        assertThat(response.getId()).isNotNull();
        assertThat(response.getTitre()).isEqualTo("Les Misérables");
        assertThat(response.isDisponible()).isTrue();
    }

    @Test
    void obtenirLivreInexistantLanceException() {
        assertThatThrownBy(() -> livreService.obtenirLivre(999L))
                .isInstanceOf(LivreNotFoundException.class);
    }

    @Test
    void emprunterEtRetournerLivre() {
        LivreResponse livre = livreService.ajouterLivre(buildRequest("9782070408504"));

        LivreResponse emprunte = livreService.emprunterLivre(livre.getId());
        assertThat(emprunte.isDisponible()).isFalse();

        LivreResponse retourne = livreService.retournerLivre(livre.getId());
        assertThat(retourne.isDisponible()).isTrue();
    }

    @Test
    void rechercherParAuteur() {
        livreService.ajouterLivre(buildRequest("9782070408504"));
        List<LivreResponse> resultats = livreService.rechercherParAuteur("hugo");
        assertThat(resultats).isNotEmpty();
    }

    @Test
    void supprimerLivre() {
        LivreResponse livre = livreService.ajouterLivre(buildRequest("9782070408504"));
        livreService.supprimerLivre(livre.getId());
        assertThatThrownBy(() -> livreService.obtenirLivre(livre.getId()))
                .isInstanceOf(LivreNotFoundException.class);
    }
}
