package com.dqs.eventdrivensearch.queryDistribution.service;

import com.dqs.eventdrivensearch.queryDistribution.model.IndexPartition;
import com.dqs.eventdrivensearch.queryDistribution.model.QueryFilter;
import com.dqs.eventdrivensearch.queryDistribution.repository.IndexPartitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexPartitionService {

    @Autowired
    private IndexPartitionRepository repository;

    public List<List<String>> findIndexPartitions(QueryFilter filter) {
        return repository.findIndexPartitions(filter);
    }
    public List<IndexPartition> insertAll(List<IndexPartition> partitions){
        return repository.saveAll(partitions);
    }
    public void deleteAll(){
        repository.deleteAll();
    }


}
