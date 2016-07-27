package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.GestioncompetencesApp;
import com.mycompany.myapp.domain.RubriqueRelationCV;
import com.mycompany.myapp.repository.RubriqueRelationCVRepository;

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
 * Test class for the RubriqueRelationCVResource REST controller.
 *
 * @see RubriqueRelationCVResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GestioncompetencesApp.class)
@WebAppConfiguration
@IntegrationTest
public class RubriqueRelationCVResourceIntTest {


    @Inject
    private RubriqueRelationCVRepository rubriqueRelationCVRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRubriqueRelationCVMockMvc;

    private RubriqueRelationCV rubriqueRelationCV;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RubriqueRelationCVResource rubriqueRelationCVResource = new RubriqueRelationCVResource();
        ReflectionTestUtils.setField(rubriqueRelationCVResource, "rubriqueRelationCVRepository", rubriqueRelationCVRepository);
        this.restRubriqueRelationCVMockMvc = MockMvcBuilders.standaloneSetup(rubriqueRelationCVResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        rubriqueRelationCV = new RubriqueRelationCV();
    }

    @Test
    @Transactional
    public void createRubriqueRelationCV() throws Exception {
        int databaseSizeBeforeCreate = rubriqueRelationCVRepository.findAll().size();

        // Create the RubriqueRelationCV

        restRubriqueRelationCVMockMvc.perform(post("/api/rubrique-relation-cvs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rubriqueRelationCV)))
                .andExpect(status().isCreated());

        // Validate the RubriqueRelationCV in the database
        List<RubriqueRelationCV> rubriqueRelationCVS = rubriqueRelationCVRepository.findAll();
        assertThat(rubriqueRelationCVS).hasSize(databaseSizeBeforeCreate + 1);
        RubriqueRelationCV testRubriqueRelationCV = rubriqueRelationCVS.get(rubriqueRelationCVS.size() - 1);
    }

    @Test
    @Transactional
    public void getAllRubriqueRelationCVS() throws Exception {
        // Initialize the database
        rubriqueRelationCVRepository.saveAndFlush(rubriqueRelationCV);

        // Get all the rubriqueRelationCVS
        restRubriqueRelationCVMockMvc.perform(get("/api/rubrique-relation-cvs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(rubriqueRelationCV.getId().intValue())));
    }

    @Test
    @Transactional
    public void getRubriqueRelationCV() throws Exception {
        // Initialize the database
        rubriqueRelationCVRepository.saveAndFlush(rubriqueRelationCV);

        // Get the rubriqueRelationCV
        restRubriqueRelationCVMockMvc.perform(get("/api/rubrique-relation-cvs/{id}", rubriqueRelationCV.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(rubriqueRelationCV.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRubriqueRelationCV() throws Exception {
        // Get the rubriqueRelationCV
        restRubriqueRelationCVMockMvc.perform(get("/api/rubrique-relation-cvs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRubriqueRelationCV() throws Exception {
        // Initialize the database
        rubriqueRelationCVRepository.saveAndFlush(rubriqueRelationCV);
        int databaseSizeBeforeUpdate = rubriqueRelationCVRepository.findAll().size();

        // Update the rubriqueRelationCV
        RubriqueRelationCV updatedRubriqueRelationCV = new RubriqueRelationCV();
        updatedRubriqueRelationCV.setId(rubriqueRelationCV.getId());

        restRubriqueRelationCVMockMvc.perform(put("/api/rubrique-relation-cvs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRubriqueRelationCV)))
                .andExpect(status().isOk());

        // Validate the RubriqueRelationCV in the database
        List<RubriqueRelationCV> rubriqueRelationCVS = rubriqueRelationCVRepository.findAll();
        assertThat(rubriqueRelationCVS).hasSize(databaseSizeBeforeUpdate);
        RubriqueRelationCV testRubriqueRelationCV = rubriqueRelationCVS.get(rubriqueRelationCVS.size() - 1);
    }

    @Test
    @Transactional
    public void deleteRubriqueRelationCV() throws Exception {
        // Initialize the database
        rubriqueRelationCVRepository.saveAndFlush(rubriqueRelationCV);
        int databaseSizeBeforeDelete = rubriqueRelationCVRepository.findAll().size();

        // Get the rubriqueRelationCV
        restRubriqueRelationCVMockMvc.perform(delete("/api/rubrique-relation-cvs/{id}", rubriqueRelationCV.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RubriqueRelationCV> rubriqueRelationCVS = rubriqueRelationCVRepository.findAll();
        assertThat(rubriqueRelationCVS).hasSize(databaseSizeBeforeDelete - 1);
    }
}
