package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Recherche;
import com.mycompany.myapp.repository.RechercheRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RechercheResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RechercheResourceIT {

    private static final Long DEFAULT_RECHERCHE_ID = 1L;
    private static final Long UPDATED_RECHERCHE_ID = 2L;

    private static final String DEFAULT_RECHERCHE_THEMATIQUE = "AAAAAAAAAA";
    private static final String UPDATED_RECHERCHE_THEMATIQUE = "BBBBBBBBBB";

    private static final String DEFAULT_RECHERCHE_GEOGRAPHIQUE = "AAAAAAAAAA";
    private static final String UPDATED_RECHERCHE_GEOGRAPHIQUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/recherches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RechercheRepository rechercheRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRechercheMockMvc;

    private Recherche recherche;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recherche createEntity(EntityManager em) {
        Recherche recherche = new Recherche()
            .rechercheId(DEFAULT_RECHERCHE_ID)
            .rechercheThematique(DEFAULT_RECHERCHE_THEMATIQUE)
            .rechercheGeographique(DEFAULT_RECHERCHE_GEOGRAPHIQUE);
        return recherche;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recherche createUpdatedEntity(EntityManager em) {
        Recherche recherche = new Recherche()
            .rechercheId(UPDATED_RECHERCHE_ID)
            .rechercheThematique(UPDATED_RECHERCHE_THEMATIQUE)
            .rechercheGeographique(UPDATED_RECHERCHE_GEOGRAPHIQUE);
        return recherche;
    }

    @BeforeEach
    public void initTest() {
        recherche = createEntity(em);
    }

    @Test
    @Transactional
    void createRecherche() throws Exception {
        int databaseSizeBeforeCreate = rechercheRepository.findAll().size();
        // Create the Recherche
        restRechercheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recherche)))
            .andExpect(status().isCreated());

        // Validate the Recherche in the database
        List<Recherche> rechercheList = rechercheRepository.findAll();
        assertThat(rechercheList).hasSize(databaseSizeBeforeCreate + 1);
        Recherche testRecherche = rechercheList.get(rechercheList.size() - 1);
        assertThat(testRecherche.getRechercheId()).isEqualTo(DEFAULT_RECHERCHE_ID);
        assertThat(testRecherche.getRechercheThematique()).isEqualTo(DEFAULT_RECHERCHE_THEMATIQUE);
        assertThat(testRecherche.getRechercheGeographique()).isEqualTo(DEFAULT_RECHERCHE_GEOGRAPHIQUE);
    }

    @Test
    @Transactional
    void createRechercheWithExistingId() throws Exception {
        // Create the Recherche with an existing ID
        recherche.setId(1L);

        int databaseSizeBeforeCreate = rechercheRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRechercheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recherche)))
            .andExpect(status().isBadRequest());

        // Validate the Recherche in the database
        List<Recherche> rechercheList = rechercheRepository.findAll();
        assertThat(rechercheList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRecherches() throws Exception {
        // Initialize the database
        rechercheRepository.saveAndFlush(recherche);

        // Get all the rechercheList
        restRechercheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recherche.getId().intValue())))
            .andExpect(jsonPath("$.[*].rechercheId").value(hasItem(DEFAULT_RECHERCHE_ID.intValue())))
            .andExpect(jsonPath("$.[*].rechercheThematique").value(hasItem(DEFAULT_RECHERCHE_THEMATIQUE)))
            .andExpect(jsonPath("$.[*].rechercheGeographique").value(hasItem(DEFAULT_RECHERCHE_GEOGRAPHIQUE)));
    }

    @Test
    @Transactional
    void getRecherche() throws Exception {
        // Initialize the database
        rechercheRepository.saveAndFlush(recherche);

        // Get the recherche
        restRechercheMockMvc
            .perform(get(ENTITY_API_URL_ID, recherche.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recherche.getId().intValue()))
            .andExpect(jsonPath("$.rechercheId").value(DEFAULT_RECHERCHE_ID.intValue()))
            .andExpect(jsonPath("$.rechercheThematique").value(DEFAULT_RECHERCHE_THEMATIQUE))
            .andExpect(jsonPath("$.rechercheGeographique").value(DEFAULT_RECHERCHE_GEOGRAPHIQUE));
    }

    @Test
    @Transactional
    void getNonExistingRecherche() throws Exception {
        // Get the recherche
        restRechercheMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRecherche() throws Exception {
        // Initialize the database
        rechercheRepository.saveAndFlush(recherche);

        int databaseSizeBeforeUpdate = rechercheRepository.findAll().size();

        // Update the recherche
        Recherche updatedRecherche = rechercheRepository.findById(recherche.getId()).get();
        // Disconnect from session so that the updates on updatedRecherche are not directly saved in db
        em.detach(updatedRecherche);
        updatedRecherche
            .rechercheId(UPDATED_RECHERCHE_ID)
            .rechercheThematique(UPDATED_RECHERCHE_THEMATIQUE)
            .rechercheGeographique(UPDATED_RECHERCHE_GEOGRAPHIQUE);

        restRechercheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRecherche.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRecherche))
            )
            .andExpect(status().isOk());

        // Validate the Recherche in the database
        List<Recherche> rechercheList = rechercheRepository.findAll();
        assertThat(rechercheList).hasSize(databaseSizeBeforeUpdate);
        Recherche testRecherche = rechercheList.get(rechercheList.size() - 1);
        assertThat(testRecherche.getRechercheId()).isEqualTo(UPDATED_RECHERCHE_ID);
        assertThat(testRecherche.getRechercheThematique()).isEqualTo(UPDATED_RECHERCHE_THEMATIQUE);
        assertThat(testRecherche.getRechercheGeographique()).isEqualTo(UPDATED_RECHERCHE_GEOGRAPHIQUE);
    }

    @Test
    @Transactional
    void putNonExistingRecherche() throws Exception {
        int databaseSizeBeforeUpdate = rechercheRepository.findAll().size();
        recherche.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRechercheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recherche.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recherche))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recherche in the database
        List<Recherche> rechercheList = rechercheRepository.findAll();
        assertThat(rechercheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecherche() throws Exception {
        int databaseSizeBeforeUpdate = rechercheRepository.findAll().size();
        recherche.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRechercheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recherche))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recherche in the database
        List<Recherche> rechercheList = rechercheRepository.findAll();
        assertThat(rechercheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecherche() throws Exception {
        int databaseSizeBeforeUpdate = rechercheRepository.findAll().size();
        recherche.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRechercheMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(recherche)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recherche in the database
        List<Recherche> rechercheList = rechercheRepository.findAll();
        assertThat(rechercheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRechercheWithPatch() throws Exception {
        // Initialize the database
        rechercheRepository.saveAndFlush(recherche);

        int databaseSizeBeforeUpdate = rechercheRepository.findAll().size();

        // Update the recherche using partial update
        Recherche partialUpdatedRecherche = new Recherche();
        partialUpdatedRecherche.setId(recherche.getId());

        partialUpdatedRecherche
            .rechercheId(UPDATED_RECHERCHE_ID)
            .rechercheThematique(UPDATED_RECHERCHE_THEMATIQUE)
            .rechercheGeographique(UPDATED_RECHERCHE_GEOGRAPHIQUE);

        restRechercheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecherche.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecherche))
            )
            .andExpect(status().isOk());

        // Validate the Recherche in the database
        List<Recherche> rechercheList = rechercheRepository.findAll();
        assertThat(rechercheList).hasSize(databaseSizeBeforeUpdate);
        Recherche testRecherche = rechercheList.get(rechercheList.size() - 1);
        assertThat(testRecherche.getRechercheId()).isEqualTo(UPDATED_RECHERCHE_ID);
        assertThat(testRecherche.getRechercheThematique()).isEqualTo(UPDATED_RECHERCHE_THEMATIQUE);
        assertThat(testRecherche.getRechercheGeographique()).isEqualTo(UPDATED_RECHERCHE_GEOGRAPHIQUE);
    }

    @Test
    @Transactional
    void fullUpdateRechercheWithPatch() throws Exception {
        // Initialize the database
        rechercheRepository.saveAndFlush(recherche);

        int databaseSizeBeforeUpdate = rechercheRepository.findAll().size();

        // Update the recherche using partial update
        Recherche partialUpdatedRecherche = new Recherche();
        partialUpdatedRecherche.setId(recherche.getId());

        partialUpdatedRecherche
            .rechercheId(UPDATED_RECHERCHE_ID)
            .rechercheThematique(UPDATED_RECHERCHE_THEMATIQUE)
            .rechercheGeographique(UPDATED_RECHERCHE_GEOGRAPHIQUE);

        restRechercheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecherche.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecherche))
            )
            .andExpect(status().isOk());

        // Validate the Recherche in the database
        List<Recherche> rechercheList = rechercheRepository.findAll();
        assertThat(rechercheList).hasSize(databaseSizeBeforeUpdate);
        Recherche testRecherche = rechercheList.get(rechercheList.size() - 1);
        assertThat(testRecherche.getRechercheId()).isEqualTo(UPDATED_RECHERCHE_ID);
        assertThat(testRecherche.getRechercheThematique()).isEqualTo(UPDATED_RECHERCHE_THEMATIQUE);
        assertThat(testRecherche.getRechercheGeographique()).isEqualTo(UPDATED_RECHERCHE_GEOGRAPHIQUE);
    }

    @Test
    @Transactional
    void patchNonExistingRecherche() throws Exception {
        int databaseSizeBeforeUpdate = rechercheRepository.findAll().size();
        recherche.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRechercheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recherche.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recherche))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recherche in the database
        List<Recherche> rechercheList = rechercheRepository.findAll();
        assertThat(rechercheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecherche() throws Exception {
        int databaseSizeBeforeUpdate = rechercheRepository.findAll().size();
        recherche.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRechercheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recherche))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recherche in the database
        List<Recherche> rechercheList = rechercheRepository.findAll();
        assertThat(rechercheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecherche() throws Exception {
        int databaseSizeBeforeUpdate = rechercheRepository.findAll().size();
        recherche.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRechercheMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(recherche))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recherche in the database
        List<Recherche> rechercheList = rechercheRepository.findAll();
        assertThat(rechercheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecherche() throws Exception {
        // Initialize the database
        rechercheRepository.saveAndFlush(recherche);

        int databaseSizeBeforeDelete = rechercheRepository.findAll().size();

        // Delete the recherche
        restRechercheMockMvc
            .perform(delete(ENTITY_API_URL_ID, recherche.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Recherche> rechercheList = rechercheRepository.findAll();
        assertThat(rechercheList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
