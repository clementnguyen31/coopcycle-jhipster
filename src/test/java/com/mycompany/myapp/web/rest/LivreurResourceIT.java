package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Livreur;
import com.mycompany.myapp.repository.LivreurRepository;
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
 * Integration tests for the {@link LivreurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LivreurResourceIT {

    private static final Long DEFAULT_LIVREUR_ID = 1L;
    private static final Long UPDATED_LIVREUR_ID = 2L;

    private static final String DEFAULT_LIVREUR_NOM = "AAAAAAAAAA";
    private static final String UPDATED_LIVREUR_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_LIVREUR_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_LIVREUR_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_LIVREUR_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_LIVREUR_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_LIVREUR_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_LIVREUR_ADRESSE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/livreurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLivreurMockMvc;

    private Livreur livreur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livreur createEntity(EntityManager em) {
        Livreur livreur = new Livreur()
            .livreurId(DEFAULT_LIVREUR_ID)
            .livreurNom(DEFAULT_LIVREUR_NOM)
            .livreurPrenom(DEFAULT_LIVREUR_PRENOM)
            .livreurEmail(DEFAULT_LIVREUR_EMAIL)
            .livreurAdresse(DEFAULT_LIVREUR_ADRESSE);
        return livreur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livreur createUpdatedEntity(EntityManager em) {
        Livreur livreur = new Livreur()
            .livreurId(UPDATED_LIVREUR_ID)
            .livreurNom(UPDATED_LIVREUR_NOM)
            .livreurPrenom(UPDATED_LIVREUR_PRENOM)
            .livreurEmail(UPDATED_LIVREUR_EMAIL)
            .livreurAdresse(UPDATED_LIVREUR_ADRESSE);
        return livreur;
    }

    @BeforeEach
    public void initTest() {
        livreur = createEntity(em);
    }

    @Test
    @Transactional
    void createLivreur() throws Exception {
        int databaseSizeBeforeCreate = livreurRepository.findAll().size();
        // Create the Livreur
        restLivreurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livreur)))
            .andExpect(status().isCreated());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeCreate + 1);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getLivreurId()).isEqualTo(DEFAULT_LIVREUR_ID);
        assertThat(testLivreur.getLivreurNom()).isEqualTo(DEFAULT_LIVREUR_NOM);
        assertThat(testLivreur.getLivreurPrenom()).isEqualTo(DEFAULT_LIVREUR_PRENOM);
        assertThat(testLivreur.getLivreurEmail()).isEqualTo(DEFAULT_LIVREUR_EMAIL);
        assertThat(testLivreur.getLivreurAdresse()).isEqualTo(DEFAULT_LIVREUR_ADRESSE);
    }

    @Test
    @Transactional
    void createLivreurWithExistingId() throws Exception {
        // Create the Livreur with an existing ID
        livreur.setId(1L);

        int databaseSizeBeforeCreate = livreurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLivreurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livreur)))
            .andExpect(status().isBadRequest());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLivreurs() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get all the livreurList
        restLivreurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livreur.getId().intValue())))
            .andExpect(jsonPath("$.[*].livreurId").value(hasItem(DEFAULT_LIVREUR_ID.intValue())))
            .andExpect(jsonPath("$.[*].livreurNom").value(hasItem(DEFAULT_LIVREUR_NOM)))
            .andExpect(jsonPath("$.[*].livreurPrenom").value(hasItem(DEFAULT_LIVREUR_PRENOM)))
            .andExpect(jsonPath("$.[*].livreurEmail").value(hasItem(DEFAULT_LIVREUR_EMAIL)))
            .andExpect(jsonPath("$.[*].livreurAdresse").value(hasItem(DEFAULT_LIVREUR_ADRESSE)));
    }

    @Test
    @Transactional
    void getLivreur() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        // Get the livreur
        restLivreurMockMvc
            .perform(get(ENTITY_API_URL_ID, livreur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(livreur.getId().intValue()))
            .andExpect(jsonPath("$.livreurId").value(DEFAULT_LIVREUR_ID.intValue()))
            .andExpect(jsonPath("$.livreurNom").value(DEFAULT_LIVREUR_NOM))
            .andExpect(jsonPath("$.livreurPrenom").value(DEFAULT_LIVREUR_PRENOM))
            .andExpect(jsonPath("$.livreurEmail").value(DEFAULT_LIVREUR_EMAIL))
            .andExpect(jsonPath("$.livreurAdresse").value(DEFAULT_LIVREUR_ADRESSE));
    }

    @Test
    @Transactional
    void getNonExistingLivreur() throws Exception {
        // Get the livreur
        restLivreurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLivreur() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        int databaseSizeBeforeUpdate = livreurRepository.findAll().size();

        // Update the livreur
        Livreur updatedLivreur = livreurRepository.findById(livreur.getId()).get();
        // Disconnect from session so that the updates on updatedLivreur are not directly saved in db
        em.detach(updatedLivreur);
        updatedLivreur
            .livreurId(UPDATED_LIVREUR_ID)
            .livreurNom(UPDATED_LIVREUR_NOM)
            .livreurPrenom(UPDATED_LIVREUR_PRENOM)
            .livreurEmail(UPDATED_LIVREUR_EMAIL)
            .livreurAdresse(UPDATED_LIVREUR_ADRESSE);

        restLivreurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLivreur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLivreur))
            )
            .andExpect(status().isOk());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getLivreurId()).isEqualTo(UPDATED_LIVREUR_ID);
        assertThat(testLivreur.getLivreurNom()).isEqualTo(UPDATED_LIVREUR_NOM);
        assertThat(testLivreur.getLivreurPrenom()).isEqualTo(UPDATED_LIVREUR_PRENOM);
        assertThat(testLivreur.getLivreurEmail()).isEqualTo(UPDATED_LIVREUR_EMAIL);
        assertThat(testLivreur.getLivreurAdresse()).isEqualTo(UPDATED_LIVREUR_ADRESSE);
    }

    @Test
    @Transactional
    void putNonExistingLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().size();
        livreur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivreurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, livreur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(livreur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().size();
        livreur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivreurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(livreur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().size();
        livreur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivreurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(livreur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLivreurWithPatch() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        int databaseSizeBeforeUpdate = livreurRepository.findAll().size();

        // Update the livreur using partial update
        Livreur partialUpdatedLivreur = new Livreur();
        partialUpdatedLivreur.setId(livreur.getId());

        partialUpdatedLivreur.livreurId(UPDATED_LIVREUR_ID).livreurAdresse(UPDATED_LIVREUR_ADRESSE);

        restLivreurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivreur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLivreur))
            )
            .andExpect(status().isOk());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getLivreurId()).isEqualTo(UPDATED_LIVREUR_ID);
        assertThat(testLivreur.getLivreurNom()).isEqualTo(DEFAULT_LIVREUR_NOM);
        assertThat(testLivreur.getLivreurPrenom()).isEqualTo(DEFAULT_LIVREUR_PRENOM);
        assertThat(testLivreur.getLivreurEmail()).isEqualTo(DEFAULT_LIVREUR_EMAIL);
        assertThat(testLivreur.getLivreurAdresse()).isEqualTo(UPDATED_LIVREUR_ADRESSE);
    }

    @Test
    @Transactional
    void fullUpdateLivreurWithPatch() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        int databaseSizeBeforeUpdate = livreurRepository.findAll().size();

        // Update the livreur using partial update
        Livreur partialUpdatedLivreur = new Livreur();
        partialUpdatedLivreur.setId(livreur.getId());

        partialUpdatedLivreur
            .livreurId(UPDATED_LIVREUR_ID)
            .livreurNom(UPDATED_LIVREUR_NOM)
            .livreurPrenom(UPDATED_LIVREUR_PRENOM)
            .livreurEmail(UPDATED_LIVREUR_EMAIL)
            .livreurAdresse(UPDATED_LIVREUR_ADRESSE);

        restLivreurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLivreur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLivreur))
            )
            .andExpect(status().isOk());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
        Livreur testLivreur = livreurList.get(livreurList.size() - 1);
        assertThat(testLivreur.getLivreurId()).isEqualTo(UPDATED_LIVREUR_ID);
        assertThat(testLivreur.getLivreurNom()).isEqualTo(UPDATED_LIVREUR_NOM);
        assertThat(testLivreur.getLivreurPrenom()).isEqualTo(UPDATED_LIVREUR_PRENOM);
        assertThat(testLivreur.getLivreurEmail()).isEqualTo(UPDATED_LIVREUR_EMAIL);
        assertThat(testLivreur.getLivreurAdresse()).isEqualTo(UPDATED_LIVREUR_ADRESSE);
    }

    @Test
    @Transactional
    void patchNonExistingLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().size();
        livreur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivreurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, livreur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(livreur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().size();
        livreur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivreurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(livreur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLivreur() throws Exception {
        int databaseSizeBeforeUpdate = livreurRepository.findAll().size();
        livreur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLivreurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(livreur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Livreur in the database
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLivreur() throws Exception {
        // Initialize the database
        livreurRepository.saveAndFlush(livreur);

        int databaseSizeBeforeDelete = livreurRepository.findAll().size();

        // Delete the livreur
        restLivreurMockMvc
            .perform(delete(ENTITY_API_URL_ID, livreur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Livreur> livreurList = livreurRepository.findAll();
        assertThat(livreurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
