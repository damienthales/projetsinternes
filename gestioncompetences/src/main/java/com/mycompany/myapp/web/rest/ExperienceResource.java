package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Experience;
import com.mycompany.myapp.repository.ExperienceRepository;
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
 * REST controller for managing Experience.
 */
@RestController
@RequestMapping("/api")
public class ExperienceResource {

    private final Logger log = LoggerFactory.getLogger(ExperienceResource.class);
        
    @Inject
    private ExperienceRepository experienceRepository;
    
    /**
     * POST  /experiences : Create a new experience.
     *
     * @param experience the experience to create
     * @return the ResponseEntity with status 201 (Created) and with body the new experience, or with status 400 (Bad Request) if the experience has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/experiences",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Experience> createExperience(@Valid @RequestBody Experience experience) throws URISyntaxException {
        log.debug("REST request to save Experience : {}", experience);
        if (experience.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("experience", "idexists", "A new experience cannot already have an ID")).body(null);
        }
        Experience result = experienceRepository.save(experience);
        return ResponseEntity.created(new URI("/api/experiences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("experience", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /experiences : Updates an existing experience.
     *
     * @param experience the experience to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated experience,
     * or with status 400 (Bad Request) if the experience is not valid,
     * or with status 500 (Internal Server Error) if the experience couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/experiences",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Experience> updateExperience(@Valid @RequestBody Experience experience) throws URISyntaxException {
        log.debug("REST request to update Experience : {}", experience);
        if (experience.getId() == null) {
            return createExperience(experience);
        }
        Experience result = experienceRepository.save(experience);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("experience", experience.getId().toString()))
            .body(result);
    }

    /**
     * GET  /experiences : get all the experiences.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of experiences in body
     */
    @RequestMapping(value = "/experiences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Experience> getAllExperiences() {
        log.debug("REST request to get all Experiences");
        List<Experience> experiences = experienceRepository.findAll();
        return experiences;
    }

    /**
     * GET  /experiences/:id : get the "id" experience.
     *
     * @param id the id of the experience to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the experience, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/experiences/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Experience> getExperience(@PathVariable Long id) {
        log.debug("REST request to get Experience : {}", id);
        Experience experience = experienceRepository.findOne(id);
        return Optional.ofNullable(experience)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /experiences/:id : delete the "id" experience.
     *
     * @param id the id of the experience to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/experiences/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        log.debug("REST request to delete Experience : {}", id);
        experienceRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("experience", id.toString())).build();
    }

}
