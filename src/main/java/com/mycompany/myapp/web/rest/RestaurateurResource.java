package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Restaurateur;
import com.mycompany.myapp.repository.RestaurateurRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Restaurateur}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RestaurateurResource {

    private final Logger log = LoggerFactory.getLogger(RestaurateurResource.class);

    private static final String ENTITY_NAME = "restaurateur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RestaurateurRepository restaurateurRepository;

    public RestaurateurResource(RestaurateurRepository restaurateurRepository) {
        this.restaurateurRepository = restaurateurRepository;
    }

    /**
     * {@code POST  /restaurateurs} : Create a new restaurateur.
     *
     * @param restaurateur the restaurateur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new restaurateur, or with status {@code 400 (Bad Request)} if the restaurateur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/restaurateurs")
    public ResponseEntity<Restaurateur> createRestaurateur(@RequestBody Restaurateur restaurateur) throws URISyntaxException {
        log.debug("REST request to save Restaurateur : {}", restaurateur);
        if (restaurateur.getId() != null) {
            throw new BadRequestAlertException("A new restaurateur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Restaurateur result = restaurateurRepository.save(restaurateur);
        return ResponseEntity
            .created(new URI("/api/restaurateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /restaurateurs/:id} : Updates an existing restaurateur.
     *
     * @param id the id of the restaurateur to save.
     * @param restaurateur the restaurateur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurateur,
     * or with status {@code 400 (Bad Request)} if the restaurateur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the restaurateur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/restaurateurs/{id}")
    public ResponseEntity<Restaurateur> updateRestaurateur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Restaurateur restaurateur
    ) throws URISyntaxException {
        log.debug("REST request to update Restaurateur : {}, {}", id, restaurateur);
        if (restaurateur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurateur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Restaurateur result = restaurateurRepository.save(restaurateur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurateur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /restaurateurs/:id} : Partial updates given fields of an existing restaurateur, field will ignore if it is null
     *
     * @param id the id of the restaurateur to save.
     * @param restaurateur the restaurateur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated restaurateur,
     * or with status {@code 400 (Bad Request)} if the restaurateur is not valid,
     * or with status {@code 404 (Not Found)} if the restaurateur is not found,
     * or with status {@code 500 (Internal Server Error)} if the restaurateur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/restaurateurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Restaurateur> partialUpdateRestaurateur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Restaurateur restaurateur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Restaurateur partially : {}, {}", id, restaurateur);
        if (restaurateur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, restaurateur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!restaurateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Restaurateur> result = restaurateurRepository
            .findById(restaurateur.getId())
            .map(existingRestaurateur -> {
                if (restaurateur.getRestaurateurId() != null) {
                    existingRestaurateur.setRestaurateurId(restaurateur.getRestaurateurId());
                }
                if (restaurateur.getRestaurateurNom() != null) {
                    existingRestaurateur.setRestaurateurNom(restaurateur.getRestaurateurNom());
                }
                if (restaurateur.getRestaurateurPrenom() != null) {
                    existingRestaurateur.setRestaurateurPrenom(restaurateur.getRestaurateurPrenom());
                }
                if (restaurateur.getRestaurateurBoutique() != null) {
                    existingRestaurateur.setRestaurateurBoutique(restaurateur.getRestaurateurBoutique());
                }

                return existingRestaurateur;
            })
            .map(restaurateurRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, restaurateur.getId().toString())
        );
    }

    /**
     * {@code GET  /restaurateurs} : get all the restaurateurs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of restaurateurs in body.
     */
    @GetMapping("/restaurateurs")
    public List<Restaurateur> getAllRestaurateurs(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Restaurateurs");
        return restaurateurRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /restaurateurs/:id} : get the "id" restaurateur.
     *
     * @param id the id of the restaurateur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the restaurateur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/restaurateurs/{id}")
    public ResponseEntity<Restaurateur> getRestaurateur(@PathVariable Long id) {
        log.debug("REST request to get Restaurateur : {}", id);
        Optional<Restaurateur> restaurateur = restaurateurRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(restaurateur);
    }

    /**
     * {@code DELETE  /restaurateurs/:id} : delete the "id" restaurateur.
     *
     * @param id the id of the restaurateur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/restaurateurs/{id}")
    public ResponseEntity<Void> deleteRestaurateur(@PathVariable Long id) {
        log.debug("REST request to delete Restaurateur : {}", id);
        restaurateurRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
