package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.GestioncompetencesApp;
import com.mycompany.myapp.domain.Competence;
import com.mycompany.myapp.repository.CompetenceRepository;

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

import com.mycompany.myapp.domain.enumeration.NiveauCompetence;

/**
 * Test class for the CompetenceResource REST controller.
 *
 * @see CompetenceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GestioncompetencesApp.class)
@WebAppConfiguration
@IntegrationTest
public class CompetenceResourceIntTest {

    private static final String DEFAULT_COMPETENCE_LIBELLE = "AAAAA";
    private static final String UPDATED_COMPETENCE_LIBELLE = "BBBBB";

    private static final Long DEFAULT_COMPETENCE_ANNEESEXPERIENCES = 1L;
    private static final Long UPDATED_COMPETENCE_ANNEESEXPERIENCES = 2L;
    private static final String DEFAULT_COMPETENCE_CLIENT = "AAAAA";
    private static final String UPDATED_COMPETENCE_CLIENT = "BBBBB";

    private static final NiveauCompetence DEFAULT_COMPETENCE_NIVEAUCOMPETENCE = NiveauCompetence.DECOUVERTE;
    private static final NiveauCompetence UPDATED_COMPETENCE_NIVEAUCOMPETENCE = NiveauCompetence.OPERATIONNELLE;

    @Inject
    private CompetenceRepository competenceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCompetenceMockMvc;

    private Competence competence;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompetenceResource competenceResource = new CompetenceResource();
        ReflectionTestUtils.setField(competenceResource, "competenceRepository", competenceRepository);
        this.restCompetenceMockMvc = MockMvcBuilders.standaloneSetup(competenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        competence = new Competence();
        competence.setCompetenceLibelle(DEFAULT_COMPETENCE_LIBELLE);
        competence.setCompetenceAnneesexperiences(DEFAULT_COMPETENCE_ANNEESEXPERIENCES);
        competence.setCompetenceClient(DEFAULT_COMPETENCE_CLIENT);
        competence.setCompetenceNiveauCompetence(DEFAULT_COMPETENCE_NIVEAUCOMPETENCE);
    }

    @Test
    @Transactional
    public void createCompetence() throws Exception {
        int databaseSizeBeforeCreate = competenceRepository.findAll().size();

        // Create the Competence

        restCompetenceMockMvc.perform(post("/api/competences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(competence)))
                .andExpect(status().isCreated());

        // Validate the Competence in the database
        List<Competence> competences = competenceRepository.findAll();
        assertThat(competences).hasSize(databaseSizeBeforeCreate + 1);
        Competence testCompetence = competences.get(competences.size() - 1);
        assertThat(testCompetence.getCompetenceLibelle()).isEqualTo(DEFAULT_COMPETENCE_LIBELLE);
        assertThat(testCompetence.getCompetenceAnneesexperiences()).isEqualTo(DEFAULT_COMPETENCE_ANNEESEXPERIENCES);
        assertThat(testCompetence.getCompetenceClient()).isEqualTo(DEFAULT_COMPETENCE_CLIENT);
        assertThat(testCompetence.getCompetenceNiveauCompetence()).isEqualTo(DEFAULT_COMPETENCE_NIVEAUCOMPETENCE);
    }

    @Test
    @Transactional
    public void getAllCompetences() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get all the competences
        restCompetenceMockMvc.perform(get("/api/competences?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(competence.getId().intValue())))
                .andExpect(jsonPath("$.[*].competenceLibelle").value(hasItem(DEFAULT_COMPETENCE_LIBELLE.toString())))
                .andExpect(jsonPath("$.[*].competenceAnneesexperiences").value(hasItem(DEFAULT_COMPETENCE_ANNEESEXPERIENCES.intValue())))
                .andExpect(jsonPath("$.[*].competenceClient").value(hasItem(DEFAULT_COMPETENCE_CLIENT.toString())))
                .andExpect(jsonPath("$.[*].competenceNiveauCompetence").value(hasItem(DEFAULT_COMPETENCE_NIVEAUCOMPETENCE.toString())));
    }

    @Test
    @Transactional
    public void getCompetence() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);

        // Get the competence
        restCompetenceMockMvc.perform(get("/api/competences/{id}", competence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(competence.getId().intValue()))
            .andExpect(jsonPath("$.competenceLibelle").value(DEFAULT_COMPETENCE_LIBELLE.toString()))
            .andExpect(jsonPath("$.competenceAnneesexperiences").value(DEFAULT_COMPETENCE_ANNEESEXPERIENCES.intValue()))
            .andExpect(jsonPath("$.competenceClient").value(DEFAULT_COMPETENCE_CLIENT.toString()))
            .andExpect(jsonPath("$.competenceNiveauCompetence").value(DEFAULT_COMPETENCE_NIVEAUCOMPETENCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCompetence() throws Exception {
        // Get the competence
        restCompetenceMockMvc.perform(get("/api/competences/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompetence() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);
        int databaseSizeBeforeUpdate = competenceRepository.findAll().size();

        // Update the competence
        Competence updatedCompetence = new Competence();
        updatedCompetence.setId(competence.getId());
        updatedCompetence.setCompetenceLibelle(UPDATED_COMPETENCE_LIBELLE);
        updatedCompetence.setCompetenceAnneesexperiences(UPDATED_COMPETENCE_ANNEESEXPERIENCES);
        updatedCompetence.setCompetenceClient(UPDATED_COMPETENCE_CLIENT);
        updatedCompetence.setCompetenceNiveauCompetence(UPDATED_COMPETENCE_NIVEAUCOMPETENCE);

        restCompetenceMockMvc.perform(put("/api/competences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCompetence)))
                .andExpect(status().isOk());

        // Validate the Competence in the database
        List<Competence> competences = competenceRepository.findAll();
        assertThat(competences).hasSize(databaseSizeBeforeUpdate);
        Competence testCompetence = competences.get(competences.size() - 1);
        assertThat(testCompetence.getCompetenceLibelle()).isEqualTo(UPDATED_COMPETENCE_LIBELLE);
        assertThat(testCompetence.getCompetenceAnneesexperiences()).isEqualTo(UPDATED_COMPETENCE_ANNEESEXPERIENCES);
        assertThat(testCompetence.getCompetenceClient()).isEqualTo(UPDATED_COMPETENCE_CLIENT);
        assertThat(testCompetence.getCompetenceNiveauCompetence()).isEqualTo(UPDATED_COMPETENCE_NIVEAUCOMPETENCE);
    }

    @Test
    @Transactional
    public void deleteCompetence() throws Exception {
        // Initialize the database
        competenceRepository.saveAndFlush(competence);
        int databaseSizeBeforeDelete = competenceRepository.findAll().size();

        // Get the competence
        restCompetenceMockMvc.perform(delete("/api/competences/{id}", competence.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Competence> competences = competenceRepository.findAll();
        assertThat(competences).hasSize(databaseSizeBeforeDelete - 1);
    }
}
