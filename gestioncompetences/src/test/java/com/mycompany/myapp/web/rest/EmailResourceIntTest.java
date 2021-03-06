package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.GestioncompetencesApp;
import com.mycompany.myapp.domain.Email;
import com.mycompany.myapp.repository.EmailRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EmailResource REST controller.
 *
 * @see EmailResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GestioncompetencesApp.class)
@WebAppConfiguration
@IntegrationTest
public class EmailResourceIntTest {

    private static final String DEFAULT_EMAIL_LIBELLE = "AAAAA";
    private static final String UPDATED_EMAIL_LIBELLE = "BBBBB";

    @Inject
    private EmailRepository emailRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmailMockMvc;

    private Email email;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmailResource emailResource = new EmailResource();
        ReflectionTestUtils.setField(emailResource, "emailRepository", emailRepository);
        this.restEmailMockMvc = MockMvcBuilders.standaloneSetup(emailResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        email = new Email();
        email.setEmailLibelle(DEFAULT_EMAIL_LIBELLE);
    }

    @Test
    @Transactional
    public void createEmail() throws Exception {
        int databaseSizeBeforeCreate = emailRepository.findAll().size();

        // Create the Email

        restEmailMockMvc.perform(post("/api/emails")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(email)))
                .andExpect(status().isCreated());

        // Validate the Email in the database
        List<Email> emails = emailRepository.findAll();
        assertThat(emails).hasSize(databaseSizeBeforeCreate + 1);
        Email testEmail = emails.get(emails.size() - 1);
        assertThat(testEmail.getEmailLibelle()).isEqualTo(DEFAULT_EMAIL_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllEmails() throws Exception {
        // Initialize the database
        emailRepository.saveAndFlush(email);

        // Get all the emails
        restEmailMockMvc.perform(get("/api/emails?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(email.getId().intValue())))
                .andExpect(jsonPath("$.[*].emailLibelle").value(hasItem(DEFAULT_EMAIL_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void getEmail() throws Exception {
        // Initialize the database
        emailRepository.saveAndFlush(email);

        // Get the email
        restEmailMockMvc.perform(get("/api/emails/{id}", email.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(email.getId().intValue()))
            .andExpect(jsonPath("$.emailLibelle").value(DEFAULT_EMAIL_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmail() throws Exception {
        // Get the email
        restEmailMockMvc.perform(get("/api/emails/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmail() throws Exception {
        // Initialize the database
        emailRepository.saveAndFlush(email);
        int databaseSizeBeforeUpdate = emailRepository.findAll().size();

        // Update the email
        Email updatedEmail = new Email();
        updatedEmail.setId(email.getId());
        updatedEmail.setEmailLibelle(UPDATED_EMAIL_LIBELLE);

        restEmailMockMvc.perform(put("/api/emails")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEmail)))
                .andExpect(status().isOk());

        // Validate the Email in the database
        List<Email> emails = emailRepository.findAll();
        assertThat(emails).hasSize(databaseSizeBeforeUpdate);
        Email testEmail = emails.get(emails.size() - 1);
        assertThat(testEmail.getEmailLibelle()).isEqualTo(UPDATED_EMAIL_LIBELLE);
    }

    @Test
    @Transactional
    public void deleteEmail() throws Exception {
        // Initialize the database
        emailRepository.saveAndFlush(email);
        int databaseSizeBeforeDelete = emailRepository.findAll().size();

        // Get the email
        restEmailMockMvc.perform(delete("/api/emails/{id}", email.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Email> emails = emailRepository.findAll();
        assertThat(emails).hasSize(databaseSizeBeforeDelete - 1);
    }
}
