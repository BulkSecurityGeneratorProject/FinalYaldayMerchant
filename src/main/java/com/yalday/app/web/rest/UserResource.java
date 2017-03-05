package com.yalday.app.web.rest;


import com.codahale.metrics.annotation.Timed;
import com.yalday.app.domain.User;
import com.yalday.app.domain.dto.UserDTO;
import com.yalday.app.repository.UserRepository;
import com.yalday.app.repository.search.UserSearchRepository;
import com.yalday.app.web.rest.util.HeaderUtil;
import com.yalday.app.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing User.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserSearchRepository userSearchRepository;

    /**
     * POST  /user : Create a new user.
     *
     * @param userDTO the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the merchant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/users")
    @Timed
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);
        User result = userRepository.save(userDTO.toUser());
        userSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/user/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("user", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /users : Updates an existing user.
     *
     * @param user the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the user is not valid,
     * or with status 500 (Internal Server Error) if the user couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/users/{id}")
    @Timed
    public ResponseEntity<User> updateUser(@NotNull @PathVariable Long id, @Valid @RequestBody UserDTO user) throws URISyntaxException {
        log.debug("REST request to update User : {}", user);
        User result = userRepository.saveAndFlush(user.toUser(Optional.of(id)));
        userSearchRepository.save(result);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("user", id.toString()))
                .body(result);
    }

    /**
     * GET  /users : get all the users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of users in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/users")
    @Timed
    public ResponseEntity<List<User>> getAllUsers(Pageable pageable)
            throws URISyntaxException {
        log.debug("REST request to get a page of Users");
        Page<User> page = userRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /users/:id : get the "id" user.
     *
     * @param id the id of the user to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the user, or with status 404 (Not Found)
     */
    @GetMapping("/users/{id}")
    @Timed
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        log.debug("REST request to get User : {}", id);
        User user = userRepository.findOne(id);
        return Optional.ofNullable(user)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /users/:id : delete the "id" user.
     *
     * @param id the id of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/users/{id}")
    @Timed
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.debug("REST request to delete User : {}", id);
        userRepository.delete(id);
        userSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("user", id.toString())).build();
    }

    /**
     * SEARCH  /_search/users?query=:query : search for the user corresponding
     * to the query.
     *
     * @param query the query of the user search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/users")
    @Timed
    public ResponseEntity<List<User>> searchUsers(@RequestParam String query, Pageable pageable)
            throws URISyntaxException {
        log.debug("REST request to search for a page of Users for query {}", query);
        Page<User> page = userSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
