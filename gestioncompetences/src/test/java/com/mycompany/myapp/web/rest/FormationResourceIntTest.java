package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.GestioncompetencesApp;
import com.mycompany.myapp.domain.Formation;
import com.mycompany.myapp.repository.FormationRepository;

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
 * Test class for the FormationResource REST controller.
 *
 * @see FormationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GestioncompetencesApp.class)
@WebAppConfiguration
@IntegrationTest
public class FormationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_FORMATION_NOM = "AAAAA";
    private static final String UPDATED_FORMATION_NOM = "BBBBB";

    private static final ZonedDateTime DEFAULT_FORMATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FORMATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FORMATION_DATE_STR = dateTimeFormatter.format(DEFAULT_FORMATION_DATE);

    @Inject
    private FormationRepository formationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFormationMockMvc;

    private Formation formation;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FormationResource formationResource = new FormationResource();
        ReflectionTestUtils.setField(formationResource, "formationRepository", formationRepository);
        this.restFormationMockMvc = MockMvcBuilders.standaloneSetup(formationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        formation = new Formation();
        formation.setFormationNom(DEFAULT_FORMATION_NOM);
        formation.setFormationDate(DEFAULT_FORMATION_DATE);
    }

    @Test
    @Transactional
    public void createFormation() throws Exception {
        int databaseSizeBeforeCreate = formationRepository.findAll().size();

        // Create the Formation

        restFormationMockMvc.perform(post("/api/formations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(formation)))
                .andExpect(status().isCreated());

        // Validate the Formation in the database
        List<Formation> formations = formationRepository.findAll();
        assertThat(formations).hasSize(databaseSizeBeforeCreate + 1);
        Formation testFormation = formations.get(formations.size() - 1);
        assertThat(testFormation.getFormationNom()).isEqualTo(DEFAULT_FORMATION_NOM);
        assertThat(testFormation.getFormationDate()).isEqualTo(DEFAULT_FORMATION_DATE);
    }

    @Test
    @Transactional
    public void checkFormationNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationRepository.findAll().size();
        // set the field null
        formation.setFormationNom(null);

        // Create the Formation, which fails.

        restFormationMockMvc.perform(post("/api/formations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(formation)))
                .andExpect(status().isBadRequest());

        List<Formation> formations = formationRepository.findAll();
        assertThat(formations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFormationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = formationRepository.findAll().size();
        // set the field null
        formation.setFormationDate(null);

        // Create the Formation, which fails.

        restFormationMockMvc.perform(post("/api/formations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(formation)))
                .andExpect(status().isBadRequest());

        List<Formation> formations = formationRepository.findAll();
        assertThat(formations).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFormations() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get all the formations
        restFormationMockMvc.perform(get("/api/formations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(formation.getId().intValue())))
                .andExpect(jsonPath("$.[*].formationNom").value(hasItem(DEFAULT_FORMATION_NOM.toString())))
                .andExpect(jsonPath("$.[*].formationDate").value(hasItem(DEFAULT_FORMATION_DATE_STR)));
    }

    @Test
    @Transactional
    public void getFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);

        // Get the formation
        restFormationMockMvc.perform(get("/api/formations/{id}", formation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(formation.getId().intValue()))
            .andExpect(jsonPath("$.formationNom").value(DEFAULT_FORMATION_NOM.toString()))
            .andExpect(jsonPath("$.formationDate").value(DEFAULT_FORMATION_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingFormation() throws Exception {
        // Get the formation
        restFormationMockMvc.perform(get("/api/formations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);
        int databaseSizeBeforeUpdate = formationRepository.findAll().size();

        // Update the formation
        Formation updatedFormation = new Formation();
        updatedFormation.setId(formation.getId());
        updatedFormation.setFormationNom(UPDATED_FORMATION_NOM);
        updatedFormation.setFormationDate(UPDATED_FORMATION_DATE);

        restFormationMockMvc.perform(put("/api/formations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFormation)))
                .andExpect(status().isOk());

        // Validate the Formation in the database
        List<Formation> formations = formationRepository.findAll();
        assertThat(formations).hasSize(databaseSizeBeforeUpdate);
        Formation testFormation = formations.get(formations.size() - 1);
        assertThat(testFormation.getFormationNom()).isEqualTo(UPDATED_FORMATION_NOM);
        assertThat(testFormation.getFormationDate()).isEqualTo(UPDATED_FORMATION_DATE);
    }

    @Test
    @Transactional
    public void deleteFormation() throws Exception {
        // Initialize the database
        formationRepository.saveAndFlush(formation);
        int databaseSizeBeforeDelete = formationRepository.findAll().size();

        // Get the formation
        restFormationMockMvc.perform(delete("/api/formations/{id}", formation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Formation> formations = formationRepository.findAll();
        assertThat(formations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
