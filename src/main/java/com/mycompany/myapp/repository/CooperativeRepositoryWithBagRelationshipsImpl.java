package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Cooperative;
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
public class CooperativeRepositoryWithBagRelationshipsImpl implements CooperativeRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Cooperative> fetchBagRelationships(Optional<Cooperative> cooperative) {
        return cooperative.map(this::fetchLivreurs).map(this::fetchRestaurateurs).map(this::fetchClients);
    }

    @Override
    public Page<Cooperative> fetchBagRelationships(Page<Cooperative> cooperatives) {
        return new PageImpl<>(
            fetchBagRelationships(cooperatives.getContent()),
            cooperatives.getPageable(),
            cooperatives.getTotalElements()
        );
    }

    @Override
    public List<Cooperative> fetchBagRelationships(List<Cooperative> cooperatives) {
        return Optional
            .of(cooperatives)
            .map(this::fetchLivreurs)
            .map(this::fetchRestaurateurs)
            .map(this::fetchClients)
            .orElse(Collections.emptyList());
    }

    Cooperative fetchLivreurs(Cooperative result) {
        return entityManager
            .createQuery(
                "select cooperative from Cooperative cooperative left join fetch cooperative.livreurs where cooperative is :cooperative",
                Cooperative.class
            )
            .setParameter("cooperative", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Cooperative> fetchLivreurs(List<Cooperative> cooperatives) {
        return entityManager
            .createQuery(
                "select distinct cooperative from Cooperative cooperative left join fetch cooperative.livreurs where cooperative in :cooperatives",
                Cooperative.class
            )
            .setParameter("cooperatives", cooperatives)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    Cooperative fetchRestaurateurs(Cooperative result) {
        return entityManager
            .createQuery(
                "select cooperative from Cooperative cooperative left join fetch cooperative.restaurateurs where cooperative is :cooperative",
                Cooperative.class
            )
            .setParameter("cooperative", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Cooperative> fetchRestaurateurs(List<Cooperative> cooperatives) {
        return entityManager
            .createQuery(
                "select distinct cooperative from Cooperative cooperative left join fetch cooperative.restaurateurs where cooperative in :cooperatives",
                Cooperative.class
            )
            .setParameter("cooperatives", cooperatives)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    Cooperative fetchClients(Cooperative result) {
        return entityManager
            .createQuery(
                "select cooperative from Cooperative cooperative left join fetch cooperative.clients where cooperative is :cooperative",
                Cooperative.class
            )
            .setParameter("cooperative", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Cooperative> fetchClients(List<Cooperative> cooperatives) {
        return entityManager
            .createQuery(
                "select distinct cooperative from Cooperative cooperative left join fetch cooperative.clients where cooperative in :cooperatives",
                Cooperative.class
            )
            .setParameter("cooperatives", cooperatives)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
