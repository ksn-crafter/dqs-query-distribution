package com.dqs.eventdrivensearch.queryDistribution.model;

import java.util.List;

public interface IndexPartitionCustomRepository {
    List<List<String>> findIndexPartitions(QueryFilter filter);
}
