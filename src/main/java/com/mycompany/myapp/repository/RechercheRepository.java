package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Recherche;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Recherche entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RechercheRepository extends JpaRepository<Recherche, Long> {}
