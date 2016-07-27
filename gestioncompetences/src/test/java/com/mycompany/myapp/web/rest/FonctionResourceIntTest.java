package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.GestioncompetencesApp;
import com.mycompany.myapp.domain.Fonction;
import com.mycompany.myapp.repository.FonctionRepository;

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
 * Test class for the FonctionResource REST controller.
 *
 * @see FonctionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GestioncompetencesApp.class)
@WebAppConfiguration
@IntegrationTest
public class FonctionResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_FONCTION_NOM = "AAAAA";
    private static final String UPDATED_FONCTION_NOM = "BBBBB";

    private static final ZonedDateTime DEFAULT_FONCTION_DATE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FONCTION_DATE_DEBUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FONCTION_DATE_DEBUT_STR = dateTimeFormatter.format(DEFAULT_FONCTION_DATE_DEBUT);

    private static final ZonedDateTime DEFAULT_FONCTION_DATE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FONCTION_DATE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FONCTION_DATE_FIN_STR = dateTimeFormatter.format(DEFAULT_FONCTION_DATE_FIN);

    @Inject
    private FonctionRepository fonctionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFonctionMockMvc;

    private Fonction fonction;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FonctionResource fonctionResource = new FonctionResource();
        ReflectionTestUtils.setField(fonctionResource, "fonctionRepository", fonctionRepository);
        this.restFonctionMockMvc = MockMvcBuilders.standaloneSetup(fonctionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        fonction = new Fonction();
        fonction.setFonctionNom(DEFAULT_FONCTION_NOM);
        fonction.setFonctionDateDebut(DEFAULT_FONCTION_DATE_DEBUT);
        fonction.setFonctionDateFin(DEFAULT_FONCTION_DATE_FIN);
    }

    @Test
    @Transactional
    public void createFonction() throws Exception {
        int databaseSizeBeforeCreate = fonctionRepository.findAll().size();

        // Create the Fonction

        restFonctionMockMvc.perform(post("/api/fonctions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fonction)))
                .andExpect(status().isCreated());

        // Validate the Fonction in the database
        List<Fonction> fonctions = fonctionRepository.findAll();
        assertThat(fonctions).hasSize(databaseSizeBeforeCreate + 1);
        Fonction testFonction = fonctions.get(fonctions.size() - 1);
        assertThat(testFonction.getFonctionNom()).isEqualTo(DEFAULT_FONCTION_NOM);
        assertThat(testFonction.getFonctionDateDebut()).isEqualTo(DEFAULT_FONCTION_DATE_DEBUT);
        assertThat(testFonction.getFonctionDateFin()).isEqualTo(DEFAULT_FONCTION_DATE_FIN);
    }

    @Test
    @Transactional
    public void checkFonctionNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = fonctionRepository.findAll().size();
        // set the field null
        fonction.setFonctionNom(null);

        // Create the Fonction, which fails.

        restFonctionMockMvc.perform(post("/api/fonctions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fonction)))
                .andExpect(status().isBadRequest());

        List<Fonction> fonctions = fonctionRepository.findAll();
        assertThat(fonctions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFonctionDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = fonctionRepository.findAll().size();
        // set the field null
        fonction.setFonctionDateDebut(null);

        // Create the Fonction, which fails.

        restFonctionMockMvc.perform(post("/api/fonctions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fonction)))
                .andExpect(status().isBadRequest());

        List<Fonction> fonctions = fonctionRepository.findAll();
        assertThat(fonctions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFonctions() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get all the fonctions
        restFonctionMockMvc.perform(get("/api/fonctions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fonction.getId().intValue())))
                .andExpect(jsonPath("$.[*].fonctionNom").value(hasItem(DEFAULT_FONCTION_NOM.toString())))
                .andExpect(jsonPath("$.[*].fonctionDateDebut").value(hasItem(DEFAULT_FONCTION_DATE_DEBUT_STR)))
                .andExpect(jsonPath("$.[*].fonctionDateFin").value(hasItem(DEFAULT_FONCTION_DATE_FIN_STR)));
    }

    @Test
    @Transactional
    public void getFonction() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);

        // Get the fonction
        restFonctionMockMvc.perform(get("/api/fonctions/{id}", fonction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(fonction.getId().intValue()))
            .andExpect(jsonPath("$.fonctionNom").value(DEFAULT_FONCTION_NOM.toString()))
            .andExpect(jsonPath("$.fonctionDateDebut").value(DEFAULT_FONCTION_DATE_DEBUT_STR))
            .andExpect(jsonPath("$.fonctionDateFin").value(DEFAULT_FONCTION_DATE_FIN_STR));
    }

    @Test
    @Transactional
    public void getNonExistingFonction() throws Exception {
        // Get the fonction
        restFonctionMockMvc.perform(get("/api/fonctions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFonction() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);
        int databaseSizeBeforeUpdate = fonctionRepository.findAll().size();

        // Update the fonction
        Fonction updatedFonction = new Fonction();
        updatedFonction.setId(fonction.getId());
        updatedFonction.setFonctionNom(UPDATED_FONCTION_NOM);
        updatedFonction.setFonctionDateDebut(UPDATED_FONCTION_DATE_DEBUT);
        updatedFonction.setFonctionDateFin(UPDATED_FONCTION_DATE_FIN);

        restFonctionMockMvc.perform(put("/api/fonctions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFonction)))
                .andExpect(status().isOk());

        // Validate the Fonction in the database
        List<Fonction> fonctions = fonctionRepository.findAll();
        assertThat(fonctions).hasSize(databaseSizeBeforeUpdate);
        Fonction testFonction = fonctions.get(fonctions.size() - 1);
        assertThat(testFonction.getFonctionNom()).isEqualTo(UPDATED_FONCTION_NOM);
        assertThat(testFonction.getFonctionDateDebut()).isEqualTo(UPDATED_FONCTION_DATE_DEBUT);
        assertThat(testFonction.getFonctionDateFin()).isEqualTo(UPDATED_FONCTION_DATE_FIN);
    }

    @Test
    @Transactional
    public void deleteFonction() throws Exception {
        // Initialize the database
        fonctionRepository.saveAndFlush(fonction);
        int databaseSizeBeforeDelete = fonctionRepository.findAll().size();

        // Get the fonction
        restFonctionMockMvc.perform(delete("/api/fonctions/{id}", fonction.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Fonction> fonctions = fonctionRepository.findAll();
        assertThat(fonctions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
