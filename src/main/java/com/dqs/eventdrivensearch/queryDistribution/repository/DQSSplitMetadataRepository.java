package com.dqs.eventdrivensearch.queryDistribution.repository;

import com.dqs.eventdrivensearch.queryDistribution.model.DQSSplitMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DQSSplitMetadataRepository extends MongoRepository<DQSSplitMetadata, String> {

    /**
     * Finds DQSSplitMetadata by tenantId.
     *
     * @param tenantId the tenant ID to search for
     * @return a list of DQSSplitMetadata associated with the given tenantId
     */
    List<DQSSplitMetadata> findByTenantId(String tenantId);
}