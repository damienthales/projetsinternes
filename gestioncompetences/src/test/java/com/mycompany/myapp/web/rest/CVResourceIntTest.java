package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.GestioncompetencesApp;
import com.mycompany.myapp.domain.CV;
import com.mycompany.myapp.repository.CVRepository;

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
 * Test class for the CVResource REST controller.
 *
 * @see CVResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GestioncompetencesApp.class)
@WebAppConfiguration
@IntegrationTest
public class CVResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_DATE_CV = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_CV = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_CV_STR = dateTimeFormatter.format(DEFAULT_DATE_CV);

    @Inject
    private CVRepository cVRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCVMockMvc;

    private CV cV;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CVResource cVResource = new CVResource();
        ReflectionTestUtils.setField(cVResource, "cVRepository", cVRepository);
        this.restCVMockMvc = MockMvcBuilders.standaloneSetup(cVResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        cV = new CV();
        cV.setDateCv(DEFAULT_DATE_CV);
    }

    @Test
    @Transactional
    public void createCV() throws Exception {
        int databaseSizeBeforeCreate = cVRepository.findAll().size();

        // Create the CV

        restCVMockMvc.perform(post("/api/c-vs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cV)))
                .andExpect(status().isCreated());

        // Validate the CV in the database
        List<CV> cVS = cVRepository.findAll();
        assertThat(cVS).hasSize(databaseSizeBeforeCreate + 1);
        CV testCV = cVS.get(cVS.size() - 1);
        assertThat(testCV.getDateCv()).isEqualTo(DEFAULT_DATE_CV);
    }

    @Test
    @Transactional
    public void getAllCVS() throws Exception {
        // Initialize the database
        cVRepository.saveAndFlush(cV);

        // Get all the cVS
        restCVMockMvc.perform(get("/api/c-vs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cV.getId().intValue())))
                .andExpect(jsonPath("$.[*].dateCv").value(hasItem(DEFAULT_DATE_CV_STR)));
    }

    @Test
    @Transactional
    public void getCV() throws Exception {
        // Initialize the database
        cVRepository.saveAndFlush(cV);

        // Get the cV
        restCVMockMvc.perform(get("/api/c-vs/{id}", cV.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(cV.getId().intValue()))
            .andExpect(jsonPath("$.dateCv").value(DEFAULT_DATE_CV_STR));
    }

    @Test
    @Transactional
    public void getNonExistingCV() throws Exception {
        // Get the cV
        restCVMockMvc.perform(get("/api/c-vs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCV() throws Exception {
        // Initialize the database
        cVRepository.saveAndFlush(cV);
        int databaseSizeBeforeUpdate = cVRepository.findAll().size();

        // Update the cV
        CV updatedCV = new CV();
        updatedCV.setId(cV.getId());
        updatedCV.setDateCv(UPDATED_DATE_CV);

        restCVMockMvc.perform(put("/api/c-vs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCV)))
                .andExpect(status().isOk());

        // Validate the CV in the database
        List<CV> cVS = cVRepository.findAll();
        assertThat(cVS).hasSize(databaseSizeBeforeUpdate);
        CV testCV = cVS.get(cVS.size() - 1);
        assertThat(testCV.getDateCv()).isEqualTo(UPDATED_DATE_CV);
    }

    @Test
    @Transactional
    public void deleteCV() throws Exception {
        // Initialize the database
        cVRepository.saveAndFlush(cV);
        int databaseSizeBeforeDelete = cVRepository.findAll().size();

        // Get the cV
        restCVMockMvc.perform(delete("/api/c-vs/{id}", cV.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CV> cVS = cVRepository.findAll();
        assertThat(cVS).hasSize(databaseSizeBeforeDelete - 1);
    }
}
