package com.yalday.app.web.rest;

import com.yalday.app.FinalYaldayMerchantApp;
import com.yalday.app.domain.dto.CustomerDTO;
import com.yalday.app.repository.CustomerRepository;
import com.yalday.app.repository.search.CustomerSearchRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;

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
    public void createCustomer(){

    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {

    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {

    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {

    }

    @Test
    @Transactional
    public void getCustomer() throws Exception {

    }

    @Test
    @Transactional
    public void getNonExistingCustomer() throws Exception {

    }

    @Test
    @Transactional
    public void updateCustomer() throws Exception {

    }

    @Test
    @Transactional
    public void deleteMerchant() throws Exception {

    }

    @Test
    @Transactional
    public void searchMerchant() throws Exception {

    }
}
