package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.CV;
import com.mycompany.myapp.repository.CVRepository;
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
 * REST controller for managing CV.
 */
@RestController
@RequestMapping("/api")
public class CVResource {

    private final Logger log = LoggerFactory.getLogger(CVResource.class);
        
    @Inject
    private CVRepository cVRepository;
    
    /**
     * POST  /c-vs : Create a new cV.
     *
     * @param cV the cV to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cV, or with status 400 (Bad Request) if the cV has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/c-vs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CV> createCV(@RequestBody CV cV) throws URISyntaxException {
        log.debug("REST request to save CV : {}", cV);
        if (cV.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cV", "idexists", "A new cV cannot already have an ID")).body(null);
        }
        CV result = cVRepository.save(cV);
        return ResponseEntity.created(new URI("/api/c-vs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cV", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /c-vs : Updates an existing cV.
     *
     * @param cV the cV to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cV,
     * or with status 400 (Bad Request) if the cV is not valid,
     * or with status 500 (Internal Server Error) if the cV couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/c-vs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CV> updateCV(@RequestBody CV cV) throws URISyntaxException {
        log.debug("REST request to update CV : {}", cV);
        if (cV.getId() == null) {
            return createCV(cV);
        }
        CV result = cVRepository.save(cV);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cV", cV.getId().toString()))
            .body(result);
    }

    /**
     * GET  /c-vs : get all the cVS.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cVS in body
     */
    @RequestMapping(value = "/c-vs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CV> getAllCVS() {
        log.debug("REST request to get all CVS");
        List<CV> cVS = cVRepository.findAll();
        return cVS;
    }

    /**
     * GET  /c-vs/:id : get the "id" cV.
     *
     * @param id the id of the cV to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cV, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/c-vs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CV> getCV(@PathVariable Long id) {
        log.debug("REST request to get CV : {}", id);
        CV cV = cVRepository.findOne(id);
        return Optional.ofNullable(cV)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /c-vs/:id : delete the "id" cV.
     *
     * @param id the id of the cV to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/c-vs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCV(@PathVariable Long id) {
        log.debug("REST request to delete CV : {}", id);
        cVRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cV", id.toString())).build();
    }

}
