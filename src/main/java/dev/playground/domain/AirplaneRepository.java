package dev.playground.domain;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AirplaneRepository extends ElasticsearchRepository<Airplane, String> {

    Airplane findOneByName(String name);

}
