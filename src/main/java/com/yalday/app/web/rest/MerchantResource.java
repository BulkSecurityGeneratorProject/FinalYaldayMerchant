package com.yalday.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.yalday.app.domain.Merchant;
import com.yalday.app.domain.dto.MerchantDTO;
import com.yalday.app.repository.MerchantRepository;
import com.yalday.app.repository.search.MerchantSearchRepository;
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
 * REST controller for managing Merchant.
 */
@RestController
@RequestMapping("/api")
public class MerchantResource {

    private final Logger log = LoggerFactory.getLogger(MerchantResource.class);

    @Inject
    private MerchantRepository merchantRepository;

    @Inject
    private MerchantSearchRepository merchantSearchRepository;

    /**
     * POST  /merchants : Create a new merchant.
     *
     * @param merchantDTO the merchant to create
     * @return the ResponseEntity with status 201 (Created) and with body the new merchant, or with status 400 (Bad Request) if the merchant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/merchants")
    @Timed
    public ResponseEntity<Merchant> createMerchant(@Valid @RequestBody MerchantDTO merchantDTO) throws URISyntaxException {
        log.debug("REST request to save Merchant : {}", merchantDTO);
        Merchant result = merchantRepository.save(merchantDTO.toMerchant());
        merchantSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/merchants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("merchant", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /merchants : Updates an existing merchant.
     *
     * @param merchant the merchant to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated merchant,
     * or with status 400 (Bad Request) if the merchant is not valid,
     * or with status 500 (Internal Server Error) if the merchant couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/merchants/{id}")
    @Timed
    public ResponseEntity<Merchant> updateMerchant(@NotNull @PathVariable Long id, @Valid @RequestBody MerchantDTO merchant) throws URISyntaxException {
        log.debug("REST request to update Merchant : {}", merchant);
        Merchant result = merchantRepository.saveAndFlush(merchant.toMerchant(Optional.of(id)));
        merchantSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("merchant", id.toString()))
            .body(result);
    }

    /**
     * GET  /merchants : get all the merchants.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of merchants in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/merchants")
    @Timed
    public ResponseEntity<List<Merchant>> getAllMerchants(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Merchants");
        Page<Merchant> page = merchantRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/merchants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /merchants/:id : get the "id" merchant.
     *
     * @param id the id of the merchant to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the merchant, or with status 404 (Not Found)
     */
    @GetMapping("/merchants/{id}")
    @Timed
    public ResponseEntity<Merchant> getMerchant(@PathVariable Long id) {
        log.debug("REST request to get Merchant : {}", id);
        Merchant merchant = merchantRepository.findOne(id);
        return Optional.ofNullable(merchant)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /merchants/:id : delete the "id" merchant.
     *
     * @param id the id of the merchant to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/merchants/{id}")
    @Timed
    public ResponseEntity<Void> deleteMerchant(@PathVariable Long id) {
        log.debug("REST request to delete Merchant : {}", id);
        merchantRepository.delete(id);
        merchantSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("merchant", id.toString())).build();
    }

    /**
     * SEARCH  /_search/merchants?query=:query : search for the merchant corresponding
     * to the query.
     *
     * @param query the query of the merchant search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/merchants")
    @Timed
    public ResponseEntity<List<Merchant>> searchMerchants(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Merchants for query {}", query);
        Page<Merchant> page = merchantSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/merchants");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
