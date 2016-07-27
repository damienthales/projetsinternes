package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.GestioncompetencesApp;
import com.mycompany.myapp.domain.DonneesCollaborateur;
import com.mycompany.myapp.repository.DonneesCollaborateurRepository;

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
 * Test class for the DonneesCollaborateurResource REST controller.
 *
 * @see DonneesCollaborateurResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GestioncompetencesApp.class)
@WebAppConfiguration
@IntegrationTest
public class DonneesCollaborateurResourceIntTest {

    private static final String DEFAULT_DESCRIPTION_RUBRIQUE_COLLABORATEUR = "AAAAA";
    private static final String UPDATED_DESCRIPTION_RUBRIQUE_COLLABORATEUR = "BBBBB";

    @Inject
    private DonneesCollaborateurRepository donneesCollaborateurRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDonneesCollaborateurMockMvc;

    private DonneesCollaborateur donneesCollaborateur;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DonneesCollaborateurResource donneesCollaborateurResource = new DonneesCollaborateurResource();
        ReflectionTestUtils.setField(donneesCollaborateurResource, "donneesCollaborateurRepository", donneesCollaborateurRepository);
        this.restDonneesCollaborateurMockMvc = MockMvcBuilders.standaloneSetup(donneesCollaborateurResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        donneesCollaborateur = new DonneesCollaborateur();
        donneesCollaborateur.setDescriptionRubriqueCollaborateur(DEFAULT_DESCRIPTION_RUBRIQUE_COLLABORATEUR);
    }

    @Test
    @Transactional
    public void createDonneesCollaborateur() throws Exception {
        int databaseSizeBeforeCreate = donneesCollaborateurRepository.findAll().size();

        // Create the DonneesCollaborateur

        restDonneesCollaborateurMockMvc.perform(post("/api/donnees-collaborateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(donneesCollaborateur)))
                .andExpect(status().isCreated());

        // Validate the DonneesCollaborateur in the database
        List<DonneesCollaborateur> donneesCollaborateurs = donneesCollaborateurRepository.findAll();
        assertThat(donneesCollaborateurs).hasSize(databaseSizeBeforeCreate + 1);
        DonneesCollaborateur testDonneesCollaborateur = donneesCollaborateurs.get(donneesCollaborateurs.size() - 1);
        assertThat(testDonneesCollaborateur.getDescriptionRubriqueCollaborateur()).isEqualTo(DEFAULT_DESCRIPTION_RUBRIQUE_COLLABORATEUR);
    }

    @Test
    @Transactional
    public void getAllDonneesCollaborateurs() throws Exception {
        // Initialize the database
        donneesCollaborateurRepository.saveAndFlush(donneesCollaborateur);

        // Get all the donneesCollaborateurs
        restDonneesCollaborateurMockMvc.perform(get("/api/donnees-collaborateurs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(donneesCollaborateur.getId().intValue())))
                .andExpect(jsonPath("$.[*].descriptionRubriqueCollaborateur").value(hasItem(DEFAULT_DESCRIPTION_RUBRIQUE_COLLABORATEUR.toString())));
    }

    @Test
    @Transactional
    public void getDonneesCollaborateur() throws Exception {
        // Initialize the database
        donneesCollaborateurRepository.saveAndFlush(donneesCollaborateur);

        // Get the donneesCollaborateur
        restDonneesCollaborateurMockMvc.perform(get("/api/donnees-collaborateurs/{id}", donneesCollaborateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(donneesCollaborateur.getId().intValue()))
            .andExpect(jsonPath("$.descriptionRubriqueCollaborateur").value(DEFAULT_DESCRIPTION_RUBRIQUE_COLLABORATEUR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDonneesCollaborateur() throws Exception {
        // Get the donneesCollaborateur
        restDonneesCollaborateurMockMvc.perform(get("/api/donnees-collaborateurs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDonneesCollaborateur() throws Exception {
        // Initialize the database
        donneesCollaborateurRepository.saveAndFlush(donneesCollaborateur);
        int databaseSizeBeforeUpdate = donneesCollaborateurRepository.findAll().size();

        // Update the donneesCollaborateur
        DonneesCollaborateur updatedDonneesCollaborateur = new DonneesCollaborateur();
        updatedDonneesCollaborateur.setId(donneesCollaborateur.getId());
        updatedDonneesCollaborateur.setDescriptionRubriqueCollaborateur(UPDATED_DESCRIPTION_RUBRIQUE_COLLABORATEUR);

        restDonneesCollaborateurMockMvc.perform(put("/api/donnees-collaborateurs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDonneesCollaborateur)))
                .andExpect(status().isOk());

        // Validate the DonneesCollaborateur in the database
        List<DonneesCollaborateur> donneesCollaborateurs = donneesCollaborateurRepository.findAll();
        assertThat(donneesCollaborateurs).hasSize(databaseSizeBeforeUpdate);
        DonneesCollaborateur testDonneesCollaborateur = donneesCollaborateurs.get(donneesCollaborateurs.size() - 1);
        assertThat(testDonneesCollaborateur.getDescriptionRubriqueCollaborateur()).isEqualTo(UPDATED_DESCRIPTION_RUBRIQUE_COLLABORATEUR);
    }

    @Test
    @Transactional
    public void deleteDonneesCollaborateur() throws Exception {
        // Initialize the database
        donneesCollaborateurRepository.saveAndFlush(donneesCollaborateur);
        int databaseSizeBeforeDelete = donneesCollaborateurRepository.findAll().size();

        // Get the donneesCollaborateur
        restDonneesCollaborateurMockMvc.perform(delete("/api/donnees-collaborateurs/{id}", donneesCollaborateur.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DonneesCollaborateur> donneesCollaborateurs = donneesCollaborateurRepository.findAll();
        assertThat(donneesCollaborateurs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
