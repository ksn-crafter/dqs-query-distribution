package com.dqs.eventdrivensearch.queryDistribution.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "index_partitions")
public record IndexPartition(@Id String partitionId, String tenant, String filePath, int yearStart, int yearEnd) {}
