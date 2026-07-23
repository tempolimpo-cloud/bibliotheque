package com.bibliotheque.repository;

import com.bibliotheque.model.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Long> {

    List<Livre> findByDisponibleTrue();

    List<Livre> findByAuteurContainingIgnoreCase(String auteur);

    List<Livre> findByTitreContainingIgnoreCase(String titre);

    List<Livre> findByCategorieIgnoreCase(String categorie);

    Optional<Livre> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);
}
