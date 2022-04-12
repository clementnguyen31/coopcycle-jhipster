package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Recherche;
import com.mycompany.myapp.repository.RechercheRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Recherche}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RechercheResource {

    private final Logger log = LoggerFactory.getLogger(RechercheResource.class);

    private static final String ENTITY_NAME = "recherche";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RechercheRepository rechercheRepository;

    public RechercheResource(RechercheRepository rechercheRepository) {
        this.rechercheRepository = rechercheRepository;
    }

    /**
     * {@code POST  /recherches} : Create a new recherche.
     *
     * @param recherche the recherche to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recherche, or with status {@code 400 (Bad Request)} if the recherche has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/recherches")
    public ResponseEntity<Recherche> createRecherche(@RequestBody Recherche recherche) throws URISyntaxException {
        log.debug("REST request to save Recherche : {}", recherche);
        if (recherche.getId() != null) {
            throw new BadRequestAlertException("A new recherche cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Recherche result = rechercheRepository.save(recherche);
        return ResponseEntity
            .created(new URI("/api/recherches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /recherches/:id} : Updates an existing recherche.
     *
     * @param id the id of the recherche to save.
     * @param recherche the recherche to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recherche,
     * or with status {@code 400 (Bad Request)} if the recherche is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recherche couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/recherches/{id}")
    public ResponseEntity<Recherche> updateRecherche(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Recherche recherche
    ) throws URISyntaxException {
        log.debug("REST request to update Recherche : {}, {}", id, recherche);
        if (recherche.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recherche.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rechercheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Recherche result = rechercheRepository.save(recherche);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recherche.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /recherches/:id} : Partial updates given fields of an existing recherche, field will ignore if it is null
     *
     * @param id the id of the recherche to save.
     * @param recherche the recherche to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recherche,
     * or with status {@code 400 (Bad Request)} if the recherche is not valid,
     * or with status {@code 404 (Not Found)} if the recherche is not found,
     * or with status {@code 500 (Internal Server Error)} if the recherche couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/recherches/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Recherche> partialUpdateRecherche(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Recherche recherche
    ) throws URISyntaxException {
        log.debug("REST request to partial update Recherche partially : {}, {}", id, recherche);
        if (recherche.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recherche.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rechercheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Recherche> result = rechercheRepository
            .findById(recherche.getId())
            .map(existingRecherche -> {
                if (recherche.getRechercheId() != null) {
                    existingRecherche.setRechercheId(recherche.getRechercheId());
                }
                if (recherche.getRechercheThematique() != null) {
                    existingRecherche.setRechercheThematique(recherche.getRechercheThematique());
                }
                if (recherche.getRechercheGeographique() != null) {
                    existingRecherche.setRechercheGeographique(recherche.getRechercheGeographique());
                }

                return existingRecherche;
            })
            .map(rechercheRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recherche.getId().toString())
        );
    }

    /**
     * {@code GET  /recherches} : get all the recherches.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recherches in body.
     */
    @GetMapping("/recherches")
    public List<Recherche> getAllRecherches() {
        log.debug("REST request to get all Recherches");
        return rechercheRepository.findAll();
    }

    /**
     * {@code GET  /recherches/:id} : get the "id" recherche.
     *
     * @param id the id of the recherche to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recherche, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recherches/{id}")
    public ResponseEntity<Recherche> getRecherche(@PathVariable Long id) {
        log.debug("REST request to get Recherche : {}", id);
        Optional<Recherche> recherche = rechercheRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(recherche);
    }

    /**
     * {@code DELETE  /recherches/:id} : delete the "id" recherche.
     *
     * @param id the id of the recherche to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/recherches/{id}")
    public ResponseEntity<Void> deleteRecherche(@PathVariable Long id) {
        log.debug("REST request to delete Recherche : {}", id);
        rechercheRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
