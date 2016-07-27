package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.GestioncompetencesApp;
import com.mycompany.myapp.domain.Rubrique;
import com.mycompany.myapp.repository.RubriqueRepository;

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

import com.mycompany.myapp.domain.enumeration.TypeRubrique;

/**
 * Test class for the RubriqueResource REST controller.
 *
 * @see RubriqueResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GestioncompetencesApp.class)
@WebAppConfiguration
@IntegrationTest
public class RubriqueResourceIntTest {

    private static final String DEFAULT_RUBRIQUE_LIBELLE = "AAAAA";
    private static final String UPDATED_RUBRIQUE_LIBELLE = "BBBBB";

    private static final TypeRubrique DEFAULT_RUBRIQUE_TYPE_RUBRIQUE = TypeRubrique.COMPETENCE;
    private static final TypeRubrique UPDATED_RUBRIQUE_TYPE_RUBRIQUE = TypeRubrique.PROFESSIONNELLE;

    private static final Boolean DEFAULT_RUBRIQUE_OBLIGATOIRE = false;
    private static final Boolean UPDATED_RUBRIQUE_OBLIGATOIRE = true;

    @Inject
    private RubriqueRepository rubriqueRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRubriqueMockMvc;

    private Rubrique rubrique;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RubriqueResource rubriqueResource = new RubriqueResource();
        ReflectionTestUtils.setField(rubriqueResource, "rubriqueRepository", rubriqueRepository);
        this.restRubriqueMockMvc = MockMvcBuilders.standaloneSetup(rubriqueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        rubrique = new Rubrique();
        rubrique.setRubriqueLibelle(DEFAULT_RUBRIQUE_LIBELLE);
        rubrique.setRubriqueTypeRubrique(DEFAULT_RUBRIQUE_TYPE_RUBRIQUE);
        rubrique.setRubriqueObligatoire(DEFAULT_RUBRIQUE_OBLIGATOIRE);
    }

    @Test
    @Transactional
    public void createRubrique() throws Exception {
        int databaseSizeBeforeCreate = rubriqueRepository.findAll().size();

        // Create the Rubrique

        restRubriqueMockMvc.perform(post("/api/rubriques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rubrique)))
                .andExpect(status().isCreated());

        // Validate the Rubrique in the database
        List<Rubrique> rubriques = rubriqueRepository.findAll();
        assertThat(rubriques).hasSize(databaseSizeBeforeCreate + 1);
        Rubrique testRubrique = rubriques.get(rubriques.size() - 1);
        assertThat(testRubrique.getRubriqueLibelle()).isEqualTo(DEFAULT_RUBRIQUE_LIBELLE);
        assertThat(testRubrique.getRubriqueTypeRubrique()).isEqualTo(DEFAULT_RUBRIQUE_TYPE_RUBRIQUE);
        assertThat(testRubrique.isRubriqueObligatoire()).isEqualTo(DEFAULT_RUBRIQUE_OBLIGATOIRE);
    }

    @Test
    @Transactional
    public void checkRubriqueLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = rubriqueRepository.findAll().size();
        // set the field null
        rubrique.setRubriqueLibelle(null);

        // Create the Rubrique, which fails.

        restRubriqueMockMvc.perform(post("/api/rubriques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rubrique)))
                .andExpect(status().isBadRequest());

        List<Rubrique> rubriques = rubriqueRepository.findAll();
        assertThat(rubriques).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRubriques() throws Exception {
        // Initialize the database
        rubriqueRepository.saveAndFlush(rubrique);

        // Get all the rubriques
        restRubriqueMockMvc.perform(get("/api/rubriques?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(rubrique.getId().intValue())))
                .andExpect(jsonPath("$.[*].rubriqueLibelle").value(hasItem(DEFAULT_RUBRIQUE_LIBELLE.toString())))
                .andExpect(jsonPath("$.[*].rubriqueTypeRubrique").value(hasItem(DEFAULT_RUBRIQUE_TYPE_RUBRIQUE.toString())))
                .andExpect(jsonPath("$.[*].rubriqueObligatoire").value(hasItem(DEFAULT_RUBRIQUE_OBLIGATOIRE.booleanValue())));
    }

    @Test
    @Transactional
    public void getRubrique() throws Exception {
        // Initialize the database
        rubriqueRepository.saveAndFlush(rubrique);

        // Get the rubrique
        restRubriqueMockMvc.perform(get("/api/rubriques/{id}", rubrique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(rubrique.getId().intValue()))
            .andExpect(jsonPath("$.rubriqueLibelle").value(DEFAULT_RUBRIQUE_LIBELLE.toString()))
            .andExpect(jsonPath("$.rubriqueTypeRubrique").value(DEFAULT_RUBRIQUE_TYPE_RUBRIQUE.toString()))
            .andExpect(jsonPath("$.rubriqueObligatoire").value(DEFAULT_RUBRIQUE_OBLIGATOIRE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRubrique() throws Exception {
        // Get the rubrique
        restRubriqueMockMvc.perform(get("/api/rubriques/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRubrique() throws Exception {
        // Initialize the database
        rubriqueRepository.saveAndFlush(rubrique);
        int databaseSizeBeforeUpdate = rubriqueRepository.findAll().size();

        // Update the rubrique
        Rubrique updatedRubrique = new Rubrique();
        updatedRubrique.setId(rubrique.getId());
        updatedRubrique.setRubriqueLibelle(UPDATED_RUBRIQUE_LIBELLE);
        updatedRubrique.setRubriqueTypeRubrique(UPDATED_RUBRIQUE_TYPE_RUBRIQUE);
        updatedRubrique.setRubriqueObligatoire(UPDATED_RUBRIQUE_OBLIGATOIRE);

        restRubriqueMockMvc.perform(put("/api/rubriques")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRubrique)))
                .andExpect(status().isOk());

        // Validate the Rubrique in the database
        List<Rubrique> rubriques = rubriqueRepository.findAll();
        assertThat(rubriques).hasSize(databaseSizeBeforeUpdate);
        Rubrique testRubrique = rubriques.get(rubriques.size() - 1);
        assertThat(testRubrique.getRubriqueLibelle()).isEqualTo(UPDATED_RUBRIQUE_LIBELLE);
        assertThat(testRubrique.getRubriqueTypeRubrique()).isEqualTo(UPDATED_RUBRIQUE_TYPE_RUBRIQUE);
        assertThat(testRubrique.isRubriqueObligatoire()).isEqualTo(UPDATED_RUBRIQUE_OBLIGATOIRE);
    }

    @Test
    @Transactional
    public void deleteRubrique() throws Exception {
        // Initialize the database
        rubriqueRepository.saveAndFlush(rubrique);
        int databaseSizeBeforeDelete = rubriqueRepository.findAll().size();

        // Get the rubrique
        restRubriqueMockMvc.perform(delete("/api/rubriques/{id}", rubrique.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Rubrique> rubriques = rubriqueRepository.findAll();
        assertThat(rubriques).hasSize(databaseSizeBeforeDelete - 1);
    }
}
