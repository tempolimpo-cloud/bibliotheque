package com.bibliotheque.controller;

import com.bibliotheque.dto.LivreRequest;
import com.bibliotheque.dto.LivreResponse;
import com.bibliotheque.service.LivreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livres")
@RequiredArgsConstructor
public class LivreController {

    private final LivreService livreService;


    // ── Recherche ─────────────────────────────────────────────────────────────
    @GetMapping("/recherche/auteur")
    public ResponseEntity<List<LivreResponse>> rechercherParAuteur(@RequestParam String auteur) {
        return ResponseEntity.ok(livreService.rechercherParAuteur(auteur));
    }
    
    @GetMapping("/recherche/categorie")
    public ResponseEntity<List<LivreResponse>> rechercherParCategorie(@RequestParam String categorie) {
        return ResponseEntity.ok(livreService.rechercherParCategorie(categorie));
    }

    // ── Emprunt & Retour ──────────────────────────────────────────────────────

    @PatchMapping("/{id}/emprunter")
    public ResponseEntity<LivreResponse> emprunterLivre(@PathVariable Long id) {
        return ResponseEntity.ok(livreService.emprunterLivre(id));
    }

    @PatchMapping("/{id}/retourner")
    public ResponseEntity<LivreResponse> retournerLivre(@PathVariable Long id) {
        return ResponseEntity.ok(livreService.retournerLivre(id));
    }
}
