package com.yalday.app.web.rest;

import com.yalday.app.FinalYaldayMerchantApp;
import com.yalday.app.domain.User;
import com.yalday.app.domain.dto.UserDTO;
import com.yalday.app.repository.UserRepository;
import com.yalday.app.repository.search.UserSearchRepository;
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
 * Test class for the UserResource REST controller.
 *
 * @see UserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinalYaldayMerchantApp.class)
public class UserResourceIntTest {

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserSearchRepository userSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUserMockMvc;

    private UserDTO user;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserResource userResource = new UserResource();
        ReflectionTestUtils.setField(userResource, "userSearchRepository", userSearchRepository);
        ReflectionTestUtils.setField(userResource, "userRepository", userRepository);
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserDTO createEntity() {
        return UserDTO.builder()
                .email(DEFAULT_EMAIL)
                .build();
    }

    @Before
    public void initTest() {
        userSearchRepository.deleteAll();
        user = createEntity();
    }

    @Test
    @Transactional
    public void createUser() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        // Create the User

        Timestamp before = Timestamp.from(Instant.now(Clock.systemDefaultZone()));
        restUserMockMvc.perform(post("/api/users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(user)))
                .andExpect(status().isCreated());
        Timestamp after = Timestamp.from(Instant.now(Clock.systemDefaultZone()));

        // Validate the User in the database
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(databaseSizeBeforeCreate + 1);
        User testUser = users.get(users.size() - 1);
        assertThat(testUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testUser.getDateCreated()).isBetween(before, after, true, true);
        assertThat(testUser.getLastEdited()).isBetween(before, after, true, true);

        // Validate the User in ElasticSearch
        User userEs = userSearchRepository.findOne(testUser.getId());
        assertThat(userEs).isEqualToComparingFieldByField(testUser);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRepository.findAll().size();
        // set the field null
        user.setEmail(null);

        // Create the User, which fails.

        restUserMockMvc.perform(post("/api/users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(user)))
                .andExpect(status().isBadRequest());

        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUsers() throws Exception {
        // Initialize the database
        User saved = userRepository.saveAndFlush(user.toUser());

        // Get all the users
        restUserMockMvc.perform(get("/api/users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saved.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    public void getUser() throws Exception {
        // Initialize the database
        User saved = userRepository.saveAndFlush(user.toUser());

        // Get the user
        restUserMockMvc.perform(get("/api/users/{id}", saved.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(saved.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUser() throws Exception {
        // Get the user
        restUserMockMvc.perform(get("/api/users/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUser() throws Exception {
        // Initialize the database
        User saved = userRepository.saveAndFlush(user.toUser());
        userSearchRepository.save(saved);
        int databaseSizeBeforeUpdate = userRepository.findAll().size();

        // Update the user
        UserDTO updatedUser = UserDTO.builder()
                .email(UPDATED_EMAIL)
                .build();

        Thread.sleep(50); // so the last edited time is different

        Timestamp updateTime = Timestamp.from(Instant.now(Clock.systemDefaultZone()));
        restUserMockMvc.perform(post("/api/users/{id}", saved.getId())
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUser)))
                .andExpect(status().isOk());

        // Validate the User in the database
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(databaseSizeBeforeUpdate);
        User testUser = users.get(users.size() - 1);
        assertThat(testUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testUser.getDateCreated()).isEqualTo(saved.getDateCreated());
        assertThat(testUser.getLastEdited()).isAfter(updateTime);

        // Validate the User in ElasticSearch
        User userEs = userSearchRepository.findOne(testUser.getId());
        assertThat(userEs).isEqualToComparingFieldByField(testUser);
    }

    @Test
    @Transactional
    public void deleteUser() throws Exception {
        // Initialize the database
        User saved = userRepository.saveAndFlush(user.toUser());
        userSearchRepository.save(saved);
        int databaseSizeBeforeDelete = userRepository.findAll().size();

        // Get the user
        restUserMockMvc.perform(delete("/api/users/{id}", saved.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean userExistsInEs = userSearchRepository.exists(saved.getId());
        assertThat(userExistsInEs).isFalse();

        // Validate the database is empty
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchUser() throws Exception {
        // Initialize the database
        User saved = userRepository.saveAndFlush(user.toUser());
        userSearchRepository.save(saved);

        // Search the user
        restUserMockMvc.perform(get("/api/_search/users?query=id:" + saved.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saved.getId().intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].dateCreated").exists())
            .andExpect(jsonPath("$.[*].lastEdited").exists());
    }
}