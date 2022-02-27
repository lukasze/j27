package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.UNrepresentative;
import com.mycompany.myapp.repository.UNrepresentativeRepository;
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
 * Integration tests for the {@link UNrepresentativeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UNrepresentativeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/u-nrepresentatives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UNrepresentativeRepository uNrepresentativeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUNrepresentativeMockMvc;

    private UNrepresentative uNrepresentative;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UNrepresentative createEntity(EntityManager em) {
        UNrepresentative uNrepresentative = new UNrepresentative().name(DEFAULT_NAME).gender(DEFAULT_GENDER);
        return uNrepresentative;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UNrepresentative createUpdatedEntity(EntityManager em) {
        UNrepresentative uNrepresentative = new UNrepresentative().name(UPDATED_NAME).gender(UPDATED_GENDER);
        return uNrepresentative;
    }

    @BeforeEach
    public void initTest() {
        uNrepresentative = createEntity(em);
    }

    @Test
    @Transactional
    void createUNrepresentative() throws Exception {
        int databaseSizeBeforeCreate = uNrepresentativeRepository.findAll().size();
        // Create the UNrepresentative
        restUNrepresentativeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uNrepresentative))
            )
            .andExpect(status().isCreated());

        // Validate the UNrepresentative in the database
        List<UNrepresentative> uNrepresentativeList = uNrepresentativeRepository.findAll();
        assertThat(uNrepresentativeList).hasSize(databaseSizeBeforeCreate + 1);
        UNrepresentative testUNrepresentative = uNrepresentativeList.get(uNrepresentativeList.size() - 1);
        assertThat(testUNrepresentative.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testUNrepresentative.getGender()).isEqualTo(DEFAULT_GENDER);
    }

    @Test
    @Transactional
    void createUNrepresentativeWithExistingId() throws Exception {
        // Create the UNrepresentative with an existing ID
        uNrepresentative.setId(1L);

        int databaseSizeBeforeCreate = uNrepresentativeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUNrepresentativeMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uNrepresentative))
            )
            .andExpect(status().isBadRequest());

        // Validate the UNrepresentative in the database
        List<UNrepresentative> uNrepresentativeList = uNrepresentativeRepository.findAll();
        assertThat(uNrepresentativeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUNrepresentatives() throws Exception {
        // Initialize the database
        uNrepresentativeRepository.saveAndFlush(uNrepresentative);

        // Get all the uNrepresentativeList
        restUNrepresentativeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uNrepresentative.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)));
    }

    @Test
    @Transactional
    void getUNrepresentative() throws Exception {
        // Initialize the database
        uNrepresentativeRepository.saveAndFlush(uNrepresentative);

        // Get the uNrepresentative
        restUNrepresentativeMockMvc
            .perform(get(ENTITY_API_URL_ID, uNrepresentative.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uNrepresentative.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER));
    }

    @Test
    @Transactional
    void getNonExistingUNrepresentative() throws Exception {
        // Get the uNrepresentative
        restUNrepresentativeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUNrepresentative() throws Exception {
        // Initialize the database
        uNrepresentativeRepository.saveAndFlush(uNrepresentative);

        int databaseSizeBeforeUpdate = uNrepresentativeRepository.findAll().size();

        // Update the uNrepresentative
        UNrepresentative updatedUNrepresentative = uNrepresentativeRepository.findById(uNrepresentative.getId()).get();
        // Disconnect from session so that the updates on updatedUNrepresentative are not directly saved in db
        em.detach(updatedUNrepresentative);
        updatedUNrepresentative.name(UPDATED_NAME).gender(UPDATED_GENDER);

        restUNrepresentativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUNrepresentative.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUNrepresentative))
            )
            .andExpect(status().isOk());

        // Validate the UNrepresentative in the database
        List<UNrepresentative> uNrepresentativeList = uNrepresentativeRepository.findAll();
        assertThat(uNrepresentativeList).hasSize(databaseSizeBeforeUpdate);
        UNrepresentative testUNrepresentative = uNrepresentativeList.get(uNrepresentativeList.size() - 1);
        assertThat(testUNrepresentative.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUNrepresentative.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    void putNonExistingUNrepresentative() throws Exception {
        int databaseSizeBeforeUpdate = uNrepresentativeRepository.findAll().size();
        uNrepresentative.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUNrepresentativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uNrepresentative.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uNrepresentative))
            )
            .andExpect(status().isBadRequest());

        // Validate the UNrepresentative in the database
        List<UNrepresentative> uNrepresentativeList = uNrepresentativeRepository.findAll();
        assertThat(uNrepresentativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUNrepresentative() throws Exception {
        int databaseSizeBeforeUpdate = uNrepresentativeRepository.findAll().size();
        uNrepresentative.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUNrepresentativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uNrepresentative))
            )
            .andExpect(status().isBadRequest());

        // Validate the UNrepresentative in the database
        List<UNrepresentative> uNrepresentativeList = uNrepresentativeRepository.findAll();
        assertThat(uNrepresentativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUNrepresentative() throws Exception {
        int databaseSizeBeforeUpdate = uNrepresentativeRepository.findAll().size();
        uNrepresentative.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUNrepresentativeMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uNrepresentative))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UNrepresentative in the database
        List<UNrepresentative> uNrepresentativeList = uNrepresentativeRepository.findAll();
        assertThat(uNrepresentativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUNrepresentativeWithPatch() throws Exception {
        // Initialize the database
        uNrepresentativeRepository.saveAndFlush(uNrepresentative);

        int databaseSizeBeforeUpdate = uNrepresentativeRepository.findAll().size();

        // Update the uNrepresentative using partial update
        UNrepresentative partialUpdatedUNrepresentative = new UNrepresentative();
        partialUpdatedUNrepresentative.setId(uNrepresentative.getId());

        partialUpdatedUNrepresentative.name(UPDATED_NAME);

        restUNrepresentativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUNrepresentative.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUNrepresentative))
            )
            .andExpect(status().isOk());

        // Validate the UNrepresentative in the database
        List<UNrepresentative> uNrepresentativeList = uNrepresentativeRepository.findAll();
        assertThat(uNrepresentativeList).hasSize(databaseSizeBeforeUpdate);
        UNrepresentative testUNrepresentative = uNrepresentativeList.get(uNrepresentativeList.size() - 1);
        assertThat(testUNrepresentative.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUNrepresentative.getGender()).isEqualTo(DEFAULT_GENDER);
    }

    @Test
    @Transactional
    void fullUpdateUNrepresentativeWithPatch() throws Exception {
        // Initialize the database
        uNrepresentativeRepository.saveAndFlush(uNrepresentative);

        int databaseSizeBeforeUpdate = uNrepresentativeRepository.findAll().size();

        // Update the uNrepresentative using partial update
        UNrepresentative partialUpdatedUNrepresentative = new UNrepresentative();
        partialUpdatedUNrepresentative.setId(uNrepresentative.getId());

        partialUpdatedUNrepresentative.name(UPDATED_NAME).gender(UPDATED_GENDER);

        restUNrepresentativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUNrepresentative.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUNrepresentative))
            )
            .andExpect(status().isOk());

        // Validate the UNrepresentative in the database
        List<UNrepresentative> uNrepresentativeList = uNrepresentativeRepository.findAll();
        assertThat(uNrepresentativeList).hasSize(databaseSizeBeforeUpdate);
        UNrepresentative testUNrepresentative = uNrepresentativeList.get(uNrepresentativeList.size() - 1);
        assertThat(testUNrepresentative.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testUNrepresentative.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    void patchNonExistingUNrepresentative() throws Exception {
        int databaseSizeBeforeUpdate = uNrepresentativeRepository.findAll().size();
        uNrepresentative.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUNrepresentativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uNrepresentative.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uNrepresentative))
            )
            .andExpect(status().isBadRequest());

        // Validate the UNrepresentative in the database
        List<UNrepresentative> uNrepresentativeList = uNrepresentativeRepository.findAll();
        assertThat(uNrepresentativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUNrepresentative() throws Exception {
        int databaseSizeBeforeUpdate = uNrepresentativeRepository.findAll().size();
        uNrepresentative.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUNrepresentativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uNrepresentative))
            )
            .andExpect(status().isBadRequest());

        // Validate the UNrepresentative in the database
        List<UNrepresentative> uNrepresentativeList = uNrepresentativeRepository.findAll();
        assertThat(uNrepresentativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUNrepresentative() throws Exception {
        int databaseSizeBeforeUpdate = uNrepresentativeRepository.findAll().size();
        uNrepresentative.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUNrepresentativeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uNrepresentative))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UNrepresentative in the database
        List<UNrepresentative> uNrepresentativeList = uNrepresentativeRepository.findAll();
        assertThat(uNrepresentativeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUNrepresentative() throws Exception {
        // Initialize the database
        uNrepresentativeRepository.saveAndFlush(uNrepresentative);

        int databaseSizeBeforeDelete = uNrepresentativeRepository.findAll().size();

        // Delete the uNrepresentative
        restUNrepresentativeMockMvc
            .perform(delete(ENTITY_API_URL_ID, uNrepresentative.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UNrepresentative> uNrepresentativeList = uNrepresentativeRepository.findAll();
        assertThat(uNrepresentativeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
