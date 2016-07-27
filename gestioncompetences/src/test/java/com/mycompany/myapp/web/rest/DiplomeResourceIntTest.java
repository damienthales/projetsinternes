package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.GestioncompetencesApp;
import com.mycompany.myapp.domain.Diplome;
import com.mycompany.myapp.repository.DiplomeRepository;

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
 * Test class for the DiplomeResource REST controller.
 *
 * @see DiplomeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GestioncompetencesApp.class)
@WebAppConfiguration
@IntegrationTest
public class DiplomeResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_DIPLOME_NOM = "AAAAA";
    private static final String UPDATED_DIPLOME_NOM = "BBBBB";

    private static final ZonedDateTime DEFAULT_DIPLOME_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DIPLOME_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DIPLOME_DATE_STR = dateTimeFormatter.format(DEFAULT_DIPLOME_DATE);

    @Inject
    private DiplomeRepository diplomeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDiplomeMockMvc;

    private Diplome diplome;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DiplomeResource diplomeResource = new DiplomeResource();
        ReflectionTestUtils.setField(diplomeResource, "diplomeRepository", diplomeRepository);
        this.restDiplomeMockMvc = MockMvcBuilders.standaloneSetup(diplomeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        diplome = new Diplome();
        diplome.setDiplomeNom(DEFAULT_DIPLOME_NOM);
        diplome.setDiplomeDate(DEFAULT_DIPLOME_DATE);
    }

    @Test
    @Transactional
    public void createDiplome() throws Exception {
        int databaseSizeBeforeCreate = diplomeRepository.findAll().size();

        // Create the Diplome

        restDiplomeMockMvc.perform(post("/api/diplomes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(diplome)))
                .andExpect(status().isCreated());

        // Validate the Diplome in the database
        List<Diplome> diplomes = diplomeRepository.findAll();
        assertThat(diplomes).hasSize(databaseSizeBeforeCreate + 1);
        Diplome testDiplome = diplomes.get(diplomes.size() - 1);
        assertThat(testDiplome.getDiplomeNom()).isEqualTo(DEFAULT_DIPLOME_NOM);
        assertThat(testDiplome.getDiplomeDate()).isEqualTo(DEFAULT_DIPLOME_DATE);
    }

    @Test
    @Transactional
    public void checkDiplomeNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = diplomeRepository.findAll().size();
        // set the field null
        diplome.setDiplomeNom(null);

        // Create the Diplome, which fails.

        restDiplomeMockMvc.perform(post("/api/diplomes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(diplome)))
                .andExpect(status().isBadRequest());

        List<Diplome> diplomes = diplomeRepository.findAll();
        assertThat(diplomes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDiplomeDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = diplomeRepository.findAll().size();
        // set the field null
        diplome.setDiplomeDate(null);

        // Create the Diplome, which fails.

        restDiplomeMockMvc.perform(post("/api/diplomes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(diplome)))
                .andExpect(status().isBadRequest());

        List<Diplome> diplomes = diplomeRepository.findAll();
        assertThat(diplomes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDiplomes() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);

        // Get all the diplomes
        restDiplomeMockMvc.perform(get("/api/diplomes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(diplome.getId().intValue())))
                .andExpect(jsonPath("$.[*].diplomeNom").value(hasItem(DEFAULT_DIPLOME_NOM.toString())))
                .andExpect(jsonPath("$.[*].diplomeDate").value(hasItem(DEFAULT_DIPLOME_DATE_STR)));
    }

    @Test
    @Transactional
    public void getDiplome() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);

        // Get the diplome
        restDiplomeMockMvc.perform(get("/api/diplomes/{id}", diplome.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(diplome.getId().intValue()))
            .andExpect(jsonPath("$.diplomeNom").value(DEFAULT_DIPLOME_NOM.toString()))
            .andExpect(jsonPath("$.diplomeDate").value(DEFAULT_DIPLOME_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingDiplome() throws Exception {
        // Get the diplome
        restDiplomeMockMvc.perform(get("/api/diplomes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDiplome() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);
        int databaseSizeBeforeUpdate = diplomeRepository.findAll().size();

        // Update the diplome
        Diplome updatedDiplome = new Diplome();
        updatedDiplome.setId(diplome.getId());
        updatedDiplome.setDiplomeNom(UPDATED_DIPLOME_NOM);
        updatedDiplome.setDiplomeDate(UPDATED_DIPLOME_DATE);

        restDiplomeMockMvc.perform(put("/api/diplomes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDiplome)))
                .andExpect(status().isOk());

        // Validate the Diplome in the database
        List<Diplome> diplomes = diplomeRepository.findAll();
        assertThat(diplomes).hasSize(databaseSizeBeforeUpdate);
        Diplome testDiplome = diplomes.get(diplomes.size() - 1);
        assertThat(testDiplome.getDiplomeNom()).isEqualTo(UPDATED_DIPLOME_NOM);
        assertThat(testDiplome.getDiplomeDate()).isEqualTo(UPDATED_DIPLOME_DATE);
    }

    @Test
    @Transactional
    public void deleteDiplome() throws Exception {
        // Initialize the database
        diplomeRepository.saveAndFlush(diplome);
        int databaseSizeBeforeDelete = diplomeRepository.findAll().size();

        // Get the diplome
        restDiplomeMockMvc.perform(delete("/api/diplomes/{id}", diplome.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Diplome> diplomes = diplomeRepository.findAll();
        assertThat(diplomes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
