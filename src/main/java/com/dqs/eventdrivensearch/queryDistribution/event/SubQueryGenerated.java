package com.dqs.eventdrivensearch.queryDistribution.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class SubQueryGenerated {

    @JsonProperty
    private String subQueryId;

    @JsonProperty
    private String queryId;

    @JsonProperty
    private String tenantId;

    @JsonProperty
    private List<String> partitionIds;

    @JsonProperty
    private int totalSubqueries;

    public SubQueryGenerated() { }

    public SubQueryGenerated(String subQueryId, String queryId, String tenantId, List<String> partitionIds, int totalSubqueries) {
        this.subQueryId = subQueryId;
        this.queryId = queryId;
        this.tenantId = tenantId;
        this.partitionIds = partitionIds;
        this.totalSubqueries = totalSubqueries;
    }

    public String subQueryId() {
        return subQueryId;
    }

    public String queryId() {
        return queryId;
    }

    public String tenantId() {
        return tenantId;
    }

    public List<String> partitionIds() {
        return partitionIds;
    }

    public int totalSubqueries() {
        return totalSubqueries;
    }
}
