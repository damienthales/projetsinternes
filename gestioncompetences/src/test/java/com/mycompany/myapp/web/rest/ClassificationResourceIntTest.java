package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.GestioncompetencesApp;
import com.mycompany.myapp.domain.Classification;
import com.mycompany.myapp.repository.ClassificationRepository;

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
 * Test class for the ClassificationResource REST controller.
 *
 * @see ClassificationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GestioncompetencesApp.class)
@WebAppConfiguration
@IntegrationTest
public class ClassificationResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_CLASSIFICATION_NOM = "AAAAA";
    private static final String UPDATED_CLASSIFICATION_NOM = "BBBBB";

    private static final ZonedDateTime DEFAULT_CLASSIFICATION_DATE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CLASSIFICATION_DATE_DEBUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CLASSIFICATION_DATE_DEBUT_STR = dateTimeFormatter.format(DEFAULT_CLASSIFICATION_DATE_DEBUT);

    private static final ZonedDateTime DEFAULT_CLASSIFICATION_DATE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CLASSIFICATION_DATE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CLASSIFICATION_DATE_FIN_STR = dateTimeFormatter.format(DEFAULT_CLASSIFICATION_DATE_FIN);

    @Inject
    private ClassificationRepository classificationRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restClassificationMockMvc;

    private Classification classification;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClassificationResource classificationResource = new ClassificationResource();
        ReflectionTestUtils.setField(classificationResource, "classificationRepository", classificationRepository);
        this.restClassificationMockMvc = MockMvcBuilders.standaloneSetup(classificationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        classification = new Classification();
        classification.setClassificationNom(DEFAULT_CLASSIFICATION_NOM);
        classification.setClassificationDateDebut(DEFAULT_CLASSIFICATION_DATE_DEBUT);
        classification.setClassificationDateFin(DEFAULT_CLASSIFICATION_DATE_FIN);
    }

    @Test
    @Transactional
    public void createClassification() throws Exception {
        int databaseSizeBeforeCreate = classificationRepository.findAll().size();

        // Create the Classification

        restClassificationMockMvc.perform(post("/api/classifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classification)))
                .andExpect(status().isCreated());

        // Validate the Classification in the database
        List<Classification> classifications = classificationRepository.findAll();
        assertThat(classifications).hasSize(databaseSizeBeforeCreate + 1);
        Classification testClassification = classifications.get(classifications.size() - 1);
        assertThat(testClassification.getClassificationNom()).isEqualTo(DEFAULT_CLASSIFICATION_NOM);
        assertThat(testClassification.getClassificationDateDebut()).isEqualTo(DEFAULT_CLASSIFICATION_DATE_DEBUT);
        assertThat(testClassification.getClassificationDateFin()).isEqualTo(DEFAULT_CLASSIFICATION_DATE_FIN);
    }

    @Test
    @Transactional
    public void checkClassificationNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = classificationRepository.findAll().size();
        // set the field null
        classification.setClassificationNom(null);

        // Create the Classification, which fails.

        restClassificationMockMvc.perform(post("/api/classifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classification)))
                .andExpect(status().isBadRequest());

        List<Classification> classifications = classificationRepository.findAll();
        assertThat(classifications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClassificationDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = classificationRepository.findAll().size();
        // set the field null
        classification.setClassificationDateDebut(null);

        // Create the Classification, which fails.

        restClassificationMockMvc.perform(post("/api/classifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classification)))
                .andExpect(status().isBadRequest());

        List<Classification> classifications = classificationRepository.findAll();
        assertThat(classifications).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClassifications() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        // Get all the classifications
        restClassificationMockMvc.perform(get("/api/classifications?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(classification.getId().intValue())))
                .andExpect(jsonPath("$.[*].classificationNom").value(hasItem(DEFAULT_CLASSIFICATION_NOM.toString())))
                .andExpect(jsonPath("$.[*].classificationDateDebut").value(hasItem(DEFAULT_CLASSIFICATION_DATE_DEBUT_STR)))
                .andExpect(jsonPath("$.[*].classificationDateFin").value(hasItem(DEFAULT_CLASSIFICATION_DATE_FIN_STR)));
    }

    @Test
    @Transactional
    public void getClassification() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        // Get the classification
        restClassificationMockMvc.perform(get("/api/classifications/{id}", classification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(classification.getId().intValue()))
            .andExpect(jsonPath("$.classificationNom").value(DEFAULT_CLASSIFICATION_NOM.toString()))
            .andExpect(jsonPath("$.classificationDateDebut").value(DEFAULT_CLASSIFICATION_DATE_DEBUT_STR))
            .andExpect(jsonPath("$.classificationDateFin").value(DEFAULT_CLASSIFICATION_DATE_FIN_STR));
    }

    @Test
    @Transactional
    public void getNonExistingClassification() throws Exception {
        // Get the classification
        restClassificationMockMvc.perform(get("/api/classifications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassification() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);
        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();

        // Update the classification
        Classification updatedClassification = new Classification();
        updatedClassification.setId(classification.getId());
        updatedClassification.setClassificationNom(UPDATED_CLASSIFICATION_NOM);
        updatedClassification.setClassificationDateDebut(UPDATED_CLASSIFICATION_DATE_DEBUT);
        updatedClassification.setClassificationDateFin(UPDATED_CLASSIFICATION_DATE_FIN);

        restClassificationMockMvc.perform(put("/api/classifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedClassification)))
                .andExpect(status().isOk());

        // Validate the Classification in the database
        List<Classification> classifications = classificationRepository.findAll();
        assertThat(classifications).hasSize(databaseSizeBeforeUpdate);
        Classification testClassification = classifications.get(classifications.size() - 1);
        assertThat(testClassification.getClassificationNom()).isEqualTo(UPDATED_CLASSIFICATION_NOM);
        assertThat(testClassification.getClassificationDateDebut()).isEqualTo(UPDATED_CLASSIFICATION_DATE_DEBUT);
        assertThat(testClassification.getClassificationDateFin()).isEqualTo(UPDATED_CLASSIFICATION_DATE_FIN);
    }

    @Test
    @Transactional
    public void deleteClassification() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);
        int databaseSizeBeforeDelete = classificationRepository.findAll().size();

        // Get the classification
        restClassificationMockMvc.perform(delete("/api/classifications/{id}", classification.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Classification> classifications = classificationRepository.findAll();
        assertThat(classifications).hasSize(databaseSizeBeforeDelete - 1);
    }
}
