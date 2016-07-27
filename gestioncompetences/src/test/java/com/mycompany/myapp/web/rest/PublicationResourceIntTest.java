package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.GestioncompetencesApp;
import com.mycompany.myapp.domain.Publication;
import com.mycompany.myapp.repository.PublicationRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PublicationResource REST controller.
 *
 * @see PublicationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GestioncompetencesApp.class)
@WebAppConfiguration
@IntegrationTest
public class PublicationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_PUBLICATION_NOM = "AAAAA";
    private static final String UPDATED_PUBLICATION_NOM = "BBBBB";

    private static final ZonedDateTime DEFAULT_PUBLICATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_PUBLICATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_PUBLICATION_DATE_STR = dateTimeFormatter.format(DEFAULT_PUBLICATION_DATE);

    @Inject
    private PublicationRepository publicationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPublicationMockMvc;

    private Publication publication;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PublicationResource publicationResource = new PublicationResource();
        ReflectionTestUtils.setField(publicationResource, "publicationRepository", publicationRepository);
        this.restPublicationMockMvc = MockMvcBuilders.standaloneSetup(publicationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        publication = new Publication();
        publication.setPublicationNom(DEFAULT_PUBLICATION_NOM);
        publication.setPublicationDate(DEFAULT_PUBLICATION_DATE);
    }

    @Test
    @Transactional
    public void createPublication() throws Exception {
        int databaseSizeBeforeCreate = publicationRepository.findAll().size();

        // Create the Publication

        restPublicationMockMvc.perform(post("/api/publications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(publication)))
                .andExpect(status().isCreated());

        // Validate the Publication in the database
        List<Publication> publications = publicationRepository.findAll();
        assertThat(publications).hasSize(databaseSizeBeforeCreate + 1);
        Publication testPublication = publications.get(publications.size() - 1);
        assertThat(testPublication.getPublicationNom()).isEqualTo(DEFAULT_PUBLICATION_NOM);
        assertThat(testPublication.getPublicationDate()).isEqualTo(DEFAULT_PUBLICATION_DATE);
    }

    @Test
    @Transactional
    public void checkPublicationNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = publicationRepository.findAll().size();
        // set the field null
        publication.setPublicationNom(null);

        // Create the Publication, which fails.

        restPublicationMockMvc.perform(post("/api/publications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(publication)))
                .andExpect(status().isBadRequest());

        List<Publication> publications = publicationRepository.findAll();
        assertThat(publications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPublicationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = publicationRepository.findAll().size();
        // set the field null
        publication.setPublicationDate(null);

        // Create the Publication, which fails.

        restPublicationMockMvc.perform(post("/api/publications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(publication)))
                .andExpect(status().isBadRequest());

        List<Publication> publications = publicationRepository.findAll();
        assertThat(publications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPublications() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get all the publications
        restPublicationMockMvc.perform(get("/api/publications?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(publication.getId().intValue())))
                .andExpect(jsonPath("$.[*].publicationNom").value(hasItem(DEFAULT_PUBLICATION_NOM.toString())))
                .andExpect(jsonPath("$.[*].publicationDate").value(hasItem(DEFAULT_PUBLICATION_DATE_STR)));
    }

    @Test
    @Transactional
    public void getPublication() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);

        // Get the publication
        restPublicationMockMvc.perform(get("/api/publications/{id}", publication.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(publication.getId().intValue()))
            .andExpect(jsonPath("$.publicationNom").value(DEFAULT_PUBLICATION_NOM.toString()))
            .andExpect(jsonPath("$.publicationDate").value(DEFAULT_PUBLICATION_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingPublication() throws Exception {
        // Get the publication
        restPublicationMockMvc.perform(get("/api/publications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePublication() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);
        int databaseSizeBeforeUpdate = publicationRepository.findAll().size();

        // Update the publication
        Publication updatedPublication = new Publication();
        updatedPublication.setId(publication.getId());
        updatedPublication.setPublicationNom(UPDATED_PUBLICATION_NOM);
        updatedPublication.setPublicationDate(UPDATED_PUBLICATION_DATE);

        restPublicationMockMvc.perform(put("/api/publications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPublication)))
                .andExpect(status().isOk());

        // Validate the Publication in the database
        List<Publication> publications = publicationRepository.findAll();
        assertThat(publications).hasSize(databaseSizeBeforeUpdate);
        Publication testPublication = publications.get(publications.size() - 1);
        assertThat(testPublication.getPublicationNom()).isEqualTo(UPDATED_PUBLICATION_NOM);
        assertThat(testPublication.getPublicationDate()).isEqualTo(UPDATED_PUBLICATION_DATE);
    }

    @Test
    @Transactional
    public void deletePublication() throws Exception {
        // Initialize the database
        publicationRepository.saveAndFlush(publication);
        int databaseSizeBeforeDelete = publicationRepository.findAll().size();

        // Get the publication
        restPublicationMockMvc.perform(delete("/api/publications/{id}", publication.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Publication> publications = publicationRepository.findAll();
        assertThat(publications).hasSize(databaseSizeBeforeDelete - 1);
    }
}
