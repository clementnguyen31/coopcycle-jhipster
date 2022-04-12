package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Restaurateur;
import com.mycompany.myapp.repository.RestaurateurRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RestaurateurResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RestaurateurResourceIT {

    private static final Long DEFAULT_RESTAURATEUR_ID = 1L;
    private static final Long UPDATED_RESTAURATEUR_ID = 2L;

    private static final String DEFAULT_RESTAURATEUR_NOM = "AAAAAAAAAA";
    private static final String UPDATED_RESTAURATEUR_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_RESTAURATEUR_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_RESTAURATEUR_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_RESTAURATEUR_BOUTIQUE = "AAAAAAAAAA";
    private static final String UPDATED_RESTAURATEUR_BOUTIQUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/restaurateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RestaurateurRepository restaurateurRepository;

    @Mock
    private RestaurateurRepository restaurateurRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRestaurateurMockMvc;

    private Restaurateur restaurateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurateur createEntity(EntityManager em) {
        Restaurateur restaurateur = new Restaurateur()
            .restaurateurId(DEFAULT_RESTAURATEUR_ID)
            .restaurateurNom(DEFAULT_RESTAURATEUR_NOM)
            .restaurateurPrenom(DEFAULT_RESTAURATEUR_PRENOM)
            .restaurateurBoutique(DEFAULT_RESTAURATEUR_BOUTIQUE);
        return restaurateur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Restaurateur createUpdatedEntity(EntityManager em) {
        Restaurateur restaurateur = new Restaurateur()
            .restaurateurId(UPDATED_RESTAURATEUR_ID)
            .restaurateurNom(UPDATED_RESTAURATEUR_NOM)
            .restaurateurPrenom(UPDATED_RESTAURATEUR_PRENOM)
            .restaurateurBoutique(UPDATED_RESTAURATEUR_BOUTIQUE);
        return restaurateur;
    }

    @BeforeEach
    public void initTest() {
        restaurateur = createEntity(em);
    }

    @Test
    @Transactional
    void createRestaurateur() throws Exception {
        int databaseSizeBeforeCreate = restaurateurRepository.findAll().size();
        // Create the Restaurateur
        restRestaurateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurateur)))
            .andExpect(status().isCreated());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeCreate + 1);
        Restaurateur testRestaurateur = restaurateurList.get(restaurateurList.size() - 1);
        assertThat(testRestaurateur.getRestaurateurId()).isEqualTo(DEFAULT_RESTAURATEUR_ID);
        assertThat(testRestaurateur.getRestaurateurNom()).isEqualTo(DEFAULT_RESTAURATEUR_NOM);
        assertThat(testRestaurateur.getRestaurateurPrenom()).isEqualTo(DEFAULT_RESTAURATEUR_PRENOM);
        assertThat(testRestaurateur.getRestaurateurBoutique()).isEqualTo(DEFAULT_RESTAURATEUR_BOUTIQUE);
    }

    @Test
    @Transactional
    void createRestaurateurWithExistingId() throws Exception {
        // Create the Restaurateur with an existing ID
        restaurateur.setId(1L);

        int databaseSizeBeforeCreate = restaurateurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestaurateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurateur)))
            .andExpect(status().isBadRequest());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRestaurateurs() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get all the restaurateurList
        restRestaurateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].restaurateurId").value(hasItem(DEFAULT_RESTAURATEUR_ID.intValue())))
            .andExpect(jsonPath("$.[*].restaurateurNom").value(hasItem(DEFAULT_RESTAURATEUR_NOM)))
            .andExpect(jsonPath("$.[*].restaurateurPrenom").value(hasItem(DEFAULT_RESTAURATEUR_PRENOM)))
            .andExpect(jsonPath("$.[*].restaurateurBoutique").value(hasItem(DEFAULT_RESTAURATEUR_BOUTIQUE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRestaurateursWithEagerRelationshipsIsEnabled() throws Exception {
        when(restaurateurRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRestaurateurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(restaurateurRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRestaurateursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(restaurateurRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRestaurateurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(restaurateurRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getRestaurateur() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        // Get the restaurateur
        restRestaurateurMockMvc
            .perform(get(ENTITY_API_URL_ID, restaurateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(restaurateur.getId().intValue()))
            .andExpect(jsonPath("$.restaurateurId").value(DEFAULT_RESTAURATEUR_ID.intValue()))
            .andExpect(jsonPath("$.restaurateurNom").value(DEFAULT_RESTAURATEUR_NOM))
            .andExpect(jsonPath("$.restaurateurPrenom").value(DEFAULT_RESTAURATEUR_PRENOM))
            .andExpect(jsonPath("$.restaurateurBoutique").value(DEFAULT_RESTAURATEUR_BOUTIQUE));
    }

    @Test
    @Transactional
    void getNonExistingRestaurateur() throws Exception {
        // Get the restaurateur
        restRestaurateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRestaurateur() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        int databaseSizeBeforeUpdate = restaurateurRepository.findAll().size();

        // Update the restaurateur
        Restaurateur updatedRestaurateur = restaurateurRepository.findById(restaurateur.getId()).get();
        // Disconnect from session so that the updates on updatedRestaurateur are not directly saved in db
        em.detach(updatedRestaurateur);
        updatedRestaurateur
            .restaurateurId(UPDATED_RESTAURATEUR_ID)
            .restaurateurNom(UPDATED_RESTAURATEUR_NOM)
            .restaurateurPrenom(UPDATED_RESTAURATEUR_PRENOM)
            .restaurateurBoutique(UPDATED_RESTAURATEUR_BOUTIQUE);

        restRestaurateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRestaurateur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRestaurateur))
            )
            .andExpect(status().isOk());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeUpdate);
        Restaurateur testRestaurateur = restaurateurList.get(restaurateurList.size() - 1);
        assertThat(testRestaurateur.getRestaurateurId()).isEqualTo(UPDATED_RESTAURATEUR_ID);
        assertThat(testRestaurateur.getRestaurateurNom()).isEqualTo(UPDATED_RESTAURATEUR_NOM);
        assertThat(testRestaurateur.getRestaurateurPrenom()).isEqualTo(UPDATED_RESTAURATEUR_PRENOM);
        assertThat(testRestaurateur.getRestaurateurBoutique()).isEqualTo(UPDATED_RESTAURATEUR_BOUTIQUE);
    }

    @Test
    @Transactional
    void putNonExistingRestaurateur() throws Exception {
        int databaseSizeBeforeUpdate = restaurateurRepository.findAll().size();
        restaurateur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, restaurateur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRestaurateur() throws Exception {
        int databaseSizeBeforeUpdate = restaurateurRepository.findAll().size();
        restaurateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(restaurateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRestaurateur() throws Exception {
        int databaseSizeBeforeUpdate = restaurateurRepository.findAll().size();
        restaurateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurateurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(restaurateur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRestaurateurWithPatch() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        int databaseSizeBeforeUpdate = restaurateurRepository.findAll().size();

        // Update the restaurateur using partial update
        Restaurateur partialUpdatedRestaurateur = new Restaurateur();
        partialUpdatedRestaurateur.setId(restaurateur.getId());

        partialUpdatedRestaurateur
            .restaurateurId(UPDATED_RESTAURATEUR_ID)
            .restaurateurPrenom(UPDATED_RESTAURATEUR_PRENOM)
            .restaurateurBoutique(UPDATED_RESTAURATEUR_BOUTIQUE);

        restRestaurateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurateur))
            )
            .andExpect(status().isOk());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeUpdate);
        Restaurateur testRestaurateur = restaurateurList.get(restaurateurList.size() - 1);
        assertThat(testRestaurateur.getRestaurateurId()).isEqualTo(UPDATED_RESTAURATEUR_ID);
        assertThat(testRestaurateur.getRestaurateurNom()).isEqualTo(DEFAULT_RESTAURATEUR_NOM);
        assertThat(testRestaurateur.getRestaurateurPrenom()).isEqualTo(UPDATED_RESTAURATEUR_PRENOM);
        assertThat(testRestaurateur.getRestaurateurBoutique()).isEqualTo(UPDATED_RESTAURATEUR_BOUTIQUE);
    }

    @Test
    @Transactional
    void fullUpdateRestaurateurWithPatch() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        int databaseSizeBeforeUpdate = restaurateurRepository.findAll().size();

        // Update the restaurateur using partial update
        Restaurateur partialUpdatedRestaurateur = new Restaurateur();
        partialUpdatedRestaurateur.setId(restaurateur.getId());

        partialUpdatedRestaurateur
            .restaurateurId(UPDATED_RESTAURATEUR_ID)
            .restaurateurNom(UPDATED_RESTAURATEUR_NOM)
            .restaurateurPrenom(UPDATED_RESTAURATEUR_PRENOM)
            .restaurateurBoutique(UPDATED_RESTAURATEUR_BOUTIQUE);

        restRestaurateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRestaurateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRestaurateur))
            )
            .andExpect(status().isOk());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeUpdate);
        Restaurateur testRestaurateur = restaurateurList.get(restaurateurList.size() - 1);
        assertThat(testRestaurateur.getRestaurateurId()).isEqualTo(UPDATED_RESTAURATEUR_ID);
        assertThat(testRestaurateur.getRestaurateurNom()).isEqualTo(UPDATED_RESTAURATEUR_NOM);
        assertThat(testRestaurateur.getRestaurateurPrenom()).isEqualTo(UPDATED_RESTAURATEUR_PRENOM);
        assertThat(testRestaurateur.getRestaurateurBoutique()).isEqualTo(UPDATED_RESTAURATEUR_BOUTIQUE);
    }

    @Test
    @Transactional
    void patchNonExistingRestaurateur() throws Exception {
        int databaseSizeBeforeUpdate = restaurateurRepository.findAll().size();
        restaurateur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, restaurateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRestaurateur() throws Exception {
        int databaseSizeBeforeUpdate = restaurateurRepository.findAll().size();
        restaurateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(restaurateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRestaurateur() throws Exception {
        int databaseSizeBeforeUpdate = restaurateurRepository.findAll().size();
        restaurateur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRestaurateurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(restaurateur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Restaurateur in the database
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRestaurateur() throws Exception {
        // Initialize the database
        restaurateurRepository.saveAndFlush(restaurateur);

        int databaseSizeBeforeDelete = restaurateurRepository.findAll().size();

        // Delete the restaurateur
        restRestaurateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, restaurateur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Restaurateur> restaurateurList = restaurateurRepository.findAll();
        assertThat(restaurateurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
