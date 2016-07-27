package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.GestioncompetencesApp;
import com.mycompany.myapp.domain.DonneesRubrique;
import com.mycompany.myapp.repository.DonneesRubriqueRepository;

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

import com.mycompany.myapp.domain.enumeration.NiveauCompetence;

/**
 * Test class for the DonneesRubriqueResource REST controller.
 *
 * @see DonneesRubriqueResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GestioncompetencesApp.class)
@WebAppConfiguration
@IntegrationTest
public class DonneesRubriqueResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_DONNEES_RUBRIQUE_TITRE = "AAAAA";
    private static final String UPDATED_DONNEES_RUBRIQUE_TITRE = "BBBBB";

    private static final ZonedDateTime DEFAULT_DONNEES_RUBRIQUE_DATE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DONNEES_RUBRIQUE_DATE_DEBUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DONNEES_RUBRIQUE_DATE_DEBUT_STR = dateTimeFormatter.format(DEFAULT_DONNEES_RUBRIQUE_DATE_DEBUT);

    private static final ZonedDateTime DEFAULT_DONNEES_RUBRIQUE_DATE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DONNEES_RUBRIQUE_DATE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DONNEES_RUBRIQUE_DATE_FIN_STR = dateTimeFormatter.format(DEFAULT_DONNEES_RUBRIQUE_DATE_FIN);
    private static final String DEFAULT_DONNEES_RUBRIQUE_POSTE = "AAAAA";
    private static final String UPDATED_DONNEES_RUBRIQUE_POSTE = "BBBBB";
    private static final String DEFAULT_DONNEES_RUBRIQUE_CLIENT = "AAAAA";
    private static final String UPDATED_DONNEES_RUBRIQUE_CLIENT = "BBBBB";

    private static final NiveauCompetence DEFAULT_DONNEES_RUBRIQUE_NIVEAUCOMPETENCE = NiveauCompetence.DECOUVERTE;
    private static final NiveauCompetence UPDATED_DONNEES_RUBRIQUE_NIVEAUCOMPETENCE = NiveauCompetence.OPERATIONNELLE;
    private static final String DEFAULT_DONNEES_RUBRIQUE_DESCRIPTION = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DONNEES_RUBRIQUE_DESCRIPTION = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private DonneesRubriqueRepository donneesRubriqueRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDonneesRubriqueMockMvc;

    private DonneesRubrique donneesRubrique;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DonneesRubriqueResource donneesRubriqueResource = new DonneesRubriqueResource();
        ReflectionTestUtils.setField(donneesRubriqueResource, "donneesRubriqueRepository", donneesRubriqueRepository);
        this.restDonneesRubriqueMockMvc = MockMvcBuilders.standaloneSetup(donneesRubriqueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        donneesRubrique = new DonneesRubrique();
        donneesRubrique.setDonneesRubriqueTitre(DEFAULT_DONNEES_RUBRIQUE_TITRE);
        donneesRubrique.setDonneesRubriqueDateDebut(DEFAULT_DONNEES_RUBRIQUE_DATE_DEBUT);
        donneesRubrique.setDonneesRubriqueDateFin(DEFAULT_DONNEES_RUBRIQUE_DATE_FIN);
        donneesRubrique.setDonneesRubriquePoste(DEFAULT_DONNEES_RUBRIQUE_POSTE);
        donneesRubrique.setDonneesRubriqueClient(DEFAULT_DONNEES_RUBRIQUE_CLIENT);
        donneesRubrique.setDonneesRubriqueNiveaucompetence(DEFAULT_DONNEES_RUBRIQUE_NIVEAUCOMPETENCE);
        donneesRubrique.setDonneesRubriqueDescription(DEFAULT_DONNEES_RUBRIQUE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createDonneesRubrique() throws Exception {
        int databaseSizeBeforeCreate = donneesRubriqueRepository.findAll().size();

        // Create the DonneesRubrique

        restDonneesRubriqueMockMvc.perform(post("/api/donnees-rubriques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(donneesRubrique)))
                .andExpect(status().isCreated());

        // Validate the DonneesRubrique in the database
        List<DonneesRubrique> donneesRubriques = donneesRubriqueRepository.findAll();
        assertThat(donneesRubriques).hasSize(databaseSizeBeforeCreate + 1);
        DonneesRubrique testDonneesRubrique = donneesRubriques.get(donneesRubriques.size() - 1);
        assertThat(testDonneesRubrique.getDonneesRubriqueTitre()).isEqualTo(DEFAULT_DONNEES_RUBRIQUE_TITRE);
        assertThat(testDonneesRubrique.getDonneesRubriqueDateDebut()).isEqualTo(DEFAULT_DONNEES_RUBRIQUE_DATE_DEBUT);
        assertThat(testDonneesRubrique.getDonneesRubriqueDateFin()).isEqualTo(DEFAULT_DONNEES_RUBRIQUE_DATE_FIN);
        assertThat(testDonneesRubrique.getDonneesRubriquePoste()).isEqualTo(DEFAULT_DONNEES_RUBRIQUE_POSTE);
        assertThat(testDonneesRubrique.getDonneesRubriqueClient()).isEqualTo(DEFAULT_DONNEES_RUBRIQUE_CLIENT);
        assertThat(testDonneesRubrique.getDonneesRubriqueNiveaucompetence()).isEqualTo(DEFAULT_DONNEES_RUBRIQUE_NIVEAUCOMPETENCE);
        assertThat(testDonneesRubrique.getDonneesRubriqueDescription()).isEqualTo(DEFAULT_DONNEES_RUBRIQUE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void checkDonneesRubriqueTitreIsRequired() throws Exception {
        int databaseSizeBeforeTest = donneesRubriqueRepository.findAll().size();
        // set the field null
        donneesRubrique.setDonneesRubriqueTitre(null);

        // Create the DonneesRubrique, which fails.

        restDonneesRubriqueMockMvc.perform(post("/api/donnees-rubriques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(donneesRubrique)))
                .andExpect(status().isBadRequest());

        List<DonneesRubrique> donneesRubriques = donneesRubriqueRepository.findAll();
        assertThat(donneesRubriques).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDonneesRubriqueDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = donneesRubriqueRepository.findAll().size();
        // set the field null
        donneesRubrique.setDonneesRubriqueDateDebut(null);

        // Create the DonneesRubrique, which fails.

        restDonneesRubriqueMockMvc.perform(post("/api/donnees-rubriques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(donneesRubrique)))
                .andExpect(status().isBadRequest());

        List<DonneesRubrique> donneesRubriques = donneesRubriqueRepository.findAll();
        assertThat(donneesRubriques).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDonneesRubriqueDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = donneesRubriqueRepository.findAll().size();
        // set the field null
        donneesRubrique.setDonneesRubriqueDescription(null);

        // Create the DonneesRubrique, which fails.

        restDonneesRubriqueMockMvc.perform(post("/api/donnees-rubriques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(donneesRubrique)))
                .andExpect(status().isBadRequest());

        List<DonneesRubrique> donneesRubriques = donneesRubriqueRepository.findAll();
        assertThat(donneesRubriques).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDonneesRubriques() throws Exception {
        // Initialize the database
        donneesRubriqueRepository.saveAndFlush(donneesRubrique);

        // Get all the donneesRubriques
        restDonneesRubriqueMockMvc.perform(get("/api/donnees-rubriques?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(donneesRubrique.getId().intValue())))
                .andExpect(jsonPath("$.[*].donneesRubriqueTitre").value(hasItem(DEFAULT_DONNEES_RUBRIQUE_TITRE.toString())))
                .andExpect(jsonPath("$.[*].donneesRubriqueDateDebut").value(hasItem(DEFAULT_DONNEES_RUBRIQUE_DATE_DEBUT_STR)))
                .andExpect(jsonPath("$.[*].donneesRubriqueDateFin").value(hasItem(DEFAULT_DONNEES_RUBRIQUE_DATE_FIN_STR)))
                .andExpect(jsonPath("$.[*].donneesRubriquePoste").value(hasItem(DEFAULT_DONNEES_RUBRIQUE_POSTE.toString())))
                .andExpect(jsonPath("$.[*].donneesRubriqueClient").value(hasItem(DEFAULT_DONNEES_RUBRIQUE_CLIENT.toString())))
                .andExpect(jsonPath("$.[*].donneesRubriqueNiveaucompetence").value(hasItem(DEFAULT_DONNEES_RUBRIQUE_NIVEAUCOMPETENCE.toString())))
                .andExpect(jsonPath("$.[*].donneesRubriqueDescription").value(hasItem(DEFAULT_DONNEES_RUBRIQUE_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getDonneesRubrique() throws Exception {
        // Initialize the database
        donneesRubriqueRepository.saveAndFlush(donneesRubrique);

        // Get the donneesRubrique
        restDonneesRubriqueMockMvc.perform(get("/api/donnees-rubriques/{id}", donneesRubrique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(donneesRubrique.getId().intValue()))
            .andExpect(jsonPath("$.donneesRubriqueTitre").value(DEFAULT_DONNEES_RUBRIQUE_TITRE.toString()))
            .andExpect(jsonPath("$.donneesRubriqueDateDebut").value(DEFAULT_DONNEES_RUBRIQUE_DATE_DEBUT_STR))
            .andExpect(jsonPath("$.donneesRubriqueDateFin").value(DEFAULT_DONNEES_RUBRIQUE_DATE_FIN_STR))
            .andExpect(jsonPath("$.donneesRubriquePoste").value(DEFAULT_DONNEES_RUBRIQUE_POSTE.toString()))
            .andExpect(jsonPath("$.donneesRubriqueClient").value(DEFAULT_DONNEES_RUBRIQUE_CLIENT.toString()))
            .andExpect(jsonPath("$.donneesRubriqueNiveaucompetence").value(DEFAULT_DONNEES_RUBRIQUE_NIVEAUCOMPETENCE.toString()))
            .andExpect(jsonPath("$.donneesRubriqueDescription").value(DEFAULT_DONNEES_RUBRIQUE_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDonneesRubrique() throws Exception {
        // Get the donneesRubrique
        restDonneesRubriqueMockMvc.perform(get("/api/donnees-rubriques/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDonneesRubrique() throws Exception {
        // Initialize the database
        donneesRubriqueRepository.saveAndFlush(donneesRubrique);
        int databaseSizeBeforeUpdate = donneesRubriqueRepository.findAll().size();

        // Update the donneesRubrique
        DonneesRubrique updatedDonneesRubrique = new DonneesRubrique();
        updatedDonneesRubrique.setId(donneesRubrique.getId());
        updatedDonneesRubrique.setDonneesRubriqueTitre(UPDATED_DONNEES_RUBRIQUE_TITRE);
        updatedDonneesRubrique.setDonneesRubriqueDateDebut(UPDATED_DONNEES_RUBRIQUE_DATE_DEBUT);
        updatedDonneesRubrique.setDonneesRubriqueDateFin(UPDATED_DONNEES_RUBRIQUE_DATE_FIN);
        updatedDonneesRubrique.setDonneesRubriquePoste(UPDATED_DONNEES_RUBRIQUE_POSTE);
        updatedDonneesRubrique.setDonneesRubriqueClient(UPDATED_DONNEES_RUBRIQUE_CLIENT);
        updatedDonneesRubrique.setDonneesRubriqueNiveaucompetence(UPDATED_DONNEES_RUBRIQUE_NIVEAUCOMPETENCE);
        updatedDonneesRubrique.setDonneesRubriqueDescription(UPDATED_DONNEES_RUBRIQUE_DESCRIPTION);

        restDonneesRubriqueMockMvc.perform(put("/api/donnees-rubriques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDonneesRubrique)))
                .andExpect(status().isOk());

        // Validate the DonneesRubrique in the database
        List<DonneesRubrique> donneesRubriques = donneesRubriqueRepository.findAll();
        assertThat(donneesRubriques).hasSize(databaseSizeBeforeUpdate);
        DonneesRubrique testDonneesRubrique = donneesRubriques.get(donneesRubriques.size() - 1);
        assertThat(testDonneesRubrique.getDonneesRubriqueTitre()).isEqualTo(UPDATED_DONNEES_RUBRIQUE_TITRE);
        assertThat(testDonneesRubrique.getDonneesRubriqueDateDebut()).isEqualTo(UPDATED_DONNEES_RUBRIQUE_DATE_DEBUT);
        assertThat(testDonneesRubrique.getDonneesRubriqueDateFin()).isEqualTo(UPDATED_DONNEES_RUBRIQUE_DATE_FIN);
        assertThat(testDonneesRubrique.getDonneesRubriquePoste()).isEqualTo(UPDATED_DONNEES_RUBRIQUE_POSTE);
        assertThat(testDonneesRubrique.getDonneesRubriqueClient()).isEqualTo(UPDATED_DONNEES_RUBRIQUE_CLIENT);
        assertThat(testDonneesRubrique.getDonneesRubriqueNiveaucompetence()).isEqualTo(UPDATED_DONNEES_RUBRIQUE_NIVEAUCOMPETENCE);
        assertThat(testDonneesRubrique.getDonneesRubriqueDescription()).isEqualTo(UPDATED_DONNEES_RUBRIQUE_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteDonneesRubrique() throws Exception {
        // Initialize the database
        donneesRubriqueRepository.saveAndFlush(donneesRubrique);
        int databaseSizeBeforeDelete = donneesRubriqueRepository.findAll().size();

        // Get the donneesRubrique
        restDonneesRubriqueMockMvc.perform(delete("/api/donnees-rubriques/{id}", donneesRubrique.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DonneesRubrique> donneesRubriques = donneesRubriqueRepository.findAll();
        assertThat(donneesRubriques).hasSize(databaseSizeBeforeDelete - 1);
    }
}
