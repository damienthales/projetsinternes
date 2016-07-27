package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.DonneesRubrique;
import com.mycompany.myapp.repository.DonneesRubriqueRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing DonneesRubrique.
 */
@RestController
@RequestMapping("/api")
public class DonneesRubriqueResource {

    private final Logger log = LoggerFactory.getLogger(DonneesRubriqueResource.class);
        
    @Inject
    private DonneesRubriqueRepository donneesRubriqueRepository;
    
    /**
     * POST  /donnees-rubriques : Create a new donneesRubrique.
     *
     * @param donneesRubrique the donneesRubrique to create
     * @return the ResponseEntity with status 201 (Created) and with body the new donneesRubrique, or with status 400 (Bad Request) if the donneesRubrique has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/donnees-rubriques",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DonneesRubrique> createDonneesRubrique(@Valid @RequestBody DonneesRubrique donneesRubrique) throws URISyntaxException {
        log.debug("REST request to save DonneesRubrique : {}", donneesRubrique);
        if (donneesRubrique.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("donneesRubrique", "idexists", "A new donneesRubrique cannot already have an ID")).body(null);
        }
        DonneesRubrique result = donneesRubriqueRepository.save(donneesRubrique);
        return ResponseEntity.created(new URI("/api/donnees-rubriques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("donneesRubrique", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /donnees-rubriques : Updates an existing donneesRubrique.
     *
     * @param donneesRubrique the donneesRubrique to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated donneesRubrique,
     * or with status 400 (Bad Request) if the donneesRubrique is not valid,
     * or with status 500 (Internal Server Error) if the donneesRubrique couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/donnees-rubriques",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DonneesRubrique> updateDonneesRubrique(@Valid @RequestBody DonneesRubrique donneesRubrique) throws URISyntaxException {
        log.debug("REST request to update DonneesRubrique : {}", donneesRubrique);
        if (donneesRubrique.getId() == null) {
            return createDonneesRubrique(donneesRubrique);
        }
        DonneesRubrique result = donneesRubriqueRepository.save(donneesRubrique);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("donneesRubrique", donneesRubrique.getId().toString()))
            .body(result);
    }

    /**
     * GET  /donnees-rubriques : get all the donneesRubriques.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of donneesRubriques in body
     */
    @RequestMapping(value = "/donnees-rubriques",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DonneesRubrique> getAllDonneesRubriques() {
        log.debug("REST request to get all DonneesRubriques");
        List<DonneesRubrique> donneesRubriques = donneesRubriqueRepository.findAll();
        return donneesRubriques;
    }

    /**
     * GET  /donnees-rubriques/:id : get the "id" donneesRubrique.
     *
     * @param id the id of the donneesRubrique to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the donneesRubrique, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/donnees-rubriques/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DonneesRubrique> getDonneesRubrique(@PathVariable Long id) {
        log.debug("REST request to get DonneesRubrique : {}", id);
        DonneesRubrique donneesRubrique = donneesRubriqueRepository.findOne(id);
        return Optional.ofNullable(donneesRubrique)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /donnees-rubriques/:id : delete the "id" donneesRubrique.
     *
     * @param id the id of the donneesRubrique to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/donnees-rubriques/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDonneesRubrique(@PathVariable Long id) {
        log.debug("REST request to delete DonneesRubrique : {}", id);
        donneesRubriqueRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("donneesRubrique", id.toString())).build();
    }

}
