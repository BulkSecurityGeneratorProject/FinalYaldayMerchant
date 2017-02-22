package com.yalday.app.web.rest;

import com.yalday.app.FinalYaldayMerchantApp;
import com.yalday.app.domain.Customer;
import com.yalday.app.domain.Merchant;
import com.yalday.app.domain.dto.CustomerDTO;
import com.yalday.app.domain.dto.MerchantDTO;
import com.yalday.app.repository.CustomerRepository;
import com.yalday.app.repository.search.CustomerSearchRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the CustomerResource REST controller.
 *
 * @see CustomerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinalYaldayMerchantApp.class)
public class CustomerResourceIntTest {

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

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private CustomerSearchRepository customerSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMerchantMockMvc;

    private CustomerDTO customer;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        CustomerResource customerResource = new CustomerResource();
        ReflectionTestUtils.setField(customerResource, "customerSearchRepository", customerSearchRepository);
        ReflectionTestUtils.setField(customerResource, "customerRepository", customerRepository);
        this.restMerchantMockMvc = MockMvcBuilders.standaloneSetup(customerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerDTO createEntity(){
        return CustomerDTO.builder()
                .name(DEFAULT_NAME)
                .email(DEFAULT_EMAIL)
                .firstLineOfAddress(DEFAULT_FIRST_LINE_OF_ADDRESS)
                .secondLineOfAddress(DEFAULT_SECOND_LINE_OF_ADDRESS)
                .city(DEFAULT_CITY)
                .postcode(DEFAULT_POSTCODE)
                .facebook(DEFAULT_FACEBOOK)
                .instagram(DEFAULT_INSTAGRAM)
                .build();
    }

    @Before
    public void initTest(){
        customerSearchRepository.deleteAll();
        customer = createEntity();
    }

    @Test
    @Transactional
    public void createCustomer() throws Exception{
        int databaseSizeBeforeCreate = customerRepository.findAll().size();

        // Create the Merchant

        Timestamp before = Timestamp.from(Instant.now(Clock.systemDefaultZone()));
        restMerchantMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customer)))
                .andExpect(status().isCreated());
        Timestamp after = Timestamp.from(Instant.now(Clock.systemDefaultZone()));

        // Validate the Customer in the database
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeCreate + 1);
        Customer testCustomer = customers.get(customers.size() - 1);
        assertThat(testCustomer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCustomer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCustomer.getFirstLineOfAddress()).isEqualTo(DEFAULT_FIRST_LINE_OF_ADDRESS);
        assertThat(testCustomer.getSecondLineOfAddress()).isEqualTo(DEFAULT_SECOND_LINE_OF_ADDRESS);
        assertThat(testCustomer.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testCustomer.getPostcode()).isEqualTo(DEFAULT_POSTCODE);
        assertThat(testCustomer.getFacebook()).isEqualTo(DEFAULT_FACEBOOK);
        assertThat(testCustomer.getInstagram()).isEqualTo(DEFAULT_INSTAGRAM);
        assertThat(testCustomer.getDateCreated()).isBetween(before, after, true, true);
        assertThat(testCustomer.getLastEdited()).isBetween(before, after, true, true);

        // Validate the Customer in ElasticSearch
        Customer customerEs = customerSearchRepository.findOne(testCustomer.getId());
        assertThat(customerEs).isEqualToComparingFieldByField(testCustomer);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setName(null);

        // Create the Customer, which fails.

        restMerchantMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customer)))
                .andExpect(status().isBadRequest());

        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerRepository.findAll().size();
        // set the field null
        customer.setEmail(null);

        // Create the Customer, which fails.

        restMerchantMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customer)))
                .andExpect(status().isBadRequest());

        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {
        // Initialize the database
        Customer saved = customerRepository.saveAndFlush(customer.toCustomer());

        // Get all the customers
        restMerchantMockMvc.perform(get("/api/customers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saved.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].firstLineOfAddress").value(hasItem(DEFAULT_FIRST_LINE_OF_ADDRESS)))
            .andExpect(jsonPath("$.[*].secondLineOfAddress").value(hasItem(DEFAULT_SECOND_LINE_OF_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].postcode").value(hasItem(DEFAULT_POSTCODE)))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK)))
            .andExpect(jsonPath("$.[*].instagram").value(hasItem(DEFAULT_INSTAGRAM)));
    }

    @Test
    @Transactional
    public void getCustomer() throws Exception {
        // Initialize the database
        Customer saved = customerRepository.saveAndFlush(customer.toCustomer());

        // Get the customer
        restMerchantMockMvc.perform(get("/api/customers/{id}", saved.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(saved.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.firstLineOfAddress").value(DEFAULT_FIRST_LINE_OF_ADDRESS))
            .andExpect(jsonPath("$.secondLineOfAddress").value(DEFAULT_SECOND_LINE_OF_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.postcode").value(DEFAULT_POSTCODE))
            .andExpect(jsonPath("$.facebook").value(DEFAULT_FACEBOOK))
            .andExpect(jsonPath("$.instagram").value(DEFAULT_INSTAGRAM));
    }

    @Test
    @Transactional
    public void getNonExistingCustomer() throws Exception {
        // Get the customer
        restMerchantMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomer() throws Exception {
        // Initialize the database
        Customer saved = customerRepository.saveAndFlush(customer.toCustomer());
        customerSearchRepository.save(saved);
        int databaseSizeBeforeUpdate = customerRepository.findAll().size();

        // Update the customer
        CustomerDTO updatedCustomer = CustomerDTO.builder()
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .firstLineOfAddress(UPDATED_FIRST_LINE_OF_ADDRESS)
            .secondLineOfAddress(UPDATED_SECOND_LINE_OF_ADDRESS)
            .city(UPDATED_CITY)
            .postcode(UPDATED_POSTCODE)
            .facebook(UPDATED_FACEBOOK)
            .instagram(UPDATED_INSTAGRAM)
            .build();

        Thread.sleep(50); // so the last edited time is different

        Timestamp updateTime = Timestamp.from(Instant.now(Clock.systemDefaultZone()));
        restMerchantMockMvc.perform(post("/api/customers/{id}", saved.getId())
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustomer)))
            .andExpect(status().isOk());

        // Validate the Merchant in the database
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeUpdate);
        Customer testCustomer = customers.get(customers.size() - 1);
        assertThat(testCustomer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCustomer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCustomer.getFirstLineOfAddress()).isEqualTo(UPDATED_FIRST_LINE_OF_ADDRESS);
        assertThat(testCustomer.getSecondLineOfAddress()).isEqualTo(UPDATED_SECOND_LINE_OF_ADDRESS);
        assertThat(testCustomer.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCustomer.getPostcode()).isEqualTo(UPDATED_POSTCODE);
        assertThat(testCustomer.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
        assertThat(testCustomer.getInstagram()).isEqualTo(UPDATED_INSTAGRAM);
        assertThat(testCustomer.getDateCreated()).isEqualTo(saved.getDateCreated());
        assertThat(testCustomer.getLastEdited()).isAfter(updateTime);

        // Validate the Customer in ElasticSearch
        Customer customerEs = customerSearchRepository.findOne(testCustomer.getId());
        assertThat(customerEs).isEqualToComparingFieldByField(testCustomer);
    }

    @Test
    @Transactional
    public void deleteCustomer() throws Exception {
        // Initialize the database
        Customer saved = customerRepository.saveAndFlush(customer.toCustomer());
        customerSearchRepository.save(saved);
        int databaseSizeBeforeDelete = customerRepository.findAll().size();

        // Get the customer
        restMerchantMockMvc.perform(delete("/api/customers/{id}", saved.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean customerExistsInEs = customerSearchRepository.exists(saved.getId());
        assertThat(customerExistsInEs).isFalse();

        // Validate the database is empty
        List<Customer> customers = customerRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCustomer() throws Exception {
        // Initialize the database
        Customer saved = customerRepository.saveAndFlush(customer.toCustomer());
        customerSearchRepository.save(saved);

        // Search the customer
        restMerchantMockMvc.perform(get("/api/_search/customers?query=id:" + saved.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saved.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
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
