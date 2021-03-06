package com.yalday.app.web.rest;

import com.yalday.app.FinalYaldayMerchantApp;
import com.yalday.app.domain.Merchant;
import com.yalday.app.domain.dto.MerchantDTO;
import com.yalday.app.repository.MerchantRepository;
import com.yalday.app.repository.search.MerchantSearchRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MerchantResource REST controller.
 *
 * @see MerchantResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinalYaldayMerchantApp.class)
public class MerchantResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_LINE_OF_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_LINE_OF_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_SECOND_LINE_OF_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_SECOND_LINE_OF_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_POSTCODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTCODE = "BBBBBBBBBB";

    private static final String DEFAULT_FACEBOOK = "AAAAAAAAAA";
    private static final String UPDATED_FACEBOOK = "BBBBBBBBBB";

    private static final String DEFAULT_INSTAGRAM = "AAAAAAAAAA";
    private static final String UPDATED_INSTAGRAM = "BBBBBBBBBB";

    private static final String DEFAULT_DATE_CREATED_STR = DateTimeFormatter.ISO_INSTANT.format(ZonedDateTime.now(Clock.systemDefaultZone()));

    private static final String DEFAULT_LAST_EDITED_STR = DateTimeFormatter.ISO_INSTANT.format(ZonedDateTime.now(Clock.systemDefaultZone()));

    @Inject
    private MerchantRepository merchantRepository;

    @Inject
    private MerchantSearchRepository merchantSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMerchantMockMvc;

    private MerchantDTO merchant;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MerchantResource merchantResource = new MerchantResource();
        ReflectionTestUtils.setField(merchantResource, "merchantSearchRepository", merchantSearchRepository);
        ReflectionTestUtils.setField(merchantResource, "merchantRepository", merchantRepository);
        this.restMerchantMockMvc = MockMvcBuilders.standaloneSetup(merchantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MerchantDTO createEntity() {
        return MerchantDTO.builder()
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .description(DEFAULT_DESCRIPTION)
            .logo(DEFAULT_LOGO)
            .firstLineOfAddress(DEFAULT_FIRST_LINE_OF_ADDRESS)
            .secondLineOfAddress(DEFAULT_SECOND_LINE_OF_ADDRESS)
            .city(DEFAULT_CITY)
            .postcode(DEFAULT_POSTCODE)
            .facebook(DEFAULT_FACEBOOK)
            .instagram(DEFAULT_INSTAGRAM)
            .build();
    }

    @Before
    public void initTest() {
        merchantSearchRepository.deleteAll();
        merchant = createEntity();
    }

    @Test
    @Transactional
    public void createMerchant() throws Exception {
        int databaseSizeBeforeCreate = merchantRepository.findAll().size();

        // Create the Merchant

        Timestamp before = Timestamp.from(Instant.now(Clock.systemDefaultZone()));
        restMerchantMockMvc.perform(post("/api/merchants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(merchant)))
            .andExpect(status().isCreated());
        Timestamp after = Timestamp.from(Instant.now(Clock.systemDefaultZone()));

        // Validate the Merchant in the database
        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeCreate + 1);
        Merchant testMerchant = merchants.get(merchants.size() - 1);
        assertThat(testMerchant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMerchant.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMerchant.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMerchant.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testMerchant.getFirstLineOfAddress()).isEqualTo(DEFAULT_FIRST_LINE_OF_ADDRESS);
        assertThat(testMerchant.getSecondLineOfAddress()).isEqualTo(DEFAULT_SECOND_LINE_OF_ADDRESS);
        assertThat(testMerchant.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testMerchant.getPostcode()).isEqualTo(DEFAULT_POSTCODE);
        assertThat(testMerchant.getFacebook()).isEqualTo(DEFAULT_FACEBOOK);
        assertThat(testMerchant.getInstagram()).isEqualTo(DEFAULT_INSTAGRAM);
        assertThat(testMerchant.getDateCreated()).isBetween(before, after, true, true);
        assertThat(testMerchant.getLastEdited()).isBetween(before, after, true, true);

        // Validate the Merchant in ElasticSearch
        Merchant merchantEs = merchantSearchRepository.findOne(testMerchant.getId());
        assertThat(merchantEs).isEqualToComparingFieldByField(testMerchant);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setName(null);

        // Create the Merchant, which fails.

        restMerchantMockMvc.perform(post("/api/merchants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(merchant)))
            .andExpect(status().isBadRequest());

        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setEmail(null);

        // Create the Merchant, which fails.

        restMerchantMockMvc.perform(post("/api/merchants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(merchant)))
            .andExpect(status().isBadRequest());

        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setDescription(null);

        // Create the Merchant, which fails.

        restMerchantMockMvc.perform(post("/api/merchants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(merchant)))
            .andExpect(status().isBadRequest());

        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPostcodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setPostcode(null);

        // Create the Merchant, which fails.

        restMerchantMockMvc.perform(post("/api/merchants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(merchant)))
            .andExpect(status().isBadRequest());

        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMerchants() throws Exception {
        // Initialize the database
        Merchant saved = merchantRepository.saveAndFlush(merchant.toMerchant());

        // Get all the merchants
        restMerchantMockMvc.perform(get("/api/merchants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saved.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.[*].firstLineOfAddress").value(hasItem(DEFAULT_FIRST_LINE_OF_ADDRESS)))
            .andExpect(jsonPath("$.[*].secondLineOfAddress").value(hasItem(DEFAULT_SECOND_LINE_OF_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].postcode").value(hasItem(DEFAULT_POSTCODE)))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK)))
            .andExpect(jsonPath("$.[*].instagram").value(hasItem(DEFAULT_INSTAGRAM)));
    }

    @Test
    @Transactional
    public void getMerchant() throws Exception {
        // Initialize the database
        Merchant saved = merchantRepository.saveAndFlush(merchant.toMerchant());

        // Get the merchant
        restMerchantMockMvc.perform(get("/api/merchants/{id}", saved.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(saved.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO.toString()))
            .andExpect(jsonPath("$.firstLineOfAddress").value(DEFAULT_FIRST_LINE_OF_ADDRESS.toString()))
            .andExpect(jsonPath("$.secondLineOfAddress").value(DEFAULT_SECOND_LINE_OF_ADDRESS.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.postcode").value(DEFAULT_POSTCODE.toString()))
            .andExpect(jsonPath("$.facebook").value(DEFAULT_FACEBOOK.toString()))
            .andExpect(jsonPath("$.instagram").value(DEFAULT_INSTAGRAM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMerchant() throws Exception {
        // Get the merchant
        restMerchantMockMvc.perform(get("/api/merchants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMerchant() throws Exception {
        // Initialize the database
        Merchant saved = merchantRepository.saveAndFlush(merchant.toMerchant());
        merchantSearchRepository.save(saved);
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();

        // Update the merchant
        MerchantDTO updatedMerchant = MerchantDTO.builder()
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .description(UPDATED_DESCRIPTION)
            .logo(UPDATED_LOGO)
            .firstLineOfAddress(UPDATED_FIRST_LINE_OF_ADDRESS)
            .secondLineOfAddress(UPDATED_SECOND_LINE_OF_ADDRESS)
            .city(UPDATED_CITY)
            .postcode(UPDATED_POSTCODE)
            .facebook(UPDATED_FACEBOOK)
            .instagram(UPDATED_INSTAGRAM)
            .build();

        Thread.sleep(50); // so the last edited time is different

        Timestamp updateTime = Timestamp.from(Instant.now(Clock.systemDefaultZone()));
        restMerchantMockMvc.perform(post("/api/merchants/{id}", saved.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMerchant)))
            .andExpect(status().isOk());

        // Validate the Merchant in the database
        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeUpdate);
        Merchant testMerchant = merchants.get(merchants.size() - 1);
        assertThat(testMerchant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMerchant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMerchant.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMerchant.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testMerchant.getFirstLineOfAddress()).isEqualTo(UPDATED_FIRST_LINE_OF_ADDRESS);
        assertThat(testMerchant.getSecondLineOfAddress()).isEqualTo(UPDATED_SECOND_LINE_OF_ADDRESS);
        assertThat(testMerchant.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testMerchant.getPostcode()).isEqualTo(UPDATED_POSTCODE);
        assertThat(testMerchant.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
        assertThat(testMerchant.getInstagram()).isEqualTo(UPDATED_INSTAGRAM);
        assertThat(testMerchant.getDateCreated()).isEqualTo(saved.getDateCreated());
        assertThat(testMerchant.getLastEdited()).isAfter(updateTime);

        // Validate the Merchant in ElasticSearch
        Merchant merchantEs = merchantSearchRepository.findOne(testMerchant.getId());
        assertThat(merchantEs).isEqualToComparingFieldByField(testMerchant);
    }

    @Test
    @Transactional
    public void deleteMerchant() throws Exception {
        // Initialize the database
        Merchant saved = merchantRepository.saveAndFlush(merchant.toMerchant());
        merchantSearchRepository.save(saved);
        int databaseSizeBeforeDelete = merchantRepository.findAll().size();

        // Get the merchant
        restMerchantMockMvc.perform(delete("/api/merchants/{id}", saved.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean merchantExistsInEs = merchantSearchRepository.exists(saved.getId());
        assertThat(merchantExistsInEs).isFalse();

        // Validate the database is empty
        List<Merchant> merchants = merchantRepository.findAll();
        assertThat(merchants).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMerchant() throws Exception {
        // Initialize the database
        Merchant saved = merchantRepository.saveAndFlush(merchant.toMerchant());
        merchantSearchRepository.save(saved);

        // Search the merchant
        restMerchantMockMvc.perform(get("/api/_search/merchants?query=id:" + saved.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saved.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.[*].firstLineOfAddress").value(hasItem(DEFAULT_FIRST_LINE_OF_ADDRESS)))
            .andExpect(jsonPath("$.[*].secondLineOfAddress").value(hasItem(DEFAULT_SECOND_LINE_OF_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].postcode").value(hasItem(DEFAULT_POSTCODE)))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK)))
            .andExpect(jsonPath("$.[*].instagram").value(hasItem(DEFAULT_INSTAGRAM)))
            .andExpect(jsonPath("$.[*].dateCreated").exists())
            .andExpect(jsonPath("$.[*].lastEdited").exists());
    }
}
