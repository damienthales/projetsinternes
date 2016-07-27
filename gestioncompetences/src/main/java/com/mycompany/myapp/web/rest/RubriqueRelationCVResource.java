package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.RubriqueRelationCV;
import com.mycompany.myapp.repository.RubriqueRelationCVRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RubriqueRelationCV.
 */
@RestController
@RequestMapping("/api")
public class RubriqueRelationCVResource {

    private final Logger log = LoggerFactory.getLogger(RubriqueRelationCVResource.class);
        
    @Inject
    private RubriqueRelationCVRepository rubriqueRelationCVRepository;
    
    /**
     * POST  /rubrique-relation-cvs : Create a new rubriqueRelationCV.
     *
     * @param rubriqueRelationCV the rubriqueRelationCV to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rubriqueRelationCV, or with status 400 (Bad Request) if the rubriqueRelationCV has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/rubrique-relation-cvs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RubriqueRelationCV> createRubriqueRelationCV(@RequestBody RubriqueRelationCV rubriqueRelationCV) throws URISyntaxException {
        log.debug("REST request to save RubriqueRelationCV : {}", rubriqueRelationCV);
        if (rubriqueRelationCV.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("rubriqueRelationCV", "idexists", "A new rubriqueRelationCV cannot already have an ID")).body(null);
        }
        RubriqueRelationCV result = rubriqueRelationCVRepository.save(rubriqueRelationCV);
        return ResponseEntity.created(new URI("/api/rubrique-relation-cvs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("rubriqueRelationCV", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rubrique-relation-cvs : Updates an existing rubriqueRelationCV.
     *
     * @param rubriqueRelationCV the rubriqueRelationCV to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rubriqueRelationCV,
     * or with status 400 (Bad Request) if the rubriqueRelationCV is not valid,
     * or with status 500 (Internal Server Error) if the rubriqueRelationCV couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/rubrique-relation-cvs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RubriqueRelationCV> updateRubriqueRelationCV(@RequestBody RubriqueRelationCV rubriqueRelationCV) throws URISyntaxException {
        log.debug("REST request to update RubriqueRelationCV : {}", rubriqueRelationCV);
        if (rubriqueRelationCV.getId() == null) {
            return createRubriqueRelationCV(rubriqueRelationCV);
        }
        RubriqueRelationCV result = rubriqueRelationCVRepository.save(rubriqueRelationCV);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("rubriqueRelationCV", rubriqueRelationCV.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rubrique-relation-cvs : get all the rubriqueRelationCVS.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of rubriqueRelationCVS in body
     */
    @RequestMapping(value = "/rubrique-relation-cvs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RubriqueRelationCV> getAllRubriqueRelationCVS() {
        log.debug("REST request to get all RubriqueRelationCVS");
        List<RubriqueRelationCV> rubriqueRelationCVS = rubriqueRelationCVRepository.findAll();
        return rubriqueRelationCVS;
    }

    /**
     * GET  /rubrique-relation-cvs/:id : get the "id" rubriqueRelationCV.
     *
     * @param id the id of the rubriqueRelationCV to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rubriqueRelationCV, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/rubrique-relation-cvs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RubriqueRelationCV> getRubriqueRelationCV(@PathVariable Long id) {
        log.debug("REST request to get RubriqueRelationCV : {}", id);
        RubriqueRelationCV rubriqueRelationCV = rubriqueRelationCVRepository.findOne(id);
        return Optional.ofNullable(rubriqueRelationCV)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rubrique-relation-cvs/:id : delete the "id" rubriqueRelationCV.
     *
     * @param id the id of the rubriqueRelationCV to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/rubrique-relation-cvs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRubriqueRelationCV(@PathVariable Long id) {
        log.debug("REST request to delete RubriqueRelationCV : {}", id);
        rubriqueRelationCVRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("rubriqueRelationCV", id.toString())).build();
    }

}
