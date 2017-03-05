package com.yalday.app.repository.search;


import com.yalday.app.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserSearchRepository extends ElasticsearchRepository<User, Long> {
}
