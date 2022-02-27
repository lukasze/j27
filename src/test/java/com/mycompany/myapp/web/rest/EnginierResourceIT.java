package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Enginier;
import com.mycompany.myapp.repository.EnginierRepository;
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
 * Integration tests for the {@link EnginierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnginierResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/enginiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EnginierRepository enginierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnginierMockMvc;

    private Enginier enginier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enginier createEntity(EntityManager em) {
        Enginier enginier = new Enginier().fullName(DEFAULT_FULL_NAME).mobile(DEFAULT_MOBILE);
        return enginier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enginier createUpdatedEntity(EntityManager em) {
        Enginier enginier = new Enginier().fullName(UPDATED_FULL_NAME).mobile(UPDATED_MOBILE);
        return enginier;
    }

    @BeforeEach
    public void initTest() {
        enginier = createEntity(em);
    }

    @Test
    @Transactional
    void createEnginier() throws Exception {
        int databaseSizeBeforeCreate = enginierRepository.findAll().size();
        // Create the Enginier
        restEnginierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enginier)))
            .andExpect(status().isCreated());

        // Validate the Enginier in the database
        List<Enginier> enginierList = enginierRepository.findAll();
        assertThat(enginierList).hasSize(databaseSizeBeforeCreate + 1);
        Enginier testEnginier = enginierList.get(enginierList.size() - 1);
        assertThat(testEnginier.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testEnginier.getMobile()).isEqualTo(DEFAULT_MOBILE);
    }

    @Test
    @Transactional
    void createEnginierWithExistingId() throws Exception {
        // Create the Enginier with an existing ID
        enginier.setId(1L);

        int databaseSizeBeforeCreate = enginierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnginierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enginier)))
            .andExpect(status().isBadRequest());

        // Validate the Enginier in the database
        List<Enginier> enginierList = enginierRepository.findAll();
        assertThat(enginierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEnginiers() throws Exception {
        // Initialize the database
        enginierRepository.saveAndFlush(enginier);

        // Get all the enginierList
        restEnginierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enginier.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)));
    }

    @Test
    @Transactional
    void getEnginier() throws Exception {
        // Initialize the database
        enginierRepository.saveAndFlush(enginier);

        // Get the enginier
        restEnginierMockMvc
            .perform(get(ENTITY_API_URL_ID, enginier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enginier.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE));
    }

    @Test
    @Transactional
    void getNonExistingEnginier() throws Exception {
        // Get the enginier
        restEnginierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEnginier() throws Exception {
        // Initialize the database
        enginierRepository.saveAndFlush(enginier);

        int databaseSizeBeforeUpdate = enginierRepository.findAll().size();

        // Update the enginier
        Enginier updatedEnginier = enginierRepository.findById(enginier.getId()).get();
        // Disconnect from session so that the updates on updatedEnginier are not directly saved in db
        em.detach(updatedEnginier);
        updatedEnginier.fullName(UPDATED_FULL_NAME).mobile(UPDATED_MOBILE);

        restEnginierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEnginier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEnginier))
            )
            .andExpect(status().isOk());

        // Validate the Enginier in the database
        List<Enginier> enginierList = enginierRepository.findAll();
        assertThat(enginierList).hasSize(databaseSizeBeforeUpdate);
        Enginier testEnginier = enginierList.get(enginierList.size() - 1);
        assertThat(testEnginier.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testEnginier.getMobile()).isEqualTo(UPDATED_MOBILE);
    }

    @Test
    @Transactional
    void putNonExistingEnginier() throws Exception {
        int databaseSizeBeforeUpdate = enginierRepository.findAll().size();
        enginier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnginierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enginier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enginier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enginier in the database
        List<Enginier> enginierList = enginierRepository.findAll();
        assertThat(enginierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnginier() throws Exception {
        int databaseSizeBeforeUpdate = enginierRepository.findAll().size();
        enginier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnginierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enginier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enginier in the database
        List<Enginier> enginierList = enginierRepository.findAll();
        assertThat(enginierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnginier() throws Exception {
        int databaseSizeBeforeUpdate = enginierRepository.findAll().size();
        enginier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnginierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enginier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enginier in the database
        List<Enginier> enginierList = enginierRepository.findAll();
        assertThat(enginierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnginierWithPatch() throws Exception {
        // Initialize the database
        enginierRepository.saveAndFlush(enginier);

        int databaseSizeBeforeUpdate = enginierRepository.findAll().size();

        // Update the enginier using partial update
        Enginier partialUpdatedEnginier = new Enginier();
        partialUpdatedEnginier.setId(enginier.getId());

        partialUpdatedEnginier.fullName(UPDATED_FULL_NAME).mobile(UPDATED_MOBILE);

        restEnginierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnginier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnginier))
            )
            .andExpect(status().isOk());

        // Validate the Enginier in the database
        List<Enginier> enginierList = enginierRepository.findAll();
        assertThat(enginierList).hasSize(databaseSizeBeforeUpdate);
        Enginier testEnginier = enginierList.get(enginierList.size() - 1);
        assertThat(testEnginier.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testEnginier.getMobile()).isEqualTo(UPDATED_MOBILE);
    }

    @Test
    @Transactional
    void fullUpdateEnginierWithPatch() throws Exception {
        // Initialize the database
        enginierRepository.saveAndFlush(enginier);

        int databaseSizeBeforeUpdate = enginierRepository.findAll().size();

        // Update the enginier using partial update
        Enginier partialUpdatedEnginier = new Enginier();
        partialUpdatedEnginier.setId(enginier.getId());

        partialUpdatedEnginier.fullName(UPDATED_FULL_NAME).mobile(UPDATED_MOBILE);

        restEnginierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnginier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnginier))
            )
            .andExpect(status().isOk());

        // Validate the Enginier in the database
        List<Enginier> enginierList = enginierRepository.findAll();
        assertThat(enginierList).hasSize(databaseSizeBeforeUpdate);
        Enginier testEnginier = enginierList.get(enginierList.size() - 1);
        assertThat(testEnginier.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testEnginier.getMobile()).isEqualTo(UPDATED_MOBILE);
    }

    @Test
    @Transactional
    void patchNonExistingEnginier() throws Exception {
        int databaseSizeBeforeUpdate = enginierRepository.findAll().size();
        enginier.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnginierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enginier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enginier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enginier in the database
        List<Enginier> enginierList = enginierRepository.findAll();
        assertThat(enginierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnginier() throws Exception {
        int databaseSizeBeforeUpdate = enginierRepository.findAll().size();
        enginier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnginierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enginier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enginier in the database
        List<Enginier> enginierList = enginierRepository.findAll();
        assertThat(enginierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnginier() throws Exception {
        int databaseSizeBeforeUpdate = enginierRepository.findAll().size();
        enginier.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnginierMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(enginier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enginier in the database
        List<Enginier> enginierList = enginierRepository.findAll();
        assertThat(enginierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnginier() throws Exception {
        // Initialize the database
        enginierRepository.saveAndFlush(enginier);

        int databaseSizeBeforeDelete = enginierRepository.findAll().size();

        // Delete the enginier
        restEnginierMockMvc
            .perform(delete(ENTITY_API_URL_ID, enginier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Enginier> enginierList = enginierRepository.findAll();
        assertThat(enginierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
