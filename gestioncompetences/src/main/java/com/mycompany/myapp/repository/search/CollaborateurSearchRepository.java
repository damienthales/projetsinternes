package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Collaborateur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Collaborateur entity.
 */
public interface CollaborateurSearchRepository extends ElasticsearchRepository<Collaborateur, Long> {
}
