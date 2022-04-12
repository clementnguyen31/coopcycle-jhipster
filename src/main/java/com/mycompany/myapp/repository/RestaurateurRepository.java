package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Restaurateur;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Restaurateur entity.
 */
@Repository
public interface RestaurateurRepository extends RestaurateurRepositoryWithBagRelationships, JpaRepository<Restaurateur, Long> {
    default Optional<Restaurateur> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Restaurateur> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Restaurateur> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
