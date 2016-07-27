package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Telephone;
import com.mycompany.myapp.repository.TelephoneRepository;
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
 * REST controller for managing Telephone.
 */
@RestController
@RequestMapping("/api")
public class TelephoneResource {

    private final Logger log = LoggerFactory.getLogger(TelephoneResource.class);
        
    @Inject
    private TelephoneRepository telephoneRepository;
    
    /**
     * POST  /telephones : Create a new telephone.
     *
     * @param telephone the telephone to create
     * @return the ResponseEntity with status 201 (Created) and with body the new telephone, or with status 400 (Bad Request) if the telephone has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/telephones",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Telephone> createTelephone(@RequestBody Telephone telephone) throws URISyntaxException {
        log.debug("REST request to save Telephone : {}", telephone);
        if (telephone.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("telephone", "idexists", "A new telephone cannot already have an ID")).body(null);
        }
        Telephone result = telephoneRepository.save(telephone);
        return ResponseEntity.created(new URI("/api/telephones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("telephone", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /telephones : Updates an existing telephone.
     *
     * @param telephone the telephone to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated telephone,
     * or with status 400 (Bad Request) if the telephone is not valid,
     * or with status 500 (Internal Server Error) if the telephone couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/telephones",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Telephone> updateTelephone(@RequestBody Telephone telephone) throws URISyntaxException {
        log.debug("REST request to update Telephone : {}", telephone);
        if (telephone.getId() == null) {
            return createTelephone(telephone);
        }
        Telephone result = telephoneRepository.save(telephone);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("telephone", telephone.getId().toString()))
            .body(result);
    }

    /**
     * GET  /telephones : get all the telephones.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of telephones in body
     */
    @RequestMapping(value = "/telephones",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Telephone> getAllTelephones() {
        log.debug("REST request to get all Telephones");
        List<Telephone> telephones = telephoneRepository.findAll();
        return telephones;
    }

    /**
     * GET  /telephones/:id : get the "id" telephone.
     *
     * @param id the id of the telephone to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the telephone, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/telephones/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Telephone> getTelephone(@PathVariable Long id) {
        log.debug("REST request to get Telephone : {}", id);
        Telephone telephone = telephoneRepository.findOne(id);
        return Optional.ofNullable(telephone)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /telephones/:id : delete the "id" telephone.
     *
     * @param id the id of the telephone to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/telephones/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTelephone(@PathVariable Long id) {
        log.debug("REST request to delete Telephone : {}", id);
        telephoneRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("telephone", id.toString())).build();
    }

}
