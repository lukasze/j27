package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.UNrepresentative;
import com.mycompany.myapp.repository.UNrepresentativeRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.UNrepresentative}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UNrepresentativeResource {

    private final Logger log = LoggerFactory.getLogger(UNrepresentativeResource.class);

    private static final String ENTITY_NAME = "uNrepresentative";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UNrepresentativeRepository uNrepresentativeRepository;

    public UNrepresentativeResource(UNrepresentativeRepository uNrepresentativeRepository) {
        this.uNrepresentativeRepository = uNrepresentativeRepository;
    }

    /**
     * {@code POST  /u-nrepresentatives} : Create a new uNrepresentative.
     *
     * @param uNrepresentative the uNrepresentative to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uNrepresentative, or with status {@code 400 (Bad Request)} if the uNrepresentative has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/u-nrepresentatives")
    public ResponseEntity<UNrepresentative> createUNrepresentative(@RequestBody UNrepresentative uNrepresentative)
        throws URISyntaxException {
        log.debug("REST request to save UNrepresentative : {}", uNrepresentative);
        if (uNrepresentative.getId() != null) {
            throw new BadRequestAlertException("A new uNrepresentative cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UNrepresentative result = uNrepresentativeRepository.save(uNrepresentative);
        return ResponseEntity
            .created(new URI("/api/u-nrepresentatives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /u-nrepresentatives/:id} : Updates an existing uNrepresentative.
     *
     * @param id the id of the uNrepresentative to save.
     * @param uNrepresentative the uNrepresentative to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uNrepresentative,
     * or with status {@code 400 (Bad Request)} if the uNrepresentative is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uNrepresentative couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/u-nrepresentatives/{id}")
    public ResponseEntity<UNrepresentative> updateUNrepresentative(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UNrepresentative uNrepresentative
    ) throws URISyntaxException {
        log.debug("REST request to update UNrepresentative : {}, {}", id, uNrepresentative);
        if (uNrepresentative.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uNrepresentative.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uNrepresentativeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UNrepresentative result = uNrepresentativeRepository.save(uNrepresentative);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uNrepresentative.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /u-nrepresentatives/:id} : Partial updates given fields of an existing uNrepresentative, field will ignore if it is null
     *
     * @param id the id of the uNrepresentative to save.
     * @param uNrepresentative the uNrepresentative to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uNrepresentative,
     * or with status {@code 400 (Bad Request)} if the uNrepresentative is not valid,
     * or with status {@code 404 (Not Found)} if the uNrepresentative is not found,
     * or with status {@code 500 (Internal Server Error)} if the uNrepresentative couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/u-nrepresentatives/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UNrepresentative> partialUpdateUNrepresentative(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UNrepresentative uNrepresentative
    ) throws URISyntaxException {
        log.debug("REST request to partial update UNrepresentative partially : {}, {}", id, uNrepresentative);
        if (uNrepresentative.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uNrepresentative.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uNrepresentativeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UNrepresentative> result = uNrepresentativeRepository
            .findById(uNrepresentative.getId())
            .map(existingUNrepresentative -> {
                if (uNrepresentative.getName() != null) {
                    existingUNrepresentative.setName(uNrepresentative.getName());
                }
                if (uNrepresentative.getGender() != null) {
                    existingUNrepresentative.setGender(uNrepresentative.getGender());
                }

                return existingUNrepresentative;
            })
            .map(uNrepresentativeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uNrepresentative.getId().toString())
        );
    }

    /**
     * {@code GET  /u-nrepresentatives} : get all the uNrepresentatives.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uNrepresentatives in body.
     */
    @GetMapping("/u-nrepresentatives")
    public List<UNrepresentative> getAllUNrepresentatives(@RequestParam(required = false) String filter) {
        if ("country-is-null".equals(filter)) {
            log.debug("REST request to get all UNrepresentatives where country is null");
            return StreamSupport
                .stream(uNrepresentativeRepository.findAll().spliterator(), false)
                .filter(uNrepresentative -> uNrepresentative.getCountry() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all UNrepresentatives");
        return uNrepresentativeRepository.findAll();
    }

    /**
     * {@code GET  /u-nrepresentatives/:id} : get the "id" uNrepresentative.
     *
     * @param id the id of the uNrepresentative to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uNrepresentative, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/u-nrepresentatives/{id}")
    public ResponseEntity<UNrepresentative> getUNrepresentative(@PathVariable Long id) {
        log.debug("REST request to get UNrepresentative : {}", id);
        Optional<UNrepresentative> uNrepresentative = uNrepresentativeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(uNrepresentative);
    }

    /**
     * {@code DELETE  /u-nrepresentatives/:id} : delete the "id" uNrepresentative.
     *
     * @param id the id of the uNrepresentative to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/u-nrepresentatives/{id}")
    public ResponseEntity<Void> deleteUNrepresentative(@PathVariable Long id) {
        log.debug("REST request to delete UNrepresentative : {}", id);
        uNrepresentativeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
