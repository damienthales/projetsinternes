package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Diplome;
import com.mycompany.myapp.repository.DiplomeRepository;
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
 * REST controller for managing Diplome.
 */
@RestController
@RequestMapping("/api")
public class DiplomeResource {

    private final Logger log = LoggerFactory.getLogger(DiplomeResource.class);
        
    @Inject
    private DiplomeRepository diplomeRepository;
    
    /**
     * POST  /diplomes : Create a new diplome.
     *
     * @param diplome the diplome to create
     * @return the ResponseEntity with status 201 (Created) and with body the new diplome, or with status 400 (Bad Request) if the diplome has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/diplomes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Diplome> createDiplome(@Valid @RequestBody Diplome diplome) throws URISyntaxException {
        log.debug("REST request to save Diplome : {}", diplome);
        if (diplome.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("diplome", "idexists", "A new diplome cannot already have an ID")).body(null);
        }
        Diplome result = diplomeRepository.save(diplome);
        return ResponseEntity.created(new URI("/api/diplomes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("diplome", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /diplomes : Updates an existing diplome.
     *
     * @param diplome the diplome to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated diplome,
     * or with status 400 (Bad Request) if the diplome is not valid,
     * or with status 500 (Internal Server Error) if the diplome couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/diplomes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Diplome> updateDiplome(@Valid @RequestBody Diplome diplome) throws URISyntaxException {
        log.debug("REST request to update Diplome : {}", diplome);
        if (diplome.getId() == null) {
            return createDiplome(diplome);
        }
        Diplome result = diplomeRepository.save(diplome);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("diplome", diplome.getId().toString()))
            .body(result);
    }

    /**
     * GET  /diplomes : get all the diplomes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of diplomes in body
     */
    @RequestMapping(value = "/diplomes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Diplome> getAllDiplomes() {
        log.debug("REST request to get all Diplomes");
        List<Diplome> diplomes = diplomeRepository.findAll();
        return diplomes;
    }

    /**
     * GET  /diplomes/:id : get the "id" diplome.
     *
     * @param id the id of the diplome to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the diplome, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/diplomes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Diplome> getDiplome(@PathVariable Long id) {
        log.debug("REST request to get Diplome : {}", id);
        Diplome diplome = diplomeRepository.findOne(id);
        return Optional.ofNullable(diplome)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /diplomes/:id : delete the "id" diplome.
     *
     * @param id the id of the diplome to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/diplomes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDiplome(@PathVariable Long id) {
        log.debug("REST request to delete Diplome : {}", id);
        diplomeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("diplome", id.toString())).build();
    }

}
