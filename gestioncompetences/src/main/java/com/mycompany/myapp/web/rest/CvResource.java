package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Cv;
import com.mycompany.myapp.domain.DonneesRubrique;
import com.mycompany.myapp.domain.Rubrique;
import com.mycompany.myapp.repository.CvRepository;
import com.mycompany.myapp.repository.DonneesRubriqueRepository;
import com.mycompany.myapp.repository.RubriqueRepository;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing Cv.
 */
@RestController
@RequestMapping("/api")
public class CvResource {

    private final Logger log = LoggerFactory.getLogger(CvResource.class);
        
    @Inject
    private CvRepository cvRepository;
    
    @Inject
    private RubriqueRepository rubriqueRepository;
    
    @Inject
    private DonneesRubriqueRepository donneesRubriqueRepository;
    
    /**
     * POST  /cvs : Create a new cv.
     *
     * @param cv the cv to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cv, or with status 400 (Bad Request) if the cv has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cvs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cv> createCv(@RequestBody Cv cv) throws URISyntaxException {
        log.debug("REST request to save Cv : {}", cv);
        if (cv.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cv", "idexists", "A new cv cannot already have an ID")).body(null);
        }
        
        Cv result = cvRepository.save(cv);
        
        List<Rubrique> listeRub = rubriqueRepository.findAll();
        Set<DonneesRubrique> donnees = new HashSet<DonneesRubrique>();
        int i = 1;
        for (Rubrique rub : listeRub) {
        	DonneesRubrique d = new DonneesRubrique();
        	if (null != rub.isRubriqueObligatoire() && rub.isRubriqueObligatoire()) {
        		d.setRubrique(rub);
        		d.setDonneesRubriqueTitre(rub.getRubriqueLibelle());
        		d.setDonneesRubriqueOrdre(i);
        		d.setDonneesRubriqueDateDebut(ZonedDateTime.now());
        		d.setDonneesRubriqueDescription(rub.getRubriqueLibelle());
        		d.setIdCv(result.getId());
        		donnees.add(d);
        		i++;
        	}
        }
        
        donneesRubriqueRepository.save(donnees);
        
        return ResponseEntity.created(new URI("/api/cvs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cv", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cvs : Updates an existing cv.
     *
     * @param cv the cv to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cv,
     * or with status 400 (Bad Request) if the cv is not valid,
     * or with status 500 (Internal Server Error) if the cv couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cvs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cv> updateCv(@RequestBody Cv cv) throws URISyntaxException {
        log.debug("REST request to update Cv : {}", cv);
        if (cv.getId() == null) {
            return createCv(cv);
        }
        Cv result = cvRepository.save(cv);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cv", cv.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cvs : get all the cvs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cvs in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/cvs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Cv>> getAllCvs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Cvs");
        Page<Cv> page = cvRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cvs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cvs/:id : get the "id" cv.
     *
     * @param id the id of the cv to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cv, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cvs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cv> getCv(@PathVariable Long id) {
        log.debug("REST request to get Cv : {}", id);
        Cv cv = cvRepository.findOne(id);
        return Optional.ofNullable(cv)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cvs/:id : delete the "id" cv.
     *
     * @param id the id of the cv to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/cvs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCv(@PathVariable Long id) {
        log.debug("REST request to delete Cv : {}", id);
        cvRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cv", id.toString())).build();
    }

}
