package com.dqs.eventdrivensearch.queryDistribution.repository;

import com.dqs.eventdrivensearch.queryDistribution.model.QueryDescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface QueryDescriptionRepository extends MongoRepository<QueryDescription, UUID> { }
