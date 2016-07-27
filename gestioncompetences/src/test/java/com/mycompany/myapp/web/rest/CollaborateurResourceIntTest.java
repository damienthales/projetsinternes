package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.GestioncompetencesApp;
import com.mycompany.myapp.domain.Collaborateur;
import com.mycompany.myapp.repository.CollaborateurRepository;

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
import org.springframework.util.Base64Utils;

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

import com.mycompany.myapp.domain.enumeration.Sexe;
import com.mycompany.myapp.domain.enumeration.EtatMarital;
import com.mycompany.myapp.domain.enumeration.Langue;

/**
 * Test class for the CollaborateurResource REST controller.
 *
 * @see CollaborateurResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GestioncompetencesApp.class)
@WebAppConfiguration
@IntegrationTest
public class CollaborateurResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_COLLABORATEUR_NOM = "AAAAA";
    private static final String UPDATED_COLLABORATEUR_NOM = "BBBBB";
    private static final String DEFAULT_COLLABORATEUR_PRENOM = "AAAAA";
    private static final String UPDATED_COLLABORATEUR_PRENOM = "BBBBB";

    private static final ZonedDateTime DEFAULT_COLLABORATEUR_DATE_NAISSANCE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_COLLABORATEUR_DATE_NAISSANCE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_COLLABORATEUR_DATE_NAISSANCE_STR = dateTimeFormatter.format(DEFAULT_COLLABORATEUR_DATE_NAISSANCE);

    private static final Sexe DEFAULT_COLLABORATEUR_SEXE = Sexe.MASCULIN;
    private static final Sexe UPDATED_COLLABORATEUR_SEXE = Sexe.FEMININ;

    private static final EtatMarital DEFAULT_COLLABORATEUR_ETAT_MARITAL = EtatMarital.CELIBATAIRE;
    private static final EtatMarital UPDATED_COLLABORATEUR_ETAT_MARITAL = EtatMarital.PACSE;

    private static final Integer DEFAULT_COLLABORATEUR_NOMBRE_ENFANT = 1;
    private static final Integer UPDATED_COLLABORATEUR_NOMBRE_ENFANT = 2;

    private static final ZonedDateTime DEFAULT_COLLABORATEUR_DATE_ARRIVEE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_COLLABORATEUR_DATE_ARRIVEE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_COLLABORATEUR_DATE_ARRIVEE_STR = dateTimeFormatter.format(DEFAULT_COLLABORATEUR_DATE_ARRIVEE);

    private static final byte[] DEFAULT_COLLABORATEUR_PHOTOS = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_COLLABORATEUR_PHOTOS = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_COLLABORATEUR_PHOTOS_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_COLLABORATEUR_PHOTOS_CONTENT_TYPE = "image/png";

    private static final Langue DEFAULT_COLLABORATEUR_LANGUE = Langue.FRANCAIS;
    private static final Langue UPDATED_COLLABORATEUR_LANGUE = Langue.ANGLAIS;

    @Inject
    private CollaborateurRepository collaborateurRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCollaborateurMockMvc;

    private Collaborateur collaborateur;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CollaborateurResource collaborateurResource = new CollaborateurResource();
        ReflectionTestUtils.setField(collaborateurResource, "collaborateurRepository", collaborateurRepository);
        this.restCollaborateurMockMvc = MockMvcBuilders.standaloneSetup(collaborateurResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        collaborateur = new Collaborateur();
        collaborateur.setCollaborateurNom(DEFAULT_COLLABORATEUR_NOM);
        collaborateur.setCollaborateurPrenom(DEFAULT_COLLABORATEUR_PRENOM);
        collaborateur.setCollaborateurDateNaissance(DEFAULT_COLLABORATEUR_DATE_NAISSANCE);
        collaborateur.setCollaborateurSexe(DEFAULT_COLLABORATEUR_SEXE);
        collaborateur.setCollaborateurEtatMarital(DEFAULT_COLLABORATEUR_ETAT_MARITAL);
        collaborateur.setCollaborateurNombreEnfant(DEFAULT_COLLABORATEUR_NOMBRE_ENFANT);
        collaborateur.setCollaborateurDateArrivee(DEFAULT_COLLABORATEUR_DATE_ARRIVEE);
        collaborateur.setCollaborateurPhotos(DEFAULT_COLLABORATEUR_PHOTOS);
        collaborateur.setCollaborateurPhotosContentType(DEFAULT_COLLABORATEUR_PHOTOS_CONTENT_TYPE);
        collaborateur.setCollaborateurLangue(DEFAULT_COLLABORATEUR_LANGUE);
    }

    @Test
    @Transactional
    public void createCollaborateur() throws Exception {
        int databaseSizeBeforeCreate = collaborateurRepository.findAll().size();

        // Create the Collaborateur

        restCollaborateurMockMvc.perform(post("/api/collaborateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(collaborateur)))
                .andExpect(status().isCreated());

        // Validate the Collaborateur in the database
        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        assertThat(collaborateurs).hasSize(databaseSizeBeforeCreate + 1);
        Collaborateur testCollaborateur = collaborateurs.get(collaborateurs.size() - 1);
        assertThat(testCollaborateur.getCollaborateurNom()).isEqualTo(DEFAULT_COLLABORATEUR_NOM);
        assertThat(testCollaborateur.getCollaborateurPrenom()).isEqualTo(DEFAULT_COLLABORATEUR_PRENOM);
        assertThat(testCollaborateur.getCollaborateurDateNaissance()).isEqualTo(DEFAULT_COLLABORATEUR_DATE_NAISSANCE);
        assertThat(testCollaborateur.getCollaborateurSexe()).isEqualTo(DEFAULT_COLLABORATEUR_SEXE);
        assertThat(testCollaborateur.getCollaborateurEtatMarital()).isEqualTo(DEFAULT_COLLABORATEUR_ETAT_MARITAL);
        assertThat(testCollaborateur.getCollaborateurNombreEnfant()).isEqualTo(DEFAULT_COLLABORATEUR_NOMBRE_ENFANT);
        assertThat(testCollaborateur.getCollaborateurDateArrivee()).isEqualTo(DEFAULT_COLLABORATEUR_DATE_ARRIVEE);
        assertThat(testCollaborateur.getCollaborateurPhotos()).isEqualTo(DEFAULT_COLLABORATEUR_PHOTOS);
        assertThat(testCollaborateur.getCollaborateurPhotosContentType()).isEqualTo(DEFAULT_COLLABORATEUR_PHOTOS_CONTENT_TYPE);
        assertThat(testCollaborateur.getCollaborateurLangue()).isEqualTo(DEFAULT_COLLABORATEUR_LANGUE);
    }

    @Test
    @Transactional
    public void checkCollaborateurNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = collaborateurRepository.findAll().size();
        // set the field null
        collaborateur.setCollaborateurNom(null);

        // Create the Collaborateur, which fails.

        restCollaborateurMockMvc.perform(post("/api/collaborateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(collaborateur)))
                .andExpect(status().isBadRequest());

        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        assertThat(collaborateurs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCollaborateurPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = collaborateurRepository.findAll().size();
        // set the field null
        collaborateur.setCollaborateurPrenom(null);

        // Create the Collaborateur, which fails.

        restCollaborateurMockMvc.perform(post("/api/collaborateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(collaborateur)))
                .andExpect(status().isBadRequest());

        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        assertThat(collaborateurs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCollaborateurDateNaissanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = collaborateurRepository.findAll().size();
        // set the field null
        collaborateur.setCollaborateurDateNaissance(null);

        // Create the Collaborateur, which fails.

        restCollaborateurMockMvc.perform(post("/api/collaborateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(collaborateur)))
                .andExpect(status().isBadRequest());

        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        assertThat(collaborateurs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCollaborateurSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = collaborateurRepository.findAll().size();
        // set the field null
        collaborateur.setCollaborateurSexe(null);

        // Create the Collaborateur, which fails.

        restCollaborateurMockMvc.perform(post("/api/collaborateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(collaborateur)))
                .andExpect(status().isBadRequest());

        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        assertThat(collaborateurs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCollaborateurEtatMaritalIsRequired() throws Exception {
        int databaseSizeBeforeTest = collaborateurRepository.findAll().size();
        // set the field null
        collaborateur.setCollaborateurEtatMarital(null);

        // Create the Collaborateur, which fails.

        restCollaborateurMockMvc.perform(post("/api/collaborateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(collaborateur)))
                .andExpect(status().isBadRequest());

        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        assertThat(collaborateurs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCollaborateurDateArriveeIsRequired() throws Exception {
        int databaseSizeBeforeTest = collaborateurRepository.findAll().size();
        // set the field null
        collaborateur.setCollaborateurDateArrivee(null);

        // Create the Collaborateur, which fails.

        restCollaborateurMockMvc.perform(post("/api/collaborateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(collaborateur)))
                .andExpect(status().isBadRequest());

        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        assertThat(collaborateurs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCollaborateurs() throws Exception {
        // Initialize the database
        collaborateurRepository.saveAndFlush(collaborateur);

        // Get all the collaborateurs
        restCollaborateurMockMvc.perform(get("/api/collaborateurs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(collaborateur.getId().intValue())))
                .andExpect(jsonPath("$.[*].collaborateurNom").value(hasItem(DEFAULT_COLLABORATEUR_NOM.toString())))
                .andExpect(jsonPath("$.[*].collaborateurPrenom").value(hasItem(DEFAULT_COLLABORATEUR_PRENOM.toString())))
                .andExpect(jsonPath("$.[*].collaborateurDateNaissance").value(hasItem(DEFAULT_COLLABORATEUR_DATE_NAISSANCE_STR)))
                .andExpect(jsonPath("$.[*].collaborateurSexe").value(hasItem(DEFAULT_COLLABORATEUR_SEXE.toString())))
                .andExpect(jsonPath("$.[*].collaborateurEtatMarital").value(hasItem(DEFAULT_COLLABORATEUR_ETAT_MARITAL.toString())))
                .andExpect(jsonPath("$.[*].collaborateurNombreEnfant").value(hasItem(DEFAULT_COLLABORATEUR_NOMBRE_ENFANT)))
                .andExpect(jsonPath("$.[*].collaborateurDateArrivee").value(hasItem(DEFAULT_COLLABORATEUR_DATE_ARRIVEE_STR)))
                .andExpect(jsonPath("$.[*].collaborateurPhotosContentType").value(hasItem(DEFAULT_COLLABORATEUR_PHOTOS_CONTENT_TYPE)))
                .andExpect(jsonPath("$.[*].collaborateurPhotos").value(hasItem(Base64Utils.encodeToString(DEFAULT_COLLABORATEUR_PHOTOS))))
                .andExpect(jsonPath("$.[*].collaborateurLangue").value(hasItem(DEFAULT_COLLABORATEUR_LANGUE.toString())));
    }

    @Test
    @Transactional
    public void getCollaborateur() throws Exception {
        // Initialize the database
        collaborateurRepository.saveAndFlush(collaborateur);

        // Get the collaborateur
        restCollaborateurMockMvc.perform(get("/api/collaborateurs/{id}", collaborateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(collaborateur.getId().intValue()))
            .andExpect(jsonPath("$.collaborateurNom").value(DEFAULT_COLLABORATEUR_NOM.toString()))
            .andExpect(jsonPath("$.collaborateurPrenom").value(DEFAULT_COLLABORATEUR_PRENOM.toString()))
            .andExpect(jsonPath("$.collaborateurDateNaissance").value(DEFAULT_COLLABORATEUR_DATE_NAISSANCE_STR))
            .andExpect(jsonPath("$.collaborateurSexe").value(DEFAULT_COLLABORATEUR_SEXE.toString()))
            .andExpect(jsonPath("$.collaborateurEtatMarital").value(DEFAULT_COLLABORATEUR_ETAT_MARITAL.toString()))
            .andExpect(jsonPath("$.collaborateurNombreEnfant").value(DEFAULT_COLLABORATEUR_NOMBRE_ENFANT))
            .andExpect(jsonPath("$.collaborateurDateArrivee").value(DEFAULT_COLLABORATEUR_DATE_ARRIVEE_STR))
            .andExpect(jsonPath("$.collaborateurPhotosContentType").value(DEFAULT_COLLABORATEUR_PHOTOS_CONTENT_TYPE))
            .andExpect(jsonPath("$.collaborateurPhotos").value(Base64Utils.encodeToString(DEFAULT_COLLABORATEUR_PHOTOS)))
            .andExpect(jsonPath("$.collaborateurLangue").value(DEFAULT_COLLABORATEUR_LANGUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCollaborateur() throws Exception {
        // Get the collaborateur
        restCollaborateurMockMvc.perform(get("/api/collaborateurs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCollaborateur() throws Exception {
        // Initialize the database
        collaborateurRepository.saveAndFlush(collaborateur);
        int databaseSizeBeforeUpdate = collaborateurRepository.findAll().size();

        // Update the collaborateur
        Collaborateur updatedCollaborateur = new Collaborateur();
        updatedCollaborateur.setId(collaborateur.getId());
        updatedCollaborateur.setCollaborateurNom(UPDATED_COLLABORATEUR_NOM);
        updatedCollaborateur.setCollaborateurPrenom(UPDATED_COLLABORATEUR_PRENOM);
        updatedCollaborateur.setCollaborateurDateNaissance(UPDATED_COLLABORATEUR_DATE_NAISSANCE);
        updatedCollaborateur.setCollaborateurSexe(UPDATED_COLLABORATEUR_SEXE);
        updatedCollaborateur.setCollaborateurEtatMarital(UPDATED_COLLABORATEUR_ETAT_MARITAL);
        updatedCollaborateur.setCollaborateurNombreEnfant(UPDATED_COLLABORATEUR_NOMBRE_ENFANT);
        updatedCollaborateur.setCollaborateurDateArrivee(UPDATED_COLLABORATEUR_DATE_ARRIVEE);
        updatedCollaborateur.setCollaborateurPhotos(UPDATED_COLLABORATEUR_PHOTOS);
        updatedCollaborateur.setCollaborateurPhotosContentType(UPDATED_COLLABORATEUR_PHOTOS_CONTENT_TYPE);
        updatedCollaborateur.setCollaborateurLangue(UPDATED_COLLABORATEUR_LANGUE);

        restCollaborateurMockMvc.perform(put("/api/collaborateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCollaborateur)))
                .andExpect(status().isOk());

        // Validate the Collaborateur in the database
        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        assertThat(collaborateurs).hasSize(databaseSizeBeforeUpdate);
        Collaborateur testCollaborateur = collaborateurs.get(collaborateurs.size() - 1);
        assertThat(testCollaborateur.getCollaborateurNom()).isEqualTo(UPDATED_COLLABORATEUR_NOM);
        assertThat(testCollaborateur.getCollaborateurPrenom()).isEqualTo(UPDATED_COLLABORATEUR_PRENOM);
        assertThat(testCollaborateur.getCollaborateurDateNaissance()).isEqualTo(UPDATED_COLLABORATEUR_DATE_NAISSANCE);
        assertThat(testCollaborateur.getCollaborateurSexe()).isEqualTo(UPDATED_COLLABORATEUR_SEXE);
        assertThat(testCollaborateur.getCollaborateurEtatMarital()).isEqualTo(UPDATED_COLLABORATEUR_ETAT_MARITAL);
        assertThat(testCollaborateur.getCollaborateurNombreEnfant()).isEqualTo(UPDATED_COLLABORATEUR_NOMBRE_ENFANT);
        assertThat(testCollaborateur.getCollaborateurDateArrivee()).isEqualTo(UPDATED_COLLABORATEUR_DATE_ARRIVEE);
        assertThat(testCollaborateur.getCollaborateurPhotos()).isEqualTo(UPDATED_COLLABORATEUR_PHOTOS);
        assertThat(testCollaborateur.getCollaborateurPhotosContentType()).isEqualTo(UPDATED_COLLABORATEUR_PHOTOS_CONTENT_TYPE);
        assertThat(testCollaborateur.getCollaborateurLangue()).isEqualTo(UPDATED_COLLABORATEUR_LANGUE);
    }

    @Test
    @Transactional
    public void deleteCollaborateur() throws Exception {
        // Initialize the database
        collaborateurRepository.saveAndFlush(collaborateur);
        int databaseSizeBeforeDelete = collaborateurRepository.findAll().size();

        // Get the collaborateur
        restCollaborateurMockMvc.perform(delete("/api/collaborateurs/{id}", collaborateur.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Collaborateur> collaborateurs = collaborateurRepository.findAll();
        assertThat(collaborateurs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
