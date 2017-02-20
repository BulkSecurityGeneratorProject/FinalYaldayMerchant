package com.yalday.app.repository.search;

import com.yalday.app.domain.Customer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CustomerSearchRepository extends ElasticsearchRepository<Customer, Long> {
}
