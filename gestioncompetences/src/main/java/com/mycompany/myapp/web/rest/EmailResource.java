package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.Email;
import com.mycompany.myapp.repository.EmailRepository;
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
 * REST controller for managing Email.
 */
@RestController
@RequestMapping("/api")
public class EmailResource {

    private final Logger log = LoggerFactory.getLogger(EmailResource.class);
        
    @Inject
    private EmailRepository emailRepository;
    
    /**
     * POST  /emails : Create a new email.
     *
     * @param email the email to create
     * @return the ResponseEntity with status 201 (Created) and with body the new email, or with status 400 (Bad Request) if the email has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/emails",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Email> createEmail(@RequestBody Email email) throws URISyntaxException {
        log.debug("REST request to save Email : {}", email);
        if (email.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("email", "idexists", "A new email cannot already have an ID")).body(null);
        }
        Email result = emailRepository.save(email);
        return ResponseEntity.created(new URI("/api/emails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("email", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /emails : Updates an existing email.
     *
     * @param email the email to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated email,
     * or with status 400 (Bad Request) if the email is not valid,
     * or with status 500 (Internal Server Error) if the email couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/emails",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Email> updateEmail(@RequestBody Email email) throws URISyntaxException {
        log.debug("REST request to update Email : {}", email);
        if (email.getId() == null) {
            return createEmail(email);
        }
        Email result = emailRepository.save(email);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("email", email.getId().toString()))
            .body(result);
    }

    /**
     * GET  /emails : get all the emails.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of emails in body
     */
    @RequestMapping(value = "/emails",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Email> getAllEmails() {
        log.debug("REST request to get all Emails");
        List<Email> emails = emailRepository.findAll();
        return emails;
    }

    /**
     * GET  /emails/:id : get the "id" email.
     *
     * @param id the id of the email to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the email, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/emails/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Email> getEmail(@PathVariable Long id) {
        log.debug("REST request to get Email : {}", id);
        Email email = emailRepository.findOne(id);
        return Optional.ofNullable(email)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /emails/:id : delete the "id" email.
     *
     * @param id the id of the email to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/emails/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        log.debug("REST request to delete Email : {}", id);
        emailRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("email", id.toString())).build();
    }

}
