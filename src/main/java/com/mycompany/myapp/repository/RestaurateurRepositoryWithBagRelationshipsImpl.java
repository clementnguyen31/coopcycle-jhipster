package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Restaurateur;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class RestaurateurRepositoryWithBagRelationshipsImpl implements RestaurateurRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Restaurateur> fetchBagRelationships(Optional<Restaurateur> restaurateur) {
        return restaurateur.map(this::fetchClients);
    }

    @Override
    public Page<Restaurateur> fetchBagRelationships(Page<Restaurateur> restaurateurs) {
        return new PageImpl<>(
            fetchBagRelationships(restaurateurs.getContent()),
            restaurateurs.getPageable(),
            restaurateurs.getTotalElements()
        );
    }

    @Override
    public List<Restaurateur> fetchBagRelationships(List<Restaurateur> restaurateurs) {
        return Optional.of(restaurateurs).map(this::fetchClients).orElse(Collections.emptyList());
    }

    Restaurateur fetchClients(Restaurateur result) {
        return entityManager
            .createQuery(
                "select restaurateur from Restaurateur restaurateur left join fetch restaurateur.clients where restaurateur is :restaurateur",
                Restaurateur.class
            )
            .setParameter("restaurateur", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Restaurateur> fetchClients(List<Restaurateur> restaurateurs) {
        return entityManager
            .createQuery(
                "select distinct restaurateur from Restaurateur restaurateur left join fetch restaurateur.clients where restaurateur in :restaurateurs",
                Restaurateur.class
            )
            .setParameter("restaurateurs", restaurateurs)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
