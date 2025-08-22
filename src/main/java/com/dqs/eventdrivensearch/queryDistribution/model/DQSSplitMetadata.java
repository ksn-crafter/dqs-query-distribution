package com.dqs.eventdrivensearch.queryDistribution.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dqs_split_metadata")
public record DQSSplitMetadata(
    @Id String id,
    String splitId,
    String indexUid,
    String tenantId,
    long footerOffsetsStart,
    long footerOffsetsEnd
) {}
