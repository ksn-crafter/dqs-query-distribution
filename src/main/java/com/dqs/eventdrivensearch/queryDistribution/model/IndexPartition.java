package com.dqs.eventdrivensearch.queryDistribution.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "indexPartition")
public record IndexPartition(@Id String partitionId, String tenantId, String filepath, int yearStart, int yearEnd) {}
