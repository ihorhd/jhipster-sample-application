package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.Source;
import com.mycompany.myapp.repository.SourceRepository;
import com.mycompany.myapp.service.SourceService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SourceResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class SourceResourceIT {

    private static final String DEFAULT_SOURCE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_COST_USD = 1L;
    private static final Long UPDATED_COST_USD = 2L;

    private static final Long DEFAULT_COST_ARS = 1L;
    private static final Long UPDATED_COST_ARS = 2L;

    private static final Instant DEFAULT_COST_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COST_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SourceRepository sourceRepository;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSourceMockMvc;

    private Source source;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Source createEntity(EntityManager em) {
        Source source = new Source()
            .sourceName(DEFAULT_SOURCE_NAME)
            .costUSD(DEFAULT_COST_USD)
            .costARS(DEFAULT_COST_ARS)
            .costDate(DEFAULT_COST_DATE);
        return source;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Source createUpdatedEntity(EntityManager em) {
        Source source = new Source()
            .sourceName(UPDATED_SOURCE_NAME)
            .costUSD(UPDATED_COST_USD)
            .costARS(UPDATED_COST_ARS)
            .costDate(UPDATED_COST_DATE);
        return source;
    }

    @BeforeEach
    public void initTest() {
        source = createEntity(em);
    }

    @Test
    @Transactional
    public void createSource() throws Exception {
        int databaseSizeBeforeCreate = sourceRepository.findAll().size();

        // Create the Source
        restSourceMockMvc.perform(post("/api/sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(source)))
            .andExpect(status().isCreated());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeCreate + 1);
        Source testSource = sourceList.get(sourceList.size() - 1);
        assertThat(testSource.getSourceName()).isEqualTo(DEFAULT_SOURCE_NAME);
        assertThat(testSource.getCostUSD()).isEqualTo(DEFAULT_COST_USD);
        assertThat(testSource.getCostARS()).isEqualTo(DEFAULT_COST_ARS);
        assertThat(testSource.getCostDate()).isEqualTo(DEFAULT_COST_DATE);
    }

    @Test
    @Transactional
    public void createSourceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sourceRepository.findAll().size();

        // Create the Source with an existing ID
        source.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSourceMockMvc.perform(post("/api/sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(source)))
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSources() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get all the sourceList
        restSourceMockMvc.perform(get("/api/sources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(source.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceName").value(hasItem(DEFAULT_SOURCE_NAME)))
            .andExpect(jsonPath("$.[*].costUSD").value(hasItem(DEFAULT_COST_USD.intValue())))
            .andExpect(jsonPath("$.[*].costARS").value(hasItem(DEFAULT_COST_ARS.intValue())))
            .andExpect(jsonPath("$.[*].costDate").value(hasItem(DEFAULT_COST_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getSource() throws Exception {
        // Initialize the database
        sourceRepository.saveAndFlush(source);

        // Get the source
        restSourceMockMvc.perform(get("/api/sources/{id}", source.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(source.getId().intValue()))
            .andExpect(jsonPath("$.sourceName").value(DEFAULT_SOURCE_NAME))
            .andExpect(jsonPath("$.costUSD").value(DEFAULT_COST_USD.intValue()))
            .andExpect(jsonPath("$.costARS").value(DEFAULT_COST_ARS.intValue()))
            .andExpect(jsonPath("$.costDate").value(DEFAULT_COST_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSource() throws Exception {
        // Get the source
        restSourceMockMvc.perform(get("/api/sources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSource() throws Exception {
        // Initialize the database
        sourceService.save(source);

        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();

        // Update the source
        Source updatedSource = sourceRepository.findById(source.getId()).get();
        // Disconnect from session so that the updates on updatedSource are not directly saved in db
        em.detach(updatedSource);
        updatedSource
            .sourceName(UPDATED_SOURCE_NAME)
            .costUSD(UPDATED_COST_USD)
            .costARS(UPDATED_COST_ARS)
            .costDate(UPDATED_COST_DATE);

        restSourceMockMvc.perform(put("/api/sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSource)))
            .andExpect(status().isOk());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
        Source testSource = sourceList.get(sourceList.size() - 1);
        assertThat(testSource.getSourceName()).isEqualTo(UPDATED_SOURCE_NAME);
        assertThat(testSource.getCostUSD()).isEqualTo(UPDATED_COST_USD);
        assertThat(testSource.getCostARS()).isEqualTo(UPDATED_COST_ARS);
        assertThat(testSource.getCostDate()).isEqualTo(UPDATED_COST_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingSource() throws Exception {
        int databaseSizeBeforeUpdate = sourceRepository.findAll().size();

        // Create the Source

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSourceMockMvc.perform(put("/api/sources")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(source)))
            .andExpect(status().isBadRequest());

        // Validate the Source in the database
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSource() throws Exception {
        // Initialize the database
        sourceService.save(source);

        int databaseSizeBeforeDelete = sourceRepository.findAll().size();

        // Delete the source
        restSourceMockMvc.perform(delete("/api/sources/{id}", source.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Source> sourceList = sourceRepository.findAll();
        assertThat(sourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
