package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Restaurateur;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface RestaurateurRepositoryWithBagRelationships {
    Optional<Restaurateur> fetchBagRelationships(Optional<Restaurateur> restaurateur);

    List<Restaurateur> fetchBagRelationships(List<Restaurateur> restaurateurs);

    Page<Restaurateur> fetchBagRelationships(Page<Restaurateur> restaurateurs);
}
