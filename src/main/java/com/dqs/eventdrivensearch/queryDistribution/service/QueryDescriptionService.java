package com.dqs.eventdrivensearch.queryDistribution.service;

import com.dqs.eventdrivensearch.queryDistribution.model.QueryDescription;
import com.dqs.eventdrivensearch.queryDistribution.repository.QueryDescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class QueryDescriptionService {

    @Autowired
    private QueryDescriptionRepository repository;

    @Transactional
    public QueryDescription createQueryDescription(QueryDescription queryDescription) {
        return repository.save(queryDescription);
    }

    @Transactional(readOnly = true)
    public QueryDescription getQueryDescriptionById(UUID queryId) {
        return repository.findById(queryId).orElse(null);
    }
}
