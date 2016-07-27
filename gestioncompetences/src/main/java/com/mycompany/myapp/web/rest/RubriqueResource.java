package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Rubrique;
import com.mycompany.myapp.repository.RubriqueRepository;
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
 * REST controller for managing Rubrique.
 */
@RestController
@RequestMapping("/api")
public class RubriqueResource {

    private final Logger log = LoggerFactory.getLogger(RubriqueResource.class);
        
    @Inject
    private RubriqueRepository rubriqueRepository;
    
    /**
     * POST  /rubriques : Create a new rubrique.
     *
     * @param rubrique the rubrique to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rubrique, or with status 400 (Bad Request) if the rubrique has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/rubriques",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Rubrique> createRubrique(@Valid @RequestBody Rubrique rubrique) throws URISyntaxException {
        log.debug("REST request to save Rubrique : {}", rubrique);
        if (rubrique.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("rubrique", "idexists", "A new rubrique cannot already have an ID")).body(null);
        }
        Rubrique result = rubriqueRepository.save(rubrique);
        return ResponseEntity.created(new URI("/api/rubriques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("rubrique", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rubriques : Updates an existing rubrique.
     *
     * @param rubrique the rubrique to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rubrique,
     * or with status 400 (Bad Request) if the rubrique is not valid,
     * or with status 500 (Internal Server Error) if the rubrique couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/rubriques",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Rubrique> updateRubrique(@Valid @RequestBody Rubrique rubrique) throws URISyntaxException {
        log.debug("REST request to update Rubrique : {}", rubrique);
        if (rubrique.getId() == null) {
            return createRubrique(rubrique);
        }
        Rubrique result = rubriqueRepository.save(rubrique);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("rubrique", rubrique.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rubriques : get all the rubriques.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rubriques in body
     */
    @RequestMapping(value = "/rubriques",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Rubrique> getAllRubriques() {
        log.debug("REST request to get all Rubriques");
        List<Rubrique> rubriques = rubriqueRepository.findAll();
        return rubriques;
    }

    /**
     * GET  /rubriques/:id : get the "id" rubrique.
     *
     * @param id the id of the rubrique to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rubrique, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/rubriques/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Rubrique> getRubrique(@PathVariable Long id) {
        log.debug("REST request to get Rubrique : {}", id);
        Rubrique rubrique = rubriqueRepository.findOne(id);
        return Optional.ofNullable(rubrique)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rubriques/:id : delete the "id" rubrique.
     *
     * @param id the id of the rubrique to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/rubriques/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRubrique(@PathVariable Long id) {
        log.debug("REST request to delete Rubrique : {}", id);
        rubriqueRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("rubrique", id.toString())).build();
    }

}
