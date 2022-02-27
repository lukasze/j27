package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Enginier;
import com.mycompany.myapp.repository.EnginierRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Enginier}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EnginierResource {

    private final Logger log = LoggerFactory.getLogger(EnginierResource.class);

    private static final String ENTITY_NAME = "enginier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnginierRepository enginierRepository;

    public EnginierResource(EnginierRepository enginierRepository) {
        this.enginierRepository = enginierRepository;
    }

    /**
     * {@code POST  /enginiers} : Create a new enginier.
     *
     * @param enginier the enginier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new enginier, or with status {@code 400 (Bad Request)} if the enginier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/enginiers")
    public ResponseEntity<Enginier> createEnginier(@RequestBody Enginier enginier) throws URISyntaxException {
        log.debug("REST request to save Enginier : {}", enginier);
        if (enginier.getId() != null) {
            throw new BadRequestAlertException("A new enginier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Enginier result = enginierRepository.save(enginier);
        return ResponseEntity
            .created(new URI("/api/enginiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /enginiers/:id} : Updates an existing enginier.
     *
     * @param id the id of the enginier to save.
     * @param enginier the enginier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enginier,
     * or with status {@code 400 (Bad Request)} if the enginier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enginier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/enginiers/{id}")
    public ResponseEntity<Enginier> updateEnginier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Enginier enginier
    ) throws URISyntaxException {
        log.debug("REST request to update Enginier : {}, {}", id, enginier);
        if (enginier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enginier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enginierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Enginier result = enginierRepository.save(enginier);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enginier.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /enginiers/:id} : Partial updates given fields of an existing enginier, field will ignore if it is null
     *
     * @param id the id of the enginier to save.
     * @param enginier the enginier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enginier,
     * or with status {@code 400 (Bad Request)} if the enginier is not valid,
     * or with status {@code 404 (Not Found)} if the enginier is not found,
     * or with status {@code 500 (Internal Server Error)} if the enginier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/enginiers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Enginier> partialUpdateEnginier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Enginier enginier
    ) throws URISyntaxException {
        log.debug("REST request to partial update Enginier partially : {}, {}", id, enginier);
        if (enginier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enginier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enginierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Enginier> result = enginierRepository
            .findById(enginier.getId())
            .map(existingEnginier -> {
                if (enginier.getFullName() != null) {
                    existingEnginier.setFullName(enginier.getFullName());
                }
                if (enginier.getMobile() != null) {
                    existingEnginier.setMobile(enginier.getMobile());
                }

                return existingEnginier;
            })
            .map(enginierRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enginier.getId().toString())
        );
    }

    /**
     * {@code GET  /enginiers} : get all the enginiers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of enginiers in body.
     */
    @GetMapping("/enginiers")
    public List<Enginier> getAllEnginiers() {
        log.debug("REST request to get all Enginiers");
        return enginierRepository.findAll();
    }

    /**
     * {@code GET  /enginiers/:id} : get the "id" enginier.
     *
     * @param id the id of the enginier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the enginier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/enginiers/{id}")
    public ResponseEntity<Enginier> getEnginier(@PathVariable Long id) {
        log.debug("REST request to get Enginier : {}", id);
        Optional<Enginier> enginier = enginierRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(enginier);
    }

    /**
     * {@code DELETE  /enginiers/:id} : delete the "id" enginier.
     *
     * @param id the id of the enginier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/enginiers/{id}")
    public ResponseEntity<Void> deleteEnginier(@PathVariable Long id) {
        log.debug("REST request to delete Enginier : {}", id);
        enginierRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
