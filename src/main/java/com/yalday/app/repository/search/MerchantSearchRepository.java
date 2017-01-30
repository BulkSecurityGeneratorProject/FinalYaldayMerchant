package com.yalday.app.repository.search;

import com.yalday.app.domain.Merchant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Merchant entity.
 */
public interface MerchantSearchRepository extends ElasticsearchRepository<Merchant, Long> {
}
