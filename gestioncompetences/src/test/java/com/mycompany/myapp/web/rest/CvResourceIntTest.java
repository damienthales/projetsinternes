package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.GestioncompetencesApp;
import com.mycompany.myapp.domain.Cv;
import com.mycompany.myapp.repository.CvRepository;

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
 * Test class for the CvResource REST controller.
 *
 * @see CvResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GestioncompetencesApp.class)
@WebAppConfiguration
@IntegrationTest
public class CvResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_DATE_CV = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_CV = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_CV_STR = dateTimeFormatter.format(DEFAULT_DATE_CV);
    private static final String DEFAULT_LIBELLE = "AAAAA";
    private static final String UPDATED_LIBELLE = "BBBBB";

    @Inject
    private CvRepository cvRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCvMockMvc;

    private Cv cv;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CvResource cvResource = new CvResource();
        ReflectionTestUtils.setField(cvResource, "cvRepository", cvRepository);
        this.restCvMockMvc = MockMvcBuilders.standaloneSetup(cvResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        cv = new Cv();
        cv.setCvDate(DEFAULT_DATE_CV);
        cv.setCvLibelle(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createCv() throws Exception {
        int databaseSizeBeforeCreate = cvRepository.findAll().size();

        // Create the Cv

        restCvMockMvc.perform(post("/api/cvs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cv)))
                .andExpect(status().isCreated());

        // Validate the Cv in the database
        List<Cv> cvs = cvRepository.findAll();
        assertThat(cvs).hasSize(databaseSizeBeforeCreate + 1);
        Cv testCv = cvs.get(cvs.size() - 1);
        assertThat(testCv.getCvDate()).isEqualTo(DEFAULT_DATE_CV);
        assertThat(testCv.getCvLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void getAllCvs() throws Exception {
        // Initialize the database
        cvRepository.saveAndFlush(cv);

        // Get all the cvs
        restCvMockMvc.perform(get("/api/cvs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cv.getId().intValue())))
                .andExpect(jsonPath("$.[*].cvDate").value(hasItem(DEFAULT_DATE_CV_STR)))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void getCv() throws Exception {
        // Initialize the database
        cvRepository.saveAndFlush(cv);

        // Get the cv
        restCvMockMvc.perform(get("/api/cvs/{id}", cv.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(cv.getId().intValue()))
            .andExpect(jsonPath("$.cvDate").value(DEFAULT_DATE_CV_STR))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCv() throws Exception {
        // Get the cv
        restCvMockMvc.perform(get("/api/cvs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCv() throws Exception {
        // Initialize the database
        cvRepository.saveAndFlush(cv);
        int databaseSizeBeforeUpdate = cvRepository.findAll().size();

        // Update the cv
        Cv updatedCv = new Cv();
        updatedCv.setId(cv.getId());
        updatedCv.setCvDate(UPDATED_DATE_CV);
        updatedCv.setCvLibelle(UPDATED_LIBELLE);

        restCvMockMvc.perform(put("/api/cvs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCv)))
                .andExpect(status().isOk());

        // Validate the Cv in the database
        List<Cv> cvs = cvRepository.findAll();
        assertThat(cvs).hasSize(databaseSizeBeforeUpdate);
        Cv testCv = cvs.get(cvs.size() - 1);
        assertThat(testCv.getCvDate()).isEqualTo(UPDATED_DATE_CV);
        assertThat(testCv.getCvLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    public void deleteCv() throws Exception {
        // Initialize the database
        cvRepository.saveAndFlush(cv);
        int databaseSizeBeforeDelete = cvRepository.findAll().size();

        // Get the cv
        restCvMockMvc.perform(delete("/api/cvs/{id}", cv.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Cv> cvs = cvRepository.findAll();
        assertThat(cvs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
