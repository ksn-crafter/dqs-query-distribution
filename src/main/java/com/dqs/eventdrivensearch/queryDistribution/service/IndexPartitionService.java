package com.dqs.eventdrivensearch.queryDistribution.service;

import com.dqs.eventdrivensearch.queryDistribution.repository.IndexPartitionCustomRepository;
import com.dqs.eventdrivensearch.queryDistribution.model.QueryFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexPartitionService {

    @Autowired
    private IndexPartitionCustomRepository repository;

    public List<List<String>> findIndexPartitions(QueryFilter filter) {
        return repository.findIndexPartitions(filter);
    }
}
