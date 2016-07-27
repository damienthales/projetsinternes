package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.GestioncompetencesApp;
import com.mycompany.myapp.domain.Experience;
import com.mycompany.myapp.repository.ExperienceRepository;

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

import com.mycompany.myapp.domain.enumeration.TypeExperience;

/**
 * Test class for the ExperienceResource REST controller.
 *
 * @see ExperienceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GestioncompetencesApp.class)
@WebAppConfiguration
@IntegrationTest
public class ExperienceResourceIntTest {

    private static final String DEFAULT_EXPERIENCE_NOM = "AAAAA";
    private static final String UPDATED_EXPERIENCE_NOM = "BBBBB";

    private static final TypeExperience DEFAULT_EXPERIENCE_TYPE_EXPERIENCE = TypeExperience.PRO;
    private static final TypeExperience UPDATED_EXPERIENCE_TYPE_EXPERIENCE = TypeExperience.EXTRA;

    @Inject
    private ExperienceRepository experienceRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restExperienceMockMvc;

    private Experience experience;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExperienceResource experienceResource = new ExperienceResource();
        ReflectionTestUtils.setField(experienceResource, "experienceRepository", experienceRepository);
        this.restExperienceMockMvc = MockMvcBuilders.standaloneSetup(experienceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        experience = new Experience();
        experience.setExperienceNom(DEFAULT_EXPERIENCE_NOM);
        experience.setExperienceTypeExperience(DEFAULT_EXPERIENCE_TYPE_EXPERIENCE);
    }

    @Test
    @Transactional
    public void createExperience() throws Exception {
        int databaseSizeBeforeCreate = experienceRepository.findAll().size();

        // Create the Experience

        restExperienceMockMvc.perform(post("/api/experiences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(experience)))
                .andExpect(status().isCreated());

        // Validate the Experience in the database
        List<Experience> experiences = experienceRepository.findAll();
        assertThat(experiences).hasSize(databaseSizeBeforeCreate + 1);
        Experience testExperience = experiences.get(experiences.size() - 1);
        assertThat(testExperience.getExperienceNom()).isEqualTo(DEFAULT_EXPERIENCE_NOM);
        assertThat(testExperience.getExperienceTypeExperience()).isEqualTo(DEFAULT_EXPERIENCE_TYPE_EXPERIENCE);
    }

    @Test
    @Transactional
    public void checkExperienceNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = experienceRepository.findAll().size();
        // set the field null
        experience.setExperienceNom(null);

        // Create the Experience, which fails.

        restExperienceMockMvc.perform(post("/api/experiences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(experience)))
                .andExpect(status().isBadRequest());

        List<Experience> experiences = experienceRepository.findAll();
        assertThat(experiences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExperiences() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get all the experiences
        restExperienceMockMvc.perform(get("/api/experiences?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(experience.getId().intValue())))
                .andExpect(jsonPath("$.[*].experienceNom").value(hasItem(DEFAULT_EXPERIENCE_NOM.toString())))
                .andExpect(jsonPath("$.[*].experienceTypeExperience").value(hasItem(DEFAULT_EXPERIENCE_TYPE_EXPERIENCE.toString())));
    }

    @Test
    @Transactional
    public void getExperience() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);

        // Get the experience
        restExperienceMockMvc.perform(get("/api/experiences/{id}", experience.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(experience.getId().intValue()))
            .andExpect(jsonPath("$.experienceNom").value(DEFAULT_EXPERIENCE_NOM.toString()))
            .andExpect(jsonPath("$.experienceTypeExperience").value(DEFAULT_EXPERIENCE_TYPE_EXPERIENCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExperience() throws Exception {
        // Get the experience
        restExperienceMockMvc.perform(get("/api/experiences/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExperience() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);
        int databaseSizeBeforeUpdate = experienceRepository.findAll().size();

        // Update the experience
        Experience updatedExperience = new Experience();
        updatedExperience.setId(experience.getId());
        updatedExperience.setExperienceNom(UPDATED_EXPERIENCE_NOM);
        updatedExperience.setExperienceTypeExperience(UPDATED_EXPERIENCE_TYPE_EXPERIENCE);

        restExperienceMockMvc.perform(put("/api/experiences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedExperience)))
                .andExpect(status().isOk());

        // Validate the Experience in the database
        List<Experience> experiences = experienceRepository.findAll();
        assertThat(experiences).hasSize(databaseSizeBeforeUpdate);
        Experience testExperience = experiences.get(experiences.size() - 1);
        assertThat(testExperience.getExperienceNom()).isEqualTo(UPDATED_EXPERIENCE_NOM);
        assertThat(testExperience.getExperienceTypeExperience()).isEqualTo(UPDATED_EXPERIENCE_TYPE_EXPERIENCE);
    }

    @Test
    @Transactional
    public void deleteExperience() throws Exception {
        // Initialize the database
        experienceRepository.saveAndFlush(experience);
        int databaseSizeBeforeDelete = experienceRepository.findAll().size();

        // Get the experience
        restExperienceMockMvc.perform(delete("/api/experiences/{id}", experience.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Experience> experiences = experienceRepository.findAll();
        assertThat(experiences).hasSize(databaseSizeBeforeDelete - 1);
    }
}
