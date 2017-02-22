package com.yalday.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yalday.app.domain.Customer;
import com.yalday.app.domain.dto.CustomerDTO;
import com.yalday.app.repository.CustomerRepository;
import com.yalday.app.repository.search.CustomerSearchRepository;
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
 * REST controller for managing Customer.
 */
@RestController
@RequestMapping("/api")
public class CustomerResource {

    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    @Inject
    private CustomerRepository customerRepository;

    @Inject
    private CustomerSearchRepository customerSearchRepository;
    /**
     * POST  /customer : Create a new customer.
     *
     * @param customerDTO the customer to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customer, or with status 400 (Bad Request) if the merchant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customers")
    @Timed
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws URISyntaxException {
        log.debug("REST request to save Customer : {}", customerDTO);
        Customer result = customerRepository.save(customerDTO.toCustomer());
        customerSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/customer/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("customer", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /customers : Updates an existing customer.
     *
     * @param customer the customer to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customer,
     * or with status 400 (Bad Request) if the customer is not valid,
     * or with status 500 (Internal Server Error) if the customer couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customers/{id}")
    @Timed
    public ResponseEntity<Customer> updateCustomer(@NotNull @PathVariable Long id, @Valid @RequestBody CustomerDTO customer) throws URISyntaxException {
        log.debug("REST request to update Customer : {}", customer);
        Customer result = customerRepository.saveAndFlush(customer.toCustomer(Optional.of(id)));
        customerSearchRepository.save(result);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("customer", id.toString()))
                .body(result);
    }

    /**
     * GET  /customers : get all the customers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of customers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/customers")
    @Timed
    public ResponseEntity<List<Customer>> getAllCustomers(Pageable pageable)
            throws URISyntaxException {
        log.debug("REST request to get a page of Customers");
        Page<Customer> page = customerRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /customers/:id : get the "id" customer.
     *
     * @param id the id of the customer to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customer, or with status 404 (Not Found)
     */
    @GetMapping("/customers/{id}")
    @Timed
    public ResponseEntity<Customer> getCustomer(@PathVariable Long id) {
        log.debug("REST request to get Customer : {}", id);
        Customer customer = customerRepository.findOne(id);
        return Optional.ofNullable(customer)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /customers/:id : delete the "id" customer.
     *
     * @param id the id of the customer to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/customers/{id}")
    @Timed
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        log.debug("REST request to delete Customer : {}", id);
        customerRepository.delete(id);
        customerSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("customer", id.toString())).build();
    }

    /**
     * SEARCH  /_search/customers?query=:query : search for the customer corresponding
     * to the query.
     *
     * @param query the query of the customer search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/customers")
    @Timed
    public ResponseEntity<List<Customer>> searchCustomers(@RequestParam String query, Pageable pageable)
            throws URISyntaxException {
        log.debug("REST request to search for a page of Customers for query {}", query);
        Page<Customer> page = customerSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/customers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
