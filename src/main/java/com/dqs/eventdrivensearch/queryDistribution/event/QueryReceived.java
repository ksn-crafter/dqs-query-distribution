package com.dqs.eventdrivensearch.queryDistribution.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class QueryReceived {

    @JsonProperty
    private String queryId;

    @JsonProperty
    private String tenantId;

    @JsonProperty
    private String term;

    @JsonProperty
    private int yearStart;

    @JsonProperty
    private int yearEnd;

    @JsonProperty
    private LocalDateTime creationTime;

    public QueryReceived() {}

    public QueryReceived(String queryId, String tenantId, String term, int yearStart, int yearEnd, LocalDateTime creationTime) {
        this.queryId = queryId;
        this.tenantId = tenantId;
        this.term = term;
        this.yearStart = yearStart;
        this.yearEnd = yearEnd;
        this.creationTime = creationTime;
    }

    public String queryId() {
        return queryId;
    }

    public String tenantId() {
        return tenantId;
    }

    public String term() {
        return term;
    }

    public int yearStart() {
        return yearStart;
    }

    public int yearEnd() {
        return yearEnd;
    }

    public LocalDateTime creationTime() {
        return creationTime;
    }
}