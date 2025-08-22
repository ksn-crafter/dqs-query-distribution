package com.dqs.eventdrivensearch.queryDistribution.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dqs_task")
public record DQSTask(
    @Id String taskId,
    String queryId,
    String dqsSplitMetadataId,
    String status,
    int workerId,
    long creationTimestamp,
    Long completionTimeStamp
) {}
