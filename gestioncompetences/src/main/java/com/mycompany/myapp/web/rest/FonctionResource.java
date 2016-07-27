package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Fonction;
import com.mycompany.myapp.repository.FonctionRepository;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * REST controller for managing Fonction.
 */
@RestController
@RequestMapping("/api")
public class FonctionResource {

    private final Logger log = LoggerFactory.getLogger(FonctionResource.class);
        
    @Inject
    private FonctionRepository fonctionRepository;
    
    /**
     * POST  /fonctions : Create a new fonction.
     *
     * @param fonction the fonction to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fonction, or with status 400 (Bad Request) if the fonction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/fonctions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fonction> createFonction(@Valid @RequestBody Fonction fonction) throws URISyntaxException {
        log.debug("REST request to save Fonction : {}", fonction);
        if (fonction.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("fonction", "idexists", "A new fonction cannot already have an ID")).body(null);
        }
        Fonction result = fonctionRepository.save(fonction);
        return ResponseEntity.created(new URI("/api/fonctions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fonction", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fonctions : Updates an existing fonction.
     *
     * @param fonction the fonction to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fonction,
     * or with status 400 (Bad Request) if the fonction is not valid,
     * or with status 500 (Internal Server Error) if the fonction couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/fonctions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fonction> updateFonction(@Valid @RequestBody Fonction fonction) throws URISyntaxException {
        log.debug("REST request to update Fonction : {}", fonction);
        if (fonction.getId() == null) {
            return createFonction(fonction);
        }
        Fonction result = fonctionRepository.save(fonction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fonction", fonction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fonctions : get all the fonctions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fonctions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/fonctions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Fonction>> getAllFonctions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Fonctions");
        Page<Fonction> page = fonctionRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fonctions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fonctions/:id : get the "id" fonction.
     *
     * @param id the id of the fonction to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fonction, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/fonctions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Fonction> getFonction(@PathVariable Long id) {
        log.debug("REST request to get Fonction : {}", id);
        Fonction fonction = fonctionRepository.findOne(id);
        return Optional.ofNullable(fonction)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /fonctions/:id : delete the "id" fonction.
     *
     * @param id the id of the fonction to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/fonctions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFonction(@PathVariable Long id) {
        log.debug("REST request to delete Fonction : {}", id);
        fonctionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fonction", id.toString())).build();
    }

}
