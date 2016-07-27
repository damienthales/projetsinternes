package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Competence;
import com.mycompany.myapp.repository.CompetenceRepository;
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
 * REST controller for managing Competence.
 */
@RestController
@RequestMapping("/api")
public class CompetenceResource {

    private final Logger log = LoggerFactory.getLogger(CompetenceResource.class);
        
    @Inject
    private CompetenceRepository competenceRepository;
    
    /**
     * POST  /competences : Create a new competence.
     *
     * @param competence the competence to create
     * @return the ResponseEntity with status 201 (Created) and with body the new competence, or with status 400 (Bad Request) if the competence has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/competences",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Competence> createCompetence(@RequestBody Competence competence) throws URISyntaxException {
        log.debug("REST request to save Competence : {}", competence);
        if (competence.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("competence", "idexists", "A new competence cannot already have an ID")).body(null);
        }
        Competence result = competenceRepository.save(competence);
        return ResponseEntity.created(new URI("/api/competences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("competence", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /competences : Updates an existing competence.
     *
     * @param competence the competence to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated competence,
     * or with status 400 (Bad Request) if the competence is not valid,
     * or with status 500 (Internal Server Error) if the competence couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/competences",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Competence> updateCompetence(@RequestBody Competence competence) throws URISyntaxException {
        log.debug("REST request to update Competence : {}", competence);
        if (competence.getId() == null) {
            return createCompetence(competence);
        }
        Competence result = competenceRepository.save(competence);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("competence", competence.getId().toString()))
            .body(result);
    }

    /**
     * GET  /competences : get all the competences.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of competences in body
     */
    @RequestMapping(value = "/competences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Competence> getAllCompetences() {
        log.debug("REST request to get all Competences");
        List<Competence> competences = competenceRepository.findAll();
        return competences;
    }

    /**
     * GET  /competences/:id : get the "id" competence.
     *
     * @param id the id of the competence to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the competence, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/competences/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Competence> getCompetence(@PathVariable Long id) {
        log.debug("REST request to get Competence : {}", id);
        Competence competence = competenceRepository.findOne(id);
        return Optional.ofNullable(competence)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /competences/:id : delete the "id" competence.
     *
     * @param id the id of the competence to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/competences/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCompetence(@PathVariable Long id) {
        log.debug("REST request to delete Competence : {}", id);
        competenceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("competence", id.toString())).build();
    }

}
