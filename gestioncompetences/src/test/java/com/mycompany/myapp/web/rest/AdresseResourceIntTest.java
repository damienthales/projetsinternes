package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.GestioncompetencesApp;
import com.mycompany.myapp.domain.Adresse;
import com.mycompany.myapp.repository.AdresseRepository;

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
 * Test class for the AdresseResource REST controller.
 *
 * @see AdresseResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GestioncompetencesApp.class)
@WebAppConfiguration
@IntegrationTest
public class AdresseResourceIntTest {

    private static final String DEFAULT_ADRESSE_NUMERO = "AAAAA";
    private static final String UPDATED_ADRESSE_NUMERO = "BBBBB";
    private static final String DEFAULT_ADRESSE_VOIE = "AAAAA";
    private static final String UPDATED_ADRESSE_VOIE = "BBBBB";
    private static final String DEFAULT_ADRESSE_VOIE_2 = "AAAAA";
    private static final String UPDATED_ADRESSE_VOIE_2 = "BBBBB";
    private static final String DEFAULT_ADRESSE_CODE_POSTAL = "AAAAA";
    private static final String UPDATED_ADRESSE_CODE_POSTAL = "BBBBB";
    private static final String DEFAULT_ADRESSE_VILLE = "AAAAA";
    private static final String UPDATED_ADRESSE_VILLE = "BBBBB";
    private static final String DEFAULT_ADRESSE_PAYS = "AAAAA";
    private static final String UPDATED_ADRESSE_PAYS = "BBBBB";

    @Inject
    private AdresseRepository adresseRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAdresseMockMvc;

    private Adresse adresse;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdresseResource adresseResource = new AdresseResource();
        ReflectionTestUtils.setField(adresseResource, "adresseRepository", adresseRepository);
        this.restAdresseMockMvc = MockMvcBuilders.standaloneSetup(adresseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        adresse = new Adresse();
        adresse.setAdresseNumero(DEFAULT_ADRESSE_NUMERO);
        adresse.setAdresseVoie(DEFAULT_ADRESSE_VOIE);
        adresse.setAdresseVoie2(DEFAULT_ADRESSE_VOIE_2);
        adresse.setAdresseCodePostal(DEFAULT_ADRESSE_CODE_POSTAL);
        adresse.setAdresseVille(DEFAULT_ADRESSE_VILLE);
        adresse.setAdressePays(DEFAULT_ADRESSE_PAYS);
    }

    @Test
    @Transactional
    public void createAdresse() throws Exception {
        int databaseSizeBeforeCreate = adresseRepository.findAll().size();

        // Create the Adresse

        restAdresseMockMvc.perform(post("/api/adresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adresse)))
                .andExpect(status().isCreated());

        // Validate the Adresse in the database
        List<Adresse> adresses = adresseRepository.findAll();
        assertThat(adresses).hasSize(databaseSizeBeforeCreate + 1);
        Adresse testAdresse = adresses.get(adresses.size() - 1);
        assertThat(testAdresse.getAdresseNumero()).isEqualTo(DEFAULT_ADRESSE_NUMERO);
        assertThat(testAdresse.getAdresseVoie()).isEqualTo(DEFAULT_ADRESSE_VOIE);
        assertThat(testAdresse.getAdresseVoie2()).isEqualTo(DEFAULT_ADRESSE_VOIE_2);
        assertThat(testAdresse.getAdresseCodePostal()).isEqualTo(DEFAULT_ADRESSE_CODE_POSTAL);
        assertThat(testAdresse.getAdresseVille()).isEqualTo(DEFAULT_ADRESSE_VILLE);
        assertThat(testAdresse.getAdressePays()).isEqualTo(DEFAULT_ADRESSE_PAYS);
    }

    @Test
    @Transactional
    public void checkAdresseNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = adresseRepository.findAll().size();
        // set the field null
        adresse.setAdresseNumero(null);

        // Create the Adresse, which fails.

        restAdresseMockMvc.perform(post("/api/adresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adresse)))
                .andExpect(status().isBadRequest());

        List<Adresse> adresses = adresseRepository.findAll();
        assertThat(adresses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdresseVoieIsRequired() throws Exception {
        int databaseSizeBeforeTest = adresseRepository.findAll().size();
        // set the field null
        adresse.setAdresseVoie(null);

        // Create the Adresse, which fails.

        restAdresseMockMvc.perform(post("/api/adresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adresse)))
                .andExpect(status().isBadRequest());

        List<Adresse> adresses = adresseRepository.findAll();
        assertThat(adresses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdresseCodePostalIsRequired() throws Exception {
        int databaseSizeBeforeTest = adresseRepository.findAll().size();
        // set the field null
        adresse.setAdresseCodePostal(null);

        // Create the Adresse, which fails.

        restAdresseMockMvc.perform(post("/api/adresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adresse)))
                .andExpect(status().isBadRequest());

        List<Adresse> adresses = adresseRepository.findAll();
        assertThat(adresses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdresseVilleIsRequired() throws Exception {
        int databaseSizeBeforeTest = adresseRepository.findAll().size();
        // set the field null
        adresse.setAdresseVille(null);

        // Create the Adresse, which fails.

        restAdresseMockMvc.perform(post("/api/adresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(adresse)))
                .andExpect(status().isBadRequest());

        List<Adresse> adresses = adresseRepository.findAll();
        assertThat(adresses).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAdresses() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get all the adresses
        restAdresseMockMvc.perform(get("/api/adresses?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(adresse.getId().intValue())))
                .andExpect(jsonPath("$.[*].adresseNumero").value(hasItem(DEFAULT_ADRESSE_NUMERO.toString())))
                .andExpect(jsonPath("$.[*].adresseVoie").value(hasItem(DEFAULT_ADRESSE_VOIE.toString())))
                .andExpect(jsonPath("$.[*].adresseVoie2").value(hasItem(DEFAULT_ADRESSE_VOIE_2.toString())))
                .andExpect(jsonPath("$.[*].adresseCodePostal").value(hasItem(DEFAULT_ADRESSE_CODE_POSTAL.toString())))
                .andExpect(jsonPath("$.[*].adresseVille").value(hasItem(DEFAULT_ADRESSE_VILLE.toString())))
                .andExpect(jsonPath("$.[*].adressePays").value(hasItem(DEFAULT_ADRESSE_PAYS.toString())));
    }

    @Test
    @Transactional
    public void getAdresse() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);

        // Get the adresse
        restAdresseMockMvc.perform(get("/api/adresses/{id}", adresse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(adresse.getId().intValue()))
            .andExpect(jsonPath("$.adresseNumero").value(DEFAULT_ADRESSE_NUMERO.toString()))
            .andExpect(jsonPath("$.adresseVoie").value(DEFAULT_ADRESSE_VOIE.toString()))
            .andExpect(jsonPath("$.adresseVoie2").value(DEFAULT_ADRESSE_VOIE_2.toString()))
            .andExpect(jsonPath("$.adresseCodePostal").value(DEFAULT_ADRESSE_CODE_POSTAL.toString()))
            .andExpect(jsonPath("$.adresseVille").value(DEFAULT_ADRESSE_VILLE.toString()))
            .andExpect(jsonPath("$.adressePays").value(DEFAULT_ADRESSE_PAYS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAdresse() throws Exception {
        // Get the adresse
        restAdresseMockMvc.perform(get("/api/adresses/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdresse() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);
        int databaseSizeBeforeUpdate = adresseRepository.findAll().size();

        // Update the adresse
        Adresse updatedAdresse = new Adresse();
        updatedAdresse.setId(adresse.getId());
        updatedAdresse.setAdresseNumero(UPDATED_ADRESSE_NUMERO);
        updatedAdresse.setAdresseVoie(UPDATED_ADRESSE_VOIE);
        updatedAdresse.setAdresseVoie2(UPDATED_ADRESSE_VOIE_2);
        updatedAdresse.setAdresseCodePostal(UPDATED_ADRESSE_CODE_POSTAL);
        updatedAdresse.setAdresseVille(UPDATED_ADRESSE_VILLE);
        updatedAdresse.setAdressePays(UPDATED_ADRESSE_PAYS);

        restAdresseMockMvc.perform(put("/api/adresses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAdresse)))
                .andExpect(status().isOk());

        // Validate the Adresse in the database
        List<Adresse> adresses = adresseRepository.findAll();
        assertThat(adresses).hasSize(databaseSizeBeforeUpdate);
        Adresse testAdresse = adresses.get(adresses.size() - 1);
        assertThat(testAdresse.getAdresseNumero()).isEqualTo(UPDATED_ADRESSE_NUMERO);
        assertThat(testAdresse.getAdresseVoie()).isEqualTo(UPDATED_ADRESSE_VOIE);
        assertThat(testAdresse.getAdresseVoie2()).isEqualTo(UPDATED_ADRESSE_VOIE_2);
        assertThat(testAdresse.getAdresseCodePostal()).isEqualTo(UPDATED_ADRESSE_CODE_POSTAL);
        assertThat(testAdresse.getAdresseVille()).isEqualTo(UPDATED_ADRESSE_VILLE);
        assertThat(testAdresse.getAdressePays()).isEqualTo(UPDATED_ADRESSE_PAYS);
    }

    @Test
    @Transactional
    public void deleteAdresse() throws Exception {
        // Initialize the database
        adresseRepository.saveAndFlush(adresse);
        int databaseSizeBeforeDelete = adresseRepository.findAll().size();

        // Get the adresse
        restAdresseMockMvc.perform(delete("/api/adresses/{id}", adresse.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Adresse> adresses = adresseRepository.findAll();
        assertThat(adresses).hasSize(databaseSizeBeforeDelete - 1);
    }
}
