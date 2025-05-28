package com.dqs.eventdrivensearch.queryDistribution.model;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexPartitionRepository extends MongoRepository<IndexPartition, String> {
}
