package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.DonneesCollaborateur;
import com.mycompany.myapp.repository.DonneesCollaborateurRepository;
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
 * REST controller for managing DonneesCollaborateur.
 */
@RestController
@RequestMapping("/api")
public class DonneesCollaborateurResource {

    private final Logger log = LoggerFactory.getLogger(DonneesCollaborateurResource.class);
        
    @Inject
    private DonneesCollaborateurRepository donneesCollaborateurRepository;
    
    /**
     * POST  /donnees-collaborateurs : Create a new donneesCollaborateur.
     *
     * @param donneesCollaborateur the donneesCollaborateur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new donneesCollaborateur, or with status 400 (Bad Request) if the donneesCollaborateur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/donnees-collaborateurs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DonneesCollaborateur> createDonneesCollaborateur(@RequestBody DonneesCollaborateur donneesCollaborateur) throws URISyntaxException {
        log.debug("REST request to save DonneesCollaborateur : {}", donneesCollaborateur);
        if (donneesCollaborateur.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("donneesCollaborateur", "idexists", "A new donneesCollaborateur cannot already have an ID")).body(null);
        }
        DonneesCollaborateur result = donneesCollaborateurRepository.save(donneesCollaborateur);
        return ResponseEntity.created(new URI("/api/donnees-collaborateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("donneesCollaborateur", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /donnees-collaborateurs : Updates an existing donneesCollaborateur.
     *
     * @param donneesCollaborateur the donneesCollaborateur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated donneesCollaborateur,
     * or with status 400 (Bad Request) if the donneesCollaborateur is not valid,
     * or with status 500 (Internal Server Error) if the donneesCollaborateur couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/donnees-collaborateurs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DonneesCollaborateur> updateDonneesCollaborateur(@RequestBody DonneesCollaborateur donneesCollaborateur) throws URISyntaxException {
        log.debug("REST request to update DonneesCollaborateur : {}", donneesCollaborateur);
        if (donneesCollaborateur.getId() == null) {
            return createDonneesCollaborateur(donneesCollaborateur);
        }
        DonneesCollaborateur result = donneesCollaborateurRepository.save(donneesCollaborateur);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("donneesCollaborateur", donneesCollaborateur.getId().toString()))
            .body(result);
    }

    /**
     * GET  /donnees-collaborateurs : get all the donneesCollaborateurs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of donneesCollaborateurs in body
     */
    @RequestMapping(value = "/donnees-collaborateurs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DonneesCollaborateur> getAllDonneesCollaborateurs() {
        log.debug("REST request to get all DonneesCollaborateurs");
        List<DonneesCollaborateur> donneesCollaborateurs = donneesCollaborateurRepository.findAll();
        return donneesCollaborateurs;
    }

    /**
     * GET  /donnees-collaborateurs/:id : get the "id" donneesCollaborateur.
     *
     * @param id the id of the donneesCollaborateur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the donneesCollaborateur, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/donnees-collaborateurs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DonneesCollaborateur> getDonneesCollaborateur(@PathVariable Long id) {
        log.debug("REST request to get DonneesCollaborateur : {}", id);
        DonneesCollaborateur donneesCollaborateur = donneesCollaborateurRepository.findOne(id);
        return Optional.ofNullable(donneesCollaborateur)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /donnees-collaborateurs/:id : delete the "id" donneesCollaborateur.
     *
     * @param id the id of the donneesCollaborateur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/donnees-collaborateurs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDonneesCollaborateur(@PathVariable Long id) {
        log.debug("REST request to delete DonneesCollaborateur : {}", id);
        donneesCollaborateurRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("donneesCollaborateur", id.toString())).build();
    }

}
