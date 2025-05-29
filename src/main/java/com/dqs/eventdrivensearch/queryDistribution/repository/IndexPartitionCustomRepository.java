package com.dqs.eventdrivensearch.queryDistribution.repository;

import com.dqs.eventdrivensearch.queryDistribution.model.QueryFilter;

import java.util.List;

public interface IndexPartitionCustomRepository {
    List<List<String>> findIndexPartitions(QueryFilter filter);
}
